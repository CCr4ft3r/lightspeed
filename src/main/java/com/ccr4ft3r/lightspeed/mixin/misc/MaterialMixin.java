package com.ccr4ft3r.lightspeed.mixin.misc;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Material.class)
public abstract class MaterialMixin {

    private Integer hash;

    @Inject(method = "hashCode", at = @At("HEAD"), cancellable = true)
    public void hashCodeHeadInjected(CallbackInfoReturnable<Integer> cir) {
        if (GlobalCache.isEnabled && hash != null)
            cir.setReturnValue(hash);
    }

    @Inject(method = "hashCode", at = @At("RETURN"))
    public void hashCodeReturnInjected(CallbackInfoReturnable<Integer> cir) {
        if (GlobalCache.isEnabled && hash == null)
            hash = cir.getReturnValue();
    }
}