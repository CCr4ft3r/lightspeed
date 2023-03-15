package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.packs.DelegatingResourcePack;
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
    protected abstract List<IResourcePack> getCandidatePacks(ResourcePackType type, ResourceLocation location);

    @Inject(method = "hasResource(Lnet/minecraft/resources/ResourcePackType;Lnet/minecraft/util/ResourceLocation;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void hasResourceHeadInjected(ResourcePackType type, ResourceLocation location, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        cir.setReturnValue(getCandidatePacks(type, location).parallelStream().anyMatch(p -> p.hasResource(type, location)));
    }

    @Redirect(method = "getResources", at = @At(value = "INVOKE", target = "Ljava/util/List;stream()Ljava/util/stream/Stream;"))
    public <R> Stream<R> getResourcesStreamRedirected(List<R> instance) {
        return !GlobalCache.isEnabled ? instance.stream() : instance.parallelStream();
    }
}