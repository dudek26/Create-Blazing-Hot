package com.dudko.blazinghot.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Mixin(value = AirCurrent.class, remap = false)
public abstract class AirCurrentMixin {

	@Unique
	protected List<Pair<CastingDepotBehaviour, FanProcessingType>> blazinghot$affectedDepotHandlers = new ArrayList<>();

	@Shadow
	private int getLimit() {
		return 0;
	}

	@Unique
	public void blazinghot$findAffectedDepots() {
		AirCurrent self = (AirCurrent) (Object) this;

		Level world = self.source.getAirCurrentWorld();
		BlockPos start = self.source.getAirCurrentPos();
		blazinghot$affectedDepotHandlers.forEach(pair ->
				pair.getFirst().coolingSpeedModifier -=
						CastingDepotBehaviour.COOLING_SPEEDS.get(pair.getSecond()));
		blazinghot$affectedDepotHandlers.clear();
		int limit = getLimit();
		for (int i = 1; i <= limit; i++) {
			FanProcessingType segmentType = self.getTypeAt(i - 1);
			for (int offset : Iterate.zeroAndOne) {
				BlockPos pos = start.relative(self.direction, i).below(offset);
				CastingDepotBehaviour behaviour = BlockEntityBehaviour.get(world, pos, CastingDepotBehaviour.TYPE);
				if (behaviour != null) {
					FanProcessingType type = FanProcessingType.getAt(world, pos);
					if (type == null) type = segmentType;
					if (type == null) continue;

					float speed = CastingDepotBehaviour.COOLING_SPEEDS.get(type);
					behaviour.coolingSpeedModifier += speed;

					blazinghot$affectedDepotHandlers.add(Pair.of(behaviour, type));
				}
				if (self.direction.getAxis().isVertical()) break;
			}
		}
	}

	@Inject(method = "rebuild",
			at = @At(value = "INVOKE",
					target = "Lcom/simibubi/create/content/kinetics/fan/AirCurrent;findAffectedHandlers()V"))
	private void blazinghot$rebuild(CallbackInfo ci) {
		blazinghot$findAffectedDepots();
	}

}
