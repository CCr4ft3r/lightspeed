package com.ccr4ft3r.lightspeed.mixin.model;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.function.Function;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    @Final
    private Map<Triple<ResourceLocation, IModelTransform, Boolean>, IBakedModel> bakedCache;

    @Inject(method = "getBakedModel", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE), cancellable = true, remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void bakeContainsKeyInjected(ResourceLocation p_119350_, IModelTransform p_119351_, Function<RenderMaterial, TextureAtlasSprite> sprites, CallbackInfoReturnable<IBakedModel> cir, Triple<ResourceLocation, TransformationMatrix, Boolean> triple) {
        if (!GlobalCache.isEnabled) {
            return;
        }
        IBakedModel bakedModel = this.bakedCache.get(triple);
        if (bakedModel != null)
            cir.setReturnValue(bakedModel);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Redirect(method = "getBakedModel", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"), remap = false)
    public boolean bakeContainsKeyRedirected(Map<Triple<ResourceLocation, IModelTransform, Boolean>, IBakedModel> instance, Object o) {
        if (!GlobalCache.isEnabled) {
            return instance.containsKey(o);
        }
        return false;
    }
}