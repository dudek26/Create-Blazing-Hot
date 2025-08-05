package com.dudko.blazinghot.gui.ponder;

import com.dudko.blazinghot.gui.ponder.scenes.CastingScenes;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

public class BlazingPonderScenes {

	public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

		HELPER
				.forComponents(BlazingBlocks.CASTING_DEPOT)
				.addStoryBoard("casting/spout", CastingScenes::castingBySpout, BlazingPonderTags.CASTING_RELATED)
				.addStoryBoard("casting/molds", CastingScenes::molds)
				.addStoryBoard("casting/air_current", CastingScenes::airCurrent)
				.addStoryBoard("casting/automating", CastingScenes::automating);
	}

	public static void setFluidInTank(SceneBuilder builder, BlockPos pos, Fluid fluid, long amount) {
		builder.addInstruction(scene -> {
			PonderLevel world = scene.getWorld();
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (!(blockEntity instanceof SmartBlockEntity smartBlockEntity)) return;
			if (smartBlockEntity instanceof FluidTankBlockEntity tank) {
				setFluidInTank(tank, fluid, amount);
				return;
			}
			SmartFluidTankBehaviour tank = smartBlockEntity.getBehaviour(SmartFluidTankBehaviour.TYPE);
			if (tank == null) return;
			setFluidInTank(tank, fluid, amount);
		});
	}

	@ExpectPlatform
	public static void setFluidInTank(SmartFluidTankBehaviour tank, Fluid fluid, long amount) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void setFluidInTank(FluidTankBlockEntity tank, Fluid fluid, long amount) {
		throw new AssertionError();
	}

}
