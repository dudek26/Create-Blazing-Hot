package com.dudko.blazinghot.content.casting.casting_depot.fabric;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;

import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpoutCastingBehaviourImpl extends SpoutCastingBehaviour {

	public SpoutCastingBehaviourImpl(CastingDepotBlockEntity depot) {
		super(depot);
	}

	public static SpoutCastingBehaviour of(CastingDepotBlockEntity depot) {
		return new SpoutCastingBehaviourImpl(depot);
	}

	@Override
	public int getRecipeCoolingDuration() {
		return 0;
	}

	@Override
	public int getRecipeProcessingDuration() {
		return 0;
	}

}
