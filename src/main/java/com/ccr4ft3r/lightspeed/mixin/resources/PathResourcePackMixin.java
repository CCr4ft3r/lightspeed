package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.ccr4ft3r.lightspeed.interfaces.IPathResourcePack;
import com.ccr4ft3r.lightspeed.util.CacheUtil;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Mixin;
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

@Mixin(value = PathPackResources.class)
public abstract class PathResourcePackMixin implements IPathResourcePack, IPackResources {

    private final Map<String, Path> resolvedPathByResource = Maps.newConcurrentMap();
    private final Map<PackType, Set<String>> namespacesByPackType = Maps.newConcurrentMap();
    private final Map<String, Path> inputPathByPath = Maps.newConcurrentMap();
    private final Map<PackType, Map<String, List<Path>>> filePathsByRootByPackType = initPathsMap();
    private IModFile modFile;
    private String id;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(String packName, Path source, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    private static Map<PackType, Map<String, List<Path>>> initPathsMap() {
        Map<PackType, Map<String, List<Path>>> map = Maps.newConcurrentMap();
        for (PackType packType : PackType.values()) {
            map.put(packType, Maps.newConcurrentMap());
        }
        return map;
    }

    @Inject(method = "resolve", at = @At("HEAD"), cancellable = true, remap = false)
    public void resolveHeadInjected(String[] paths, CallbackInfoReturnable<Path> cir) {
        if (modFile == null) {
            if (!GlobalCache.isEnabled)
                return;
            Path resolved = getResolvedPath(paths);
            if (resolved != null)
                cir.setReturnValue(resolved);
            return;
        }
        if (!GlobalCache.isEnabled) {
            cir.setReturnValue(modFile.findResource(paths));
            return;
        }
        Path path = getResolvedPath(paths);
        if (path == null)
            resolvedPathByResource.put(Arrays.toString(paths), path = modFile.findResource(paths));

        cir.setReturnValue(path);
    }

    @Inject(method = "resolve", at = @At("RETURN"), remap = false)
    public void resolveReturnInjected(String[] paths, CallbackInfoReturnable<Path> cir) {
        if (!GlobalCache.isEnabled || modFile != null)
            return;
        resolvedPathByResource.put(Arrays.toString(paths), cir.getReturnValue());
    }

    @Inject(method = "getNamespaces", at = @At("HEAD"), cancellable = true)
    public void getNamespacesHeadInjected(PackType type, CallbackInfoReturnable<Set<String>> cir) {
        if (!GlobalCache.isEnabled)
            return;
        Set<String> namespaces = getCachedNamespaces(type);
        if (namespaces != null)
            cir.setReturnValue(namespaces);
    }

    @Inject(method = "getNamespaces", at = @At("RETURN"))
    public void getNamespacesReturnInjected(PackType type, CallbackInfoReturnable<Set<String>> cir) {
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
    public synchronized void getResourcesInjected(PackType type, String resourceNamespace, String pathIn, Predicate<ResourceLocation> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir, Path root) {
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
    public synchronized Stream<Path> getResourcesWalkInjected(Path start, FileVisitOption[] options, PackType type, String resourceNamespace) throws IOException {
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
        resolvedPathByResource.clear();
        namespacesByPackType.clear();
        filePathsByRootByPackType.clear();
    }

    @Override
    public void setModFile(IModFile modFile) {
        this.modFile = modFile;
        this.id = modFile.getModFileInfo().moduleName() + modFile.getModFileInfo().versionString()
            + "-" + FilenameUtils.getBaseName(modFile.getFilePath().toString()).replaceAll("[^a-zA-Z0-9.-]", "");
        setExistenceByResource(GlobalCache.PERSISTED_EXISTENCES_BY_MOD.computeIfAbsent(
            id, i -> Maps.newConcurrentMap()));
    }

    public Path getResolvedPath(String... paths) {
        return resolvedPathByResource.get(Arrays.toString(paths));
    }

    public Boolean exists(String resourceName) {
        return getExistenceByResource().get(resourceName);
    }

    public void cacheExists(String resourceName, boolean exists) {
        getExistenceByResource().put(resourceName, exists);
    }

    public void cacheFilePaths(PackType packType, String resourceNamespace, List<Path> filePaths) {
        getFilePathsMap(packType).putIfAbsent(resourceNamespace, filePaths);
    }

    public List<Path> getFilePaths(PackType packType, String resourceNamespace) {
        return getFilePathsMap(packType).get(resourceNamespace);
    }

    private Map<String, List<Path>> getFilePathsMap(PackType packType) {
        return filePathsByRootByPackType.get(packType);
    }

    public void cacheNamespaces(PackType packType, Set<String> namespaces) {
        namespacesByPackType.put(packType, namespaces);
    }

    public Set<String> getCachedNamespaces(PackType packType) {
        return namespacesByPackType.get(packType);
    }
}