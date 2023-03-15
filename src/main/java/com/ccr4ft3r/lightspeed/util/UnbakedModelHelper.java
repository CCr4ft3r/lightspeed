package com.ccr4ft3r.lightspeed.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.client.renderer.model.multipart.Multipart;
import net.minecraft.util.ResourceLocation;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class UnbakedModelHelper {

    public static Stream<RenderMaterial> getMaterialsStream(Multipart multiPart, Function<ResourceLocation, IUnbakedModel> p_111976_, Set<Pair<String, String>> p_111977_) {
        return multiPart.getSelectors().stream().flatMap((p_111981_) -> getMaterialsStream(p_111981_.getVariant(), p_111976_, p_111977_));
    }

    public static Stream<RenderMaterial> getMaterialsStream(VariantList multiVariant, Function<ResourceLocation, IUnbakedModel> p_111855_, Set<Pair<String, String>> p_111856_) {
        return multiVariant.getVariants().stream().map(Variant::getModelLocation).distinct().flatMap((p_111860_) -> {
            IUnbakedModel unbakedModel = p_111855_.apply(p_111860_);
            if (unbakedModel instanceof Multipart)
                return getMaterialsStream((Multipart) unbakedModel, p_111855_, p_111856_);
            if (unbakedModel instanceof VariantList)
                return getMaterialsStream((VariantList) unbakedModel, p_111855_, p_111856_);
            return unbakedModel.getMaterials(p_111855_, p_111856_).stream();
        });
    }
}