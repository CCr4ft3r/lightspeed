package com.ccr4ft3r.lightspeed;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.util.CompatUtil;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.resource.ResourceCacheManager;

@Mod(ModConstants.MOD_ID)
public class Main {

    public Main() {
        updateCacheFlags();
    }

    private void updateCacheFlags() {
        if (ModList.get().isLoaded(ModConstants.SOPHISTICATED_STORAGE_ID) && ModList.get().isLoaded(ModConstants.JSON_THINGS_ID))
            GlobalCache.shouldCacheWalkedPaths = false;
        if (CompatUtil.existsClass("net.minecraftforge.resource.ResourceCacheManager")
            && ResourceCacheManager.shouldUseCache())
            GlobalCache.shouldCacheEmptyNamespaces = false;
        if (ModList.get().isLoaded(ModConstants.MULTIBLOCKED_ID))
            GlobalCache.shouldCacheMaterials = false;
    }
}