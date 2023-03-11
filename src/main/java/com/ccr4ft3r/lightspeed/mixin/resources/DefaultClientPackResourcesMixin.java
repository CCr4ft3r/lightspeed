package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.ccr4ft3r.lightspeed.util.CacheUtil;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.AssetIndex;
import net.minecraft.client.resources.DefaultClientPackResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.Map;

import static com.ccr4ft3r.lightspeed.cache.GlobalCache.*;
import static com.ccr4ft3r.lightspeed.util.CacheUtil.*;

@Mixin(DefaultClientPackResources.class)
public abstract class DefaultClientPackResourcesMixin implements IPackResources {

    private Map<String, Boolean> existenceByClientResource = Maps.newConcurrentMap();
    private Map<String, Boolean> existenceByServerResource = Maps.newConcurrentMap();

    private String id;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(PackMetadataSection p_174827_, AssetIndex p_174828_, CallbackInfo ci) {
        if (!GlobalCache.isEnabled)
            return;
        GlobalCache.add(this);
        id = Minecraft.getInstance().getLaunchedVersion();
        existenceByClientResource = PERSISTED_EXISTENCES_BY_MOD.computeIfAbsent(
            id + "-client", i -> Maps.newConcurrentMap());
        existenceByServerResource = PERSISTED_EXISTENCES_BY_MOD.computeIfAbsent(
            id + "-server", i -> Maps.newConcurrentMap());
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
            return existenceByClientResource.get(resourceName);
        return existenceByServerResource.get(resourceName);
    }

    public void cacheExists(PackType packType, String resourceName, boolean exists) {
        if (packType == PackType.CLIENT_RESOURCES)
            existenceByClientResource.put(resourceName, exists);
        else existenceByServerResource.put(resourceName, exists);
    }

    @Override
    public void persistAndClearCache() {
        CacheUtil.persist(existenceByClientResource, new File(HAS_RESOURCE_CACHE_DIR.getPath(), id + "-client.ser"));
        CacheUtil.persist(existenceByServerResource, new File(HAS_RESOURCE_CACHE_DIR.getPath(), id + "-server.ser"));
        existenceByClientResource.clear();
        existenceByServerResource.clear();
        getExistenceByResource().clear();
    }
}