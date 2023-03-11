package com.ccr4ft3r.lightspeed.mixin.model;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.ccr4ft3r.lightspeed.util.UnbakedModelHelper.*;

@Mixin(MultiPart.class)
public abstract class MultiPartMixin implements UnbakedModel {

    private Collection<Material> materials;
    private Collection<ResourceLocation> dependencies;

    @Redirect(method = "getMaterials", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;flatMap(Ljava/util/function/Function;)Ljava/util/stream/Stream;"))
    public <T extends Selector, R extends Material> Stream<Material> getMaterialsFlatMapRedirected(Stream<Selector> instance, Function<? super T, ? extends Stream<? extends R>> function, Function<ResourceLocation, UnbakedModel> p_111976_, Set<Pair<String, String>> p_111977_) {
        if (!GlobalCache.isEnabled)
            return instance.flatMap((p_111981_) -> p_111981_.getVariant().getMaterials(p_111976_, p_111977_).stream());
        return instance.flatMap(selector -> getMaterialsStream(selector.getVariant(), p_111976_, p_111977_));
    }

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
}