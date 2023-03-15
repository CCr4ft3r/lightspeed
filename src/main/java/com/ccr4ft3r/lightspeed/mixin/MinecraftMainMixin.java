package com.ccr4ft3r.lightspeed.mixin;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.util.CacheUtil;
import net.minecraft.client.main.Main;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Map;

import static com.ccr4ft3r.lightspeed.util.CacheUtil.*;

@Mixin(Main.class)
public class MinecraftMainMixin {

    @Inject(method = "main", at = @At(value = "HEAD", shift = At.Shift.AFTER))
    private static void mainTryDetecVersionInjected(String[] p_129642_, CallbackInfo ci) {
        GlobalCache.EXECUTOR.execute(() -> {
            loadPersistedCaches(HAS_RESOURCE_CACHE_DIR, GlobalCache.PERSISTED_EXISTENCES_BY_MOD);
            loadPersistedCaches(NAMESPACE_CACHE_DIR, GlobalCache.PERSISTED_NAMESPACES_BY_MOD);
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static <K, V> void loadPersistedCaches(File dir, Map<String, Map<K, V>> targetMap) {
        dir.mkdirs();
        CacheUtil.getCacheFiles(dir).forEach(file -> {
            String id = FilenameUtils.getBaseName(file.getName());
            targetMap.put(id, CacheUtil.load(file));
        });
    }
}