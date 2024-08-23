package com.dudko.blazinghot.mixin_interfaces;

import java.util.List;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

public interface IAdvancementBehaviour {

	void blazinghot$registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements);

	void blazinghot$award(BlazingAdvancement advancement);

	void blazinghot$awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance);

}
