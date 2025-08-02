package com.dudko.blazinghot.registry;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class BlazingMetals {

	public static void init() {
	}

	public static List<BlazingMetal> ALL = new ArrayList<>();

	// Vanilla
	public static BlazingMetal
			IRON =
			BlazingMetal
					.create("iron",
							b -> b
									.createForms()
									.withForms(BlazingForms.OPTIONAL_ROD, BlazingForms.COMPAT_ROD)
									.waterCobble()
									.build())
					.register(),
			COPPER =
					BlazingMetal
							.create("copper",
									b -> b
											.createForms()
											.withForms(BlazingForms.OPTIONAL_ROD,
													BlazingForms.OPTIONAL_WIRE,
													BlazingForms.COMPAT_ROD,
													BlazingForms.COMPAT_WIRE)
											.waterCobble()
											.build())
							.register(),
			GOLD =
					BlazingMetal
							.create("gold",
									b -> b
											.createForms()
											.withForms(BlazingForms.OPTIONAL_ROD,
													BlazingForms.OPTIONAL_WIRE,
													BlazingForms.COMPAT_ROD,
													BlazingForms.COMPAT_WIRE)
											.waterCobble()
											.build())
							.register(),
			ANCIENT_DEBRIS =
					BlazingMetal
							.create("ancient_debris",
									b -> b
											.withForms(BlazingForms.ANCIENT_DEBRIS, BlazingForms.NETHERITE_SCRAP)
											.addFluidInteraction(Fluids.WATER,
													() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
											.build())
							.register(),
			NETHERITE =
					BlazingMetal
							.create("netherite",
									b -> b
											.withForms(BlazingForms.NETHERITE_INGOT)
											.addFluidInteraction(Fluids.WATER,
													() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
											.build())
							.register();

	// Create
	public static BlazingMetal
			BRASS =
			BlazingMetal
					.create("brass",
							b -> b
									.fromMods(Mods.CREATE)
									.createForms()
									.withForms(BlazingForms.OPTIONAL_ROD, BlazingForms.COMPAT_ROD)
									.waterCobble()
									.build())
					.register(),
			ANDESITE =
					BlazingMetal
							.create("andesite",
									b -> b
											.fromMods(Mods.CREATE)
											.withForms(BlazingForms.ANDESITE_ALLOY)
											.waterCobble()
											.build())
							.register(),
			ZINC =
					BlazingMetal
							.create("zinc",
									b -> b
											.fromMods(Mods.CREATE)
											.vanillaForms()
											.withForms(BlazingForms.OPTIONAL_SHEET, BlazingForms.ZINC_SHEET)
											.waterCobble()
											.build())
							.register();

	// Create: Blazing Hot
	public static BlazingMetal
			BLAZE_GOLD =
			BlazingMetal
					.create("blaze_gold",
							b -> b
									.fromMods(Mods.BLAZINGHOT)
									.createForms()
									.withForms(BlazingForms.ROD)
									.addFluidInteraction(Fluids.WATER, () -> Blocks.NETHERRACK)
									.build())
					.register();

}
