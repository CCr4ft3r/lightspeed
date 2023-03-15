package com.ccr4ft3r.lightspeed.cache;

import com.ccr4ft3r.lightspeed.interfaces.ICache;
import com.ccr4ft3r.lightspeed.util.CacheUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourcePackType;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ccr4ft3r.lightspeed.util.CacheUtil.*;

public class GlobalCache {

    public static Boolean isEnabled = true;
    public static Boolean shouldCacheWalkedPaths = true;
    public static Boolean shouldCacheEmptyNamespaces = true;
    public static final Map<CharSequence, List<String>> SPLITTED_STRINGS_BY_SEQUENCE = Maps.newConcurrentMap();
    public static final Map<String, String> CANONICAL_PATH_PER_FILE = Maps.newConcurrentMap();
    private static final Set<ICache> CACHES = Sets.newConcurrentHashSet();
    public static final Map<String, Map<String, Boolean>> PERSISTED_EXISTENCES_BY_MOD = Maps.newConcurrentMap();
    public static final Map<String, Map<ResourcePackType, Set<String>>> PERSISTED_NAMESPACES_BY_MOD = Maps.newConcurrentMap();
    public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public static void add(ICache cache) {
        CACHES.add(cache);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void disablePersistAndClear() {
        isEnabled = false;
        CacheUtil.getCacheFiles(HAS_RESOURCE_CACHE_DIR).forEach(File::delete);
        CacheUtil.getCacheFiles(NAMESPACE_CACHE_DIR).forEach(File::delete);
        CACHES.forEach(ICache::persistAndClearCache);
        SPLITTED_STRINGS_BY_SEQUENCE.clear();
        CANONICAL_PATH_PER_FILE.clear();
        CACHES.clear();
        PERSISTED_EXISTENCES_BY_MOD.clear();
        PERSISTED_NAMESPACES_BY_MOD.clear();
    }
}