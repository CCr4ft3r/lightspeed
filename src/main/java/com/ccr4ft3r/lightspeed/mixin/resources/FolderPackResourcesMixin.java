package com.ccr4ft3r.lightspeed.mixin.resources;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import net.minecraft.server.packs.FolderPackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;
import java.io.IOException;

import static com.ccr4ft3r.lightspeed.cache.GlobalCache.*;

@Mixin(FolderPackResources.class)
public abstract class FolderPackResourcesMixin {

    @Redirect(method = "validatePath", at = @At(value = "INVOKE", target = "Ljava/io/File;getCanonicalPath()Ljava/lang/String;"))
    private static String validatePathGetCanonicalPathRedirected(File instance) throws IOException {
        if (!GlobalCache.isEnabled)
            return instance.getCanonicalPath();
        String canoncialPath = CANONICAL_PATH_PER_FILE.get(instance.getPath());
        if (canoncialPath == null) {
            CANONICAL_PATH_PER_FILE.put(instance.getPath(), canoncialPath = instance.getCanonicalPath());
        }
        return canoncialPath;
    }
}