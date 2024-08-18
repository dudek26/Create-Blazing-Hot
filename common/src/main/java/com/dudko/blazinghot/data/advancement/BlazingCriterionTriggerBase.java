package com.dudko.blazinghot.data.advancement;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.advancement.CriterionTriggerBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BlazingCriterionTriggerBase<T extends CriterionTriggerBase.Instance> extends CriterionTriggerBase<T> {

    private final ResourceLocation id;

    public BlazingCriterionTriggerBase(String id) {
        super(id);
        this.id = BlazingHot.asResource(id);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

}
