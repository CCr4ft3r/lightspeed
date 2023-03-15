package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPackResources;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin(FilePackResources.class)
public abstract class FilePackResourcesMixin implements IPackResources {

    private final Map<PackType, List<ZipEntry>> entriesByPackType = Maps.newConcurrentMap();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(File p_10236_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    @Inject(method = "getResources", at = @At(value = "INVOKE", target = "Ljava/util/zip/ZipFile;entries()Ljava/util/Enumeration;", shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void getResourcesHeadInjected(PackType packType, String pathIn, String pathIn2, int maxDepth, Predicate<String> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir, ZipFile zip) {
        if (!GlobalCache.isEnabled)
            return;
        String base = packType.getDirectory() + "/" + pathIn + "/";
        String path = base + pathIn2 + "/";
        List<ZipEntry> entries;
        if ((entries = entriesByPackType.get(packType)) == null) {
            entries = zip.stream().filter(e -> !e.isDirectory()).filter(
                e -> !e.getName().endsWith(".mcmeta")
            ).collect(Collectors.toList());
            entriesByPackType.put(packType, entries);
        }
        cir.setReturnValue(entries.stream().filter(e -> e.getName().startsWith(path)).map(e -> {
            String locPath = e.getName().substring(base.length());
            String[] splitted = locPath.split("/");
            if (splitted.length >= maxDepth + 1 && filter.test(splitted[splitted.length - 1])) {
                return new ResourceLocation(pathIn, locPath);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public void persistAndClearCache() {
        entriesByPackType.clear();
    }
}