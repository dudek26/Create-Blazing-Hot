package com.dudko.blazinghot.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.mixin_interfaces.IAirCurrent;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@Mixin(value = AirCurrent.class, remap = false)
public abstract class AirCurrentMixin implements IAirCurrent {

	@Unique
	protected List<Pair<SpoutCastingBehaviour, FanProcessingType>> blazinghot$affectedDepotHandlers = new ArrayList<>();

	@Shadow
	private int getLimit() {
		return 0;
	}

	@Shadow
	@Final
	public IAirCurrentSource source;

	@Unique
	public void blazinghot$findAffectedDepots() {
		AirCurrent self = (AirCurrent) (Object) this;

		Level world = self.source.getAirCurrentWorld();
		BlockPos start = self.source.getAirCurrentPos();
		blazinghot$clearCastingHandlers();
		int limit = getLimit();
		for (int i = 1; i <= limit; i++) {
			FanProcessingType segmentType = self.getTypeAt(i - 1);
			for (int offset : Iterate.zeroAndOne) {
				BlockPos pos = start.relative(self.direction, i).below(offset);
				SpoutCastingBehaviour behaviour = BlockEntityBehaviour.get(world, pos, SpoutCastingBehaviour.TYPE);
				if (behaviour != null) {
					FanProcessingType type = FanProcessingType.getAt(world, pos);
					if (type == null) type = segmentType;
					if (type == null) continue;

					behaviour.fanModifiers.add(type);
					blazinghot$affectedDepotHandlers.add(Pair.of(behaviour, type));
					
				}
				if (self.direction.getAxis().isVertical()) break;
			}
		}
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void blazinghot$tickDepots(CallbackInfo ci) {
		if (source.isSourceRemoved()) blazinghot$clearCastingHandlers();
	}

	@Unique
	public void blazinghot$clearCastingHandlers() {
		blazinghot$affectedDepotHandlers.forEach(pair -> pair.getFirst().fanModifiers.remove(pair.getSecond()));
		blazinghot$affectedDepotHandlers.clear();
	}

	@Inject(method = "rebuild", at = @At("RETURN"))
	private void blazinghot$rebuild(CallbackInfo ci) {
		blazinghot$findAffectedDepots();
	}

}
