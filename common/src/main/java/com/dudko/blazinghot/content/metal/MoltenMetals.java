package com.dudko.blazinghot.content.metal;

import static com.dudko.blazinghot.compat.Mods.BLAZINGHOT;
import static com.dudko.blazinghot.compat.Mods.CREATE;
import static com.dudko.blazinghot.compat.Mods.CREATE_ADDITIONS;
import static com.dudko.blazinghot.compat.Mods.VANILLA;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

/**
 * @see com.dudko.blazinghot.registry.BlazingMetals
 */
@Deprecated
public class MoltenMetals {
	public static final List<MoltenMetal> ALL = new ArrayList<>();

	public static MoltenMetal
			IRON =
			MoltenMetal.builder("iron").createForms().compatForm(Forms.WIRE, CREATE_ADDITIONS).register(),

	GOLD = MoltenMetal.builder("gold").createForms().compatForm(Forms.WIRE, CREATE_ADDITIONS).register(),

	COPPER = MoltenMetal.builder("copper").createForms().compatForm(Forms.WIRE, CREATE_ADDITIONS).register(),

	ANCIENT_DEBRIS =
			MoltenMetal
					.builder("ancient_debris")
					.customForm("netherite_scrap",
							VANILLA.asResource("netherite_scrap"),
							MultiAmount.INGOT,
							Forms.INGOT.processingTime * 2,
							Forms.INGOT.fuelCost * 2,
							false,
							Molds.INGOT,
							VANILLA)
					.customForm("ancient_debris",
							VANILLA.asResource("ancient_debris"),
							MultiAmount.RAW_ORE,
							Forms.INGOT.processingTime * 2,
							Forms.INGOT.fuelCost * 2,
							false,
							null,
							VANILLA)
					.addFluidInteraction(Fluids.WATER, () -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
					.disableMechanicalMixing()
					.ignoreTagDatagen()
					.register(),

	NETHERITE =
			MoltenMetal
					.builder("netherite")
					.coreForms(Forms.INGOT)
					.disableMechanicalMixing()
					.addFluidInteraction(Fluids.WATER, () -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
					.register(),

	BLAZE_GOLD =
			MoltenMetal
					.builder("blaze_gold")
					.mod(BLAZINGHOT)
					.basicForms()
					.coreForms(Forms.PLATE, Forms.ROD)
					.addFluidInteraction(Fluids.WATER, () -> Blocks.NETHERRACK)
					.register(),

	ZINC = MoltenMetal.builder("zinc").mod(CREATE).basicForms().optionalForm(Forms.PLATE, CREATE_ADDITIONS).register(),

	BRASS = MoltenMetal.builder("brass").mod(CREATE).createForms().register(),

	ANDESITE =
			MoltenMetal
					.builder("andesite")
					.mod(CREATE)
					.customForm("andesite_alloy",
							AllItems.ANDESITE_ALLOY.getId(),
							Forms.INGOT.amount,
							Forms.INGOT.processingTime,
							Forms.INGOT.fuelCost,
							true,
							Molds.INGOT,
							CREATE)
					.castingOverride(Forms.INGOT, AllItems.ANDESITE_ALLOY.getId(), MultiAmount.INGOT)
					.ignoreTagDatagen()
					.register();

	public static void init() {

	}
}
