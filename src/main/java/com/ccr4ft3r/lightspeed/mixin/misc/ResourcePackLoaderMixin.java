package com.ccr4ft3r.lightspeed.mixin.misc;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.IPathResourcePack;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackLoader.class)
public abstract class ResourcePackLoaderMixin {

    @Inject(method = "createPackForMod", at = @At("HEAD"), remap = false, cancellable = true)
    private static void createPackForModHeadInjected(IModFileInfo mf, CallbackInfoReturnable<PathPackResources> cir) {
        if (!GlobalCache.isEnabled)
            return;
        PathPackResources resourcePack = new PathPackResources(mf.getFile().getFileName(), mf.getFile().getFilePath());
        ((IPathResourcePack) resourcePack).setModFile(mf.getFile());
        cir.setReturnValue(resourcePack);
    }
}