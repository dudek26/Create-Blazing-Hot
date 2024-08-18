package com.dudko.blazinghot.mixin_interfaces;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import java.util.List;

public interface ISmartBlockEntity {
    void blazinghot$registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements);

    void blazinghot$award(BlazingAdvancement advancement);

    void blazinghot$awardIfNear(BlazingAdvancement advancement, int range);
}
