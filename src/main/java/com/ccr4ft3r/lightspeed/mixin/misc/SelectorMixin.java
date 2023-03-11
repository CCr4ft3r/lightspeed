package com.ccr4ft3r.lightspeed.mixin.misc;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.ccr4ft3r.lightspeed.interfaces.ICache;
import com.google.common.collect.MapMaker;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(Selector.class)
public class SelectorMixin implements ICache {

    private final Map<StateDefinition<Block, BlockState>, Predicate<BlockState>> predicatePerDefinition = new MapMaker().weakKeys().makeMap();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initReturnInjected(Condition p_112018_, MultiVariant p_112019_, CallbackInfo ci) {
        if (GlobalCache.isEnabled)
            GlobalCache.add(this);
    }

    @Inject(method = "getPredicate", at = @At("HEAD"), cancellable = true)
    public void getPredicateHeadInjected(StateDefinition<Block, BlockState> p_112022_, CallbackInfoReturnable<Predicate<BlockState>> cir) {
        if (!GlobalCache.isEnabled)
            return;
        Predicate<BlockState> predicate = predicatePerDefinition.get(p_112022_);
        if (predicate != null)
            cir.setReturnValue(predicate);
    }

    @Inject(method = "getPredicate", at = @At("RETURN"))
    public void getPredicateReturnInjected(StateDefinition<Block, BlockState> p_112022_, CallbackInfoReturnable<Predicate<BlockState>> cir) {
        if (GlobalCache.isEnabled)
            predicatePerDefinition.put(p_112022_, cir.getReturnValue());
    }

    @Override
    public void persistAndClearCache() {
        predicatePerDefinition.clear();
    }
}