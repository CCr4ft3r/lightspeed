package com.ccr4ft3r.lightspeed.mixin.model;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.util.WeightedBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Mixin(MultiVariant.class)
public abstract class MultivariantMixin implements UnbakedModel {

    @Shadow
    public abstract List<Variant> getVariants();

    private Collection<Material> materials;
    private Collection<ResourceLocation> dependencies;

    @Inject(method = "getMaterials", at = @At("HEAD"), cancellable = true)
    public void getMaterialsHeadInjected(Function<ResourceLocation, UnbakedModel> p_111855_, Set<Pair<String, String>> p_111856_, CallbackInfoReturnable<Collection<Material>> cir) {
        if (GlobalCache.isEnabled && materials != null)
            cir.setReturnValue(materials);
    }

    @Inject(method = "getMaterials", at = @At("RETURN"))
    public void getMaterialsReturnInjected(Function<ResourceLocation, UnbakedModel> p_111855_, Set<Pair<String, String>> p_111856_, CallbackInfoReturnable<Collection<Material>> cir) {
        if (GlobalCache.isEnabled && materials == null)
            materials = cir.getReturnValue();
    }

    @Inject(method = "getDependencies", at = @At("HEAD"), cancellable = true)
    public void getDependenciesHeadInjected(CallbackInfoReturnable<Collection<ResourceLocation>> cir) {
        if (GlobalCache.isEnabled && dependencies != null)
            cir.setReturnValue(dependencies);
    }

    @Inject(method = "getDependencies", at = @At("RETURN"))
    public void getDependenciesReturnInjected(CallbackInfoReturnable<Collection<ResourceLocation>> cir) {
        if (GlobalCache.isEnabled && dependencies == null)
            dependencies = cir.getReturnValue();
    }

    @Inject(method = "bake", at = @At("HEAD"), cancellable = true)
    public void bakeHeadInjected(ModelBakery p_111850_, Function<Material, TextureAtlasSprite> p_111851_, ModelState p_111852_, ResourceLocation p_111853_, CallbackInfoReturnable<BakedModel> cir) {
        if (!GlobalCache.isEnabled) {
            return;
        }
        if (this.getVariants().isEmpty()) {
            cir.setReturnValue(null);
        } else {
            WeightedBuilder weightedBuilder = new WeightedBuilder();
            //noinspection CodeBlock2Expr
            this.getVariants().parallelStream().forEach(variant -> {
                weightedBuilder.add(p_111850_.bake(variant.getModelLocation(), variant, p_111851_), variant.getWeight());
            });
            cir.setReturnValue(weightedBuilder.build());
        }
    }
}