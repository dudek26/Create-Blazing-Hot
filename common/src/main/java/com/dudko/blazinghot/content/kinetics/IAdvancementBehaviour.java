package com.dudko.blazinghot.content.kinetics;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import java.util.List;

public interface IAdvancementBehaviour {

    void registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements);

    void award(BlazingAdvancement advancement);

    void awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance);

}
