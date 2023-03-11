package com.ccr4ft3r.lightspeed.mixin.misc;

import com.ccr4ft3r.lightspeed.cache.GlobalCache;
import com.google.common.base.Splitter;
import net.minecraft.client.renderer.block.model.multipart.KeyValueCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.ccr4ft3r.lightspeed.cache.GlobalCache.*;

@Mixin(value = KeyValueCondition.class, priority = Integer.MAX_VALUE)
public abstract class KeyValueConditionMixin {

    @Redirect(method = "getPredicate", at = @At(value = "INVOKE", target = "Lcom/google/common/base/Splitter;splitToList(Ljava/lang/CharSequence;)Ljava/util/List;"))
    public List<String> getPredicateSplitToListRedirected(Splitter instance, CharSequence sequence) {
        if (!GlobalCache.isEnabled)
            return instance.splitToList(sequence);
        return SPLITTED_STRINGS_BY_SEQUENCE.computeIfAbsent(sequence, (k) -> instance.splitToList(sequence));
    }
}