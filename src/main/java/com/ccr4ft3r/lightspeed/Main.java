package com.ccr4ft3r.lightspeed;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import static com.ccr4ft3r.lightspeed.ModConstants.*;

@Mod(ModConstants.MOD_ID)
public class Main {

    public Main() {
        updateCacheFlags();
    }

    private void updateCacheFlags() {
        if (ModList.get().isLoaded(ModConstants.SOPHISTICATED_STORAGE_ID) && ModList.get().isLoaded(ModConstants.JSON_THINGS_ID))
            GlobalCache.shouldCacheWalkedPaths = false;
        if (ModList.get().isLoaded(MULTIBLOCKED_ID))
            GlobalCache.shouldCacheMaterials = false;
    }
}