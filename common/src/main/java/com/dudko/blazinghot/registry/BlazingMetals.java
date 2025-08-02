package com.dudko.blazinghot.registry;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class BlazingMetals {

	public static List<BlazingMetal> ALL = new ArrayList<>();

	// Vanilla
	public static BlazingMetal
			IRON =
			BlazingMetal.create("iron", b -> b.createForms().withForms(BlazingForms.ROD).waterCobble().build()),
			COPPER =
					BlazingMetal.create("copper",
							b -> b.createForms().withForms(BlazingForms.ROD, BlazingForms.WIRE).waterCobble().build()),
			GOLD =
					BlazingMetal.create("gold",
							b -> b.createForms().withForms(BlazingForms.ROD, BlazingForms.WIRE).waterCobble().build()),
			ANCIENT_DEBRIS =
					BlazingMetal.create("ancient_debris",
							b -> b
									.withForms(BlazingForms.ANCIENT_DEBRIS, BlazingForms.NETHERITE_SCRAP)
									.addFluidInteraction(Fluids.WATER,
											() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
									.build()),
			NETHERITE =
					BlazingMetal.create("netherite",
							b -> b
									.withForms(BlazingForms.NETHERITE_INGOT)
									.addFluidInteraction(Fluids.WATER,
											() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
									.build());

	// Create
	public static BlazingMetal
			BRASS =
			BlazingMetal.create("brass",
					b -> b.fromMods(Mods.CREATE).createForms().withForms(BlazingForms.ROD).waterCobble().build()),
			ANDESITE =
					BlazingMetal.create("andesite",
							b -> b.fromMods(Mods.CREATE).withForms(BlazingForms.ANDESITE_ALLOY).waterCobble().build());

	// Create: Blazing Hot
	public static BlazingMetal
			BLAZE_GOLD =
			BlazingMetal.create("blaze_gold",
					b -> b
							.fromMods(Mods.BLAZINGHOT)
							.createForms()
							.withForms(BlazingForms.BLAZE_GOLD_ROD)
							.addFluidInteraction(Fluids.WATER, () -> Blocks.NETHERRACK)
							.build());

}
