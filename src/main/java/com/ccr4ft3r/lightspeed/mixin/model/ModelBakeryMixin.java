package com.ccr4ft3r.lightspeed.mixin.model;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
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
    private Map<Triple<ResourceLocation, Transformation, Boolean>, BakedModel> bakedCache;

    @Inject(method = "bake(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/model/ModelState;Ljava/util/function/Function;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE), cancellable = true, remap = false, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void bakeContainsKeyInjected(ResourceLocation p_119350_, ModelState p_119351_, Function<Material, TextureAtlasSprite> sprites, CallbackInfoReturnable<BakedModel> cir, Triple<ResourceLocation, Transformation, Boolean> triple) {
        if (!GlobalCache.isEnabled) {
            return;
        }
        BakedModel bakedModel = this.bakedCache.get(triple);
        if (bakedModel != null)
            cir.setReturnValue(bakedModel);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Redirect(method = "bake(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/resources/model/ModelState;Ljava/util/function/Function;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"), remap = false)
    public boolean bakeContainsKeyRedirected(Map<Triple<ResourceLocation, Transformation, Boolean>, BakedModel> instance, Object o) {
        if (!GlobalCache.isEnabled) {
            return instance.containsKey(o);
        }
        return false;
    }
}