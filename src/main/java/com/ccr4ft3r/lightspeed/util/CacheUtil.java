package com.ccr4ft3r.lightspeed.util;

import com.google.common.collect.Maps;
import net.minecraft.util.SharedConstants;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class CacheUtil {

    public static final File CACHE_DIR = Paths.get(FMLPaths.GAMEDIR.get().toString(), "lightspeed-cache",
        SharedConstants.getCurrentVersion().getId()).toFile();
    public static final File HAS_RESOURCE_CACHE_DIR = new File(CACHE_DIR, "hasResource");
    public static final File NAMESPACE_CACHE_DIR = new File(CACHE_DIR, "namespaces");

    public static Stream<File> getCacheFiles(File dir) {
        File[] caches = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".ser"));
        if (caches == null)
            return Stream.empty();
        return Arrays.stream(caches)
            .filter(file -> !file.isDirectory());
    }

    public static void persist(Map<?, ?> toPersist, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(toPersist);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) {
            LogUtils.getLogger().error("Cannot create cache file: {}", file, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> load(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));
            ConcurrentHashMap<K, V> loaded = (ConcurrentHashMap<K, V>) ois.readObject();
            ois.close();
            fis.close();
            return loaded;
        } catch (Exception e) {
            LogUtils.getLogger().error("Cannot load cache file: {}", file.getName(), e);
        }
        return Maps.newConcurrentMap();
    }
}