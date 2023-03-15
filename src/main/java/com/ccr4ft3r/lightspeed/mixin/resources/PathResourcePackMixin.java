package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.ccr4ft3r.lightspeed.interfaces.IPathResourcePack;
import com.ccr4ft3r.lightspeed.util.CacheUtil;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccr4ft3r.lightspeed.util.CacheUtil.*;

@Mixin(value = ModFileResourcePack.class)
public abstract class PathResourcePackMixin implements IPathResourcePack, IPackResources {

    @Shadow
    @Final
    private ModFile modFile;
    private final Map<ResourcePackType, Set<String>> namespacesByPackType = Maps.newConcurrentMap();
    private final Map<String, Path> inputPathByPath = Maps.newConcurrentMap();
    private final Map<ResourcePackType, Map<String, List<Path>>> filePathsByRootByPackType = initPathsMap();
    private String id;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(ModFile modFile, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
        this.id = modFile.getModFileInfo().getMods().get(0).getModId() + modFile.getModFileInfo().getMods().get(0).getVersion().toString()
            + "-" + FilenameUtils.getBaseName(modFile.getFilePath().toString()).replaceAll("[^a-zA-Z0-9.-]", "");
        setExistenceByResource(GlobalCache.PERSISTED_EXISTENCES_BY_MOD.computeIfAbsent(
            id, i -> Maps.newConcurrentMap()));
    }

    private static Map<ResourcePackType, Map<String, List<Path>>> initPathsMap() {
        Map<ResourcePackType, Map<String, List<Path>>> map = Maps.newConcurrentMap();
        for (ResourcePackType packType : ResourcePackType.values()) {
            map.put(packType, Maps.newConcurrentMap());
        }
        return map;
    }

    @Inject(method = "getNamespaces", at = @At("HEAD"), cancellable = true)
    public void getNamespacesHeadInjected(ResourcePackType type, CallbackInfoReturnable<Set<String>> cir) {
        if (!GlobalCache.isEnabled)
            return;
        Set<String> namespaces = getCachedNamespaces(type);
        if (namespaces != null)
            cir.setReturnValue(namespaces);
    }

    @Inject(method = "getNamespaces", at = @At("RETURN"))
    public void getNamespacesReturnInjected(ResourcePackType type, CallbackInfoReturnable<Set<String>> cir) {
        if (!GlobalCache.isEnabled)
            return;
        if (GlobalCache.shouldCacheEmptyNamespaces || cir.getReturnValue() != null && !cir.getReturnValue().isEmpty())
            cacheNamespaces(type, cir.getReturnValue());
    }

    @Inject(method = "hasResource(Ljava/lang/String;)Z", at = @At("HEAD"), cancellable = true)
    public void hasResourceHeadInjected(String name, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        Boolean exists = exists(name);
        if (exists != null)
            cir.setReturnValue(exists);
    }

    @Inject(method = "hasResource(Ljava/lang/String;)Z", at = @At("RETURN"))
    public void hasResourceReturnInjected(String name, CallbackInfoReturnable<Boolean> cir) {
        if (!GlobalCache.isEnabled)
            return;
        cacheExists(name, cir.getReturnValue());
    }

    @Inject(method = "getResources", at = @At(value = "INVOKE", target = "Ljava/nio/file/Path;getFileSystem()Ljava/nio/file/FileSystem;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public synchronized void getResourcesInjected(ResourcePackType type, String resourceNamespace, String pathIn, int maxDepth, Predicate<String> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir, Path root) {
        if (!GlobalCache.isEnabled)
            return;
        String resource = root.toString();
        Boolean exists = exists(resource);
        if (exists == null) {
            cacheExists(resource, exists = Files.exists(root));
        }
        if (!exists)
            cir.setReturnValue(Collections.emptyList());
    }

    @Redirect(method = "getResources", at = @At(value = "INVOKE", target = "Ljava/nio/file/Files;walk(Ljava/nio/file/Path;[Ljava/nio/file/FileVisitOption;)Ljava/util/stream/Stream;"))
    public synchronized Stream<Path> getResourcesWalkInjected(Path start, FileVisitOption[] options, ResourcePackType type, String resourceNamespace) throws IOException {
        if (!GlobalCache.isEnabled || !GlobalCache.shouldCacheWalkedPaths)
            return Files.walk(start);
        List<Path> paths = getFilePaths(type, resourceNamespace);
        if (paths == null) {
            try (Stream<Path> pathStream = Files.walk(start)) {
                paths = pathStream.collect(Collectors.toList());
            } finally {
                cacheFilePaths(type, resourceNamespace, paths == null ? Collections.emptyList() : paths);
            }
        }
        return paths.parallelStream();
    }

    @Redirect(method = "getResources", at = @At(value = "INVOKE", target = "Ljava/nio/file/FileSystem;getPath(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;"))
    public Path getResourcesGetPathRedirected(FileSystem instance, String s, String[] strings) {
        if (!GlobalCache.isEnabled)
            return instance.getPath(s);
        Path inputPath = inputPathByPath.get(s);
        if (inputPath == null) {
            inputPathByPath.put(s, inputPath = instance.getPath(s));
        }
        return inputPath;
    }

    @Override
    public void persistAndClearCache() {
        if (modFile != null) {
            CacheUtil.persist(getExistenceByResource(), new File(HAS_RESOURCE_CACHE_DIR.getPath(), id + ".ser"));
            CacheUtil.persist(namespacesByPackType, new File(NAMESPACE_CACHE_DIR.getPath(), id + ".ser"));
        }
        getExistenceByResource().clear();
        namespacesByPackType.clear();
        filePathsByRootByPackType.clear();
    }

    public Boolean exists(String resourceName) {
        return getExistenceByResource().get(resourceName);
    }

    public void cacheExists(String resourceName, boolean exists) {
        getExistenceByResource().put(resourceName, exists);
    }

    public void cacheFilePaths(ResourcePackType packType, String resourceNamespace, List<Path> filePaths) {
        getFilePathsMap(packType).putIfAbsent(resourceNamespace, filePaths);
    }

    public List<Path> getFilePaths(ResourcePackType packType, String resourceNamespace) {
        return getFilePathsMap(packType).get(resourceNamespace);
    }

    private Map<String, List<Path>> getFilePathsMap(ResourcePackType packType) {
        return filePathsByRootByPackType.get(packType);
    }

    public void cacheNamespaces(ResourcePackType packType, Set<String> namespaces) {
        namespacesByPackType.put(packType, namespaces);
    }

    public Set<String> getCachedNamespaces(ResourcePackType packType) {
        return namespacesByPackType.get(packType);
    }
}