package com.ccr4ft3r.lightspeed.mixin.misc;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.ICache;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(StateDefinition.class)
public class StateDefinitonMixin implements ICache {

    private Map<String, Property<?>> propsByName;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(Function p_61052_, Object p_61053_, StateDefinition.Factory p_61054_, Map p_61055_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initHeadInjected(Function p_61052_, Object p_61053_, StateDefinition.Factory p_61054_, Map p_61055_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            propsByName = p_61055_;
    }

    @Inject(method = "getProperty", at = @At("HEAD"), cancellable = true)
    public void getPropertyHeadInjected(String p_61082_, CallbackInfoReturnable<Property<?>> cir) {
        if (GlobalCache.isEnabled)
            cir.setReturnValue(propsByName.get(p_61082_));
    }

    @Override
    public void persistAndClearCache() {
        propsByName.clear();
    }
}