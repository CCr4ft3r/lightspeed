package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(net.minecraft.server.packs.VanillaPackResources.class)
public abstract class VanillaPackResourcesMixin implements IPackResources {

    private final Map<String, Boolean> existencePerClientResource = Maps.newConcurrentMap();
    private final Map<String, Boolean> existencePerServerResource = Maps.newConcurrentMap();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initHeadInjected(PackMetadataSection p_143761_, String[] p_143762_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    @Inject(method = "hasResource", at = @At("HEAD"), cancellable = true)
    public void hasResourceHeadInjected(PackType p_10355_, ResourceLocation p_10356_, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        Boolean exists = exists(p_10355_, p_10356_.toString());
        if (exists != null)
            cir.setReturnValue(exists);
    }

    @Inject(method = "hasResource", at = @At("RETURN"))
    public void hasResourceReturnInjected(PackType p_10355_, ResourceLocation p_10356_, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        cacheExists(p_10355_, p_10356_.toString(), cir.getReturnValue());
    }

    public Boolean exists(PackType packType, String resourceName) {
        if (packType == PackType.CLIENT_RESOURCES)
            return existencePerClientResource.get(resourceName);
        return existencePerServerResource.get(resourceName);
    }

    public void cacheExists(PackType packType, String resourceName, boolean exists) {
        if (packType == PackType.CLIENT_RESOURCES)
            existencePerClientResource.put(resourceName, exists);
        else existencePerServerResource.put(resourceName, exists);
    }

    @Override
    public void persistAndClearCache() {
        existencePerClientResource.clear();
        existencePerServerResource.clear();
        getExistenceByResource().clear();
    }
}