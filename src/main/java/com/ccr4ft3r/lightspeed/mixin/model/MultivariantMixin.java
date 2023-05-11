package com.ccr4ft3r.lightspeed.mixin.model;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Mixin(VariantList.class)
public abstract class MultivariantMixin implements IUnbakedModel {

    @Shadow
    public abstract List<Variant> getVariants();

    private Collection<RenderMaterial> materials;
    private Collection<ResourceLocation> dependencies;

    @Inject(method = "getMaterials", at = @At("HEAD"), cancellable = true)
    public void getMaterialsHeadInjected(Function<ResourceLocation, IUnbakedModel> p_111855_, Set<Pair<String, String>> p_111856_, CallbackInfoReturnable<Collection<RenderMaterial>> cir) {
        if (GlobalCache.isEnabled && materials != null && GlobalCache.shouldCacheMaterials)
            cir.setReturnValue(materials);
    }

    @Inject(method = "getMaterials", at = @At("RETURN"))
    public void getMaterialsReturnInjected(Function<ResourceLocation, IUnbakedModel> p_111855_, Set<Pair<String, String>> p_111856_, CallbackInfoReturnable<Collection<RenderMaterial>> cir) {
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
    public void bakeHeadInjected(ModelBakery p_111850_, Function<RenderMaterial, TextureAtlasSprite> p_111851_, IModelTransform p_111852_, ResourceLocation p_111853_, CallbackInfoReturnable<IBakedModel> cir) {
        if (!GlobalCache.isEnabled) {
            return;
        }
        if (this.getVariants().isEmpty()) {
            cir.setReturnValue(null);
        } else {
            WeightedBakedModel.Builder weightedBuilder = new WeightedBakedModel.Builder();
            //noinspection CodeBlock2Expr
            this.getVariants().parallelStream().forEach(variant -> {
                weightedBuilder.add(p_111850_.getBakedModel(variant.getModelLocation(), variant, p_111851_), variant.getWeight());
            });
            cir.setReturnValue(weightedBuilder.build());
        }
    }
}