package com.ccr4ft3r.lightspeed.mixin.misc;

import net.minecraft.client.renderer.model.WeightedBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(WeightedBakedModel.Builder.class)
public class WeightedBakedModelBuilderMixin {

    @Redirect(method = "add", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public synchronized <E> boolean addAddRedirected(List<E> instance, E e) {
        return instance.add(e);
    }
}