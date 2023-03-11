package com.ccr4ft3r.lightspeed.util;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.util.random.WeightedEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;

public class WeightedBuilder {

    private final Set<WeightedEntry.Wrapper<BakedModel>> list = Sets.newConcurrentHashSet();

    public void add(@Nullable BakedModel p_119560_, int p_119561_) {
        if (p_119560_ != null)
            list.add(WeightedEntry.wrap(p_119560_, p_119561_));
    }

    @Nullable
    public BakedModel build() {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.size() == 1 ? list.iterator().next().getData() : new WeightedBakedModel(new ArrayList<>(list));
        }
    }
}