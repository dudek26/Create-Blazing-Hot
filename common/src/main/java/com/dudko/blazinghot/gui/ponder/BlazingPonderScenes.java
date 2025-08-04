package com.dudko.blazinghot.gui.ponder;

import com.dudko.blazinghot.gui.ponder.scenes.CastingScenes;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public class BlazingPonderScenes {

	public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

		HELPER
				.forComponents(BlazingBlocks.CASTING_DEPOT)
				.addStoryBoard("casting/spout", CastingScenes::castingBySpout, BlazingPonderTags.CASTING_RELATED);
	}

	@ExpectPlatform
	public static void setTankFluid(Fluid fluid, long amount) {
		throw new AssertionError();
	}

}
