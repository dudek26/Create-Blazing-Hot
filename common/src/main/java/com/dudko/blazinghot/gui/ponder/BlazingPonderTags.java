package com.dudko.blazinghot.gui.ponder;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.createmod.catnip.platform.CatnipServices;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class BlazingPonderTags {

	public static final ResourceLocation CASTING_RELATED = loc("casting_related");

	private static ResourceLocation loc(String id) {
		return BlazingHot.asResource(id);
	}

	public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
		PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

		PonderTagRegistrationHelper<ItemLike>
				itemHelper =
				helper.withKeyFunction(CatnipServices.REGISTRIES::getKeyOrThrow);

		helper
				.registerTag(CASTING_RELATED)
				.addToIndex()
				.item(BlazingBlocks.CASTING_DEPOT, true, true)
				.title("Casting Related")
				.description("Components used in casting")
				.register();

		HELPER.addToTag(CASTING_RELATED).add(BlazingBlocks.CASTING_DEPOT).add(AllBlocks.SPOUT);
	}

}
