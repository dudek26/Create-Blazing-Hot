package com.dudko.blazinghot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.registry.BlazingBlockEntityTypes;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingCreativeTabs;
import com.dudko.blazinghot.registry.BlazingEntities;
import com.dudko.blazinghot.registry.BlazingFluids;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;

public class BlazingHot {
	public static final String ID = "blazinghot";
	public static final String NAME = "Create: Blazing Hot";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(BlazingHot.ID);

	static {
		REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item,
				TooltipHelper.Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}

	public static void init() {
		LOGGER.info("Create mod addon {} initializing! Create version: {}", NAME, Create.VERSION);

		MoltenMetals.init();

		BlazingTags.register();
		CommonTags.register();
		BlazingCreativeTabs.register();
		BlazingBlocks.register();
		BlazingItems.register();
		BlazingFluids.register();
		BlazingEntities.register();
		BlazingBlockEntityTypes.register();
		BlazingRecipeTypes.register();

		finalizeRegistrate();
	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(ID, path);
	}

	public static CreateRegistrate registrate() {
		return REGISTRATE;
	}

	@ExpectPlatform
	public static void finalizeRegistrate() {
		throw new AssertionError();
	}
}
