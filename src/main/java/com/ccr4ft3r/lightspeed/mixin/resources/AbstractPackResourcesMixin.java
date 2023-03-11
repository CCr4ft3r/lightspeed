package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.AbstractPackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Map;

@Mixin(AbstractPackResources.class)
public abstract class AbstractPackResourcesMixin implements IPackResources {

    @Shadow
    protected abstract boolean hasResource(String p_10229_);

    private Map<String, Boolean> existenceByResource = Maps.newConcurrentMap();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(File p_10207_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    @Redirect(method = "hasResource(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/AbstractPackResources;hasResource(Ljava/lang/String;)Z"))
    public boolean hasResourceHasResourceRedirected(AbstractPackResources instance, String s) {
        if (!GlobalCache.isEnabled)
            return hasResource(s);
        Boolean exists = existenceByResource.get(s);
        if (exists == null) {
            existenceByResource.put(s, exists = hasResource(s));
        }
        return exists;
    }

    @Override
    public void persistAndClearCache() {
        existenceByResource.clear();
    }

    @Override
    public void setExistenceByResource(Map<String, Boolean> existenceByResource) {
        this.existenceByResource = existenceByResource;
    }

    @Override
    public Map<String, Boolean> getExistenceByResource() {
        return existenceByResource;
    }
}