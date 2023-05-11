package com.ccr4ft3r.lightspeed.mixin.model;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

@Mixin(BlockModel.class)
public abstract class BlockModelMixin {

    private Collection<RenderMaterial> materials;

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
}