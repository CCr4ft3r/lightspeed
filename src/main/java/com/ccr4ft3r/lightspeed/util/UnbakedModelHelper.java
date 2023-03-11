package com.ccr4ft3r.lightspeed.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class UnbakedModelHelper {

    public static Stream<Material> getMaterialsStream(MultiPart multiPart, Function<ResourceLocation, UnbakedModel> p_111976_, Set<Pair<String, String>> p_111977_) {
        return multiPart.getSelectors().stream().flatMap((p_111981_) -> getMaterialsStream(p_111981_.getVariant(), p_111976_, p_111977_));
    }

    public static Stream<Material> getMaterialsStream(MultiVariant multiVariant, Function<ResourceLocation, UnbakedModel> p_111855_, Set<Pair<String, String>> p_111856_) {
        return multiVariant.getVariants().stream().map(Variant::getModelLocation).distinct().flatMap((p_111860_) -> {
            UnbakedModel unbakedModel = p_111855_.apply(p_111860_);
            if (unbakedModel instanceof MultiPart multiPart)
                return getMaterialsStream(multiPart, p_111855_, p_111856_);
            if (unbakedModel instanceof MultiVariant multiVariant1)
                return getMaterialsStream(multiVariant1, p_111855_, p_111856_);
            return unbakedModel.getMaterials(p_111855_, p_111856_).stream();
        });
    }
}