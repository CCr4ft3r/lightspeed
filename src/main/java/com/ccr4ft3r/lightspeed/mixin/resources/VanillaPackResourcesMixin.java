package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.VanillaPack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(VanillaPack.class)
public abstract class VanillaPackResourcesMixin implements IPackResources {

    private final Map<String, Boolean> existencePerClientResource = Maps.newConcurrentMap();
    private final Map<String, Boolean> existencePerServerResource = Maps.newConcurrentMap();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initHeadInjected(String[] p_i47912_1_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    @Inject(method = "hasResource", at = @At("HEAD"), cancellable = true)
    public void hasResourceHeadInjected(ResourcePackType p_10355_, ResourceLocation p_10356_, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        Boolean exists = exists(p_10355_, p_10356_.toString());
        if (exists != null)
            cir.setReturnValue(exists);
    }

    @Inject(method = "hasResource", at = @At("RETURN"))
    public void hasResourceReturnInjected(ResourcePackType p_10355_, ResourceLocation p_10356_, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        cacheExists(p_10355_, p_10356_.toString(), cir.getReturnValue());
    }

    public Boolean exists(ResourcePackType packType, String resourceName) {
        if (packType == ResourcePackType.CLIENT_RESOURCES)
            return existencePerClientResource.get(resourceName);
        return existencePerServerResource.get(resourceName);
    }

    public void cacheExists(ResourcePackType packType, String resourceName, boolean exists) {
        if (packType == ResourcePackType.CLIENT_RESOURCES)
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