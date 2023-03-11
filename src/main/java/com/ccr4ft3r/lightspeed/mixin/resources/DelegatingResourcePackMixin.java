package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.resource.DelegatingResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Stream;

@Mixin(DelegatingResourcePack.class)
public abstract class DelegatingResourcePackMixin {

    @Shadow
    protected abstract List<PackResources> getCandidatePacks(PackType type, ResourceLocation location);

    @Inject(method = "hasResource(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void hasResourceHeadInjected(PackType type, ResourceLocation location, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        cir.setReturnValue(getCandidatePacks(type, location).parallelStream().anyMatch(p -> p.hasResource(type, location)));
    }

    @Redirect(method = "getResources", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
    public <R> Stream<R> getResourcesStreamRedirected(List<R> instance) {
        return !GlobalCache.isEnabled ? instance.stream() : instance.parallelStream();
    }
}