package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.simibubi.create.AllItems;

import net.minecraft.world.item.Items;

public class BlazingForms {

	// Basic forms
	public static BlazingForm
			INGOT =
			BlazingForm.create("ingot",
					b -> b
							.withAmount(MultiAmount.INGOT)
							.withTagFolder("ingots")
							.withMeltingTime(200)
							.mechanicalMixerMeltable(true)
							.withMold(Molds.INGOT)
							.build()),
			NUGGET =
					BlazingForm.create("nugget",
							b -> b
									.withAmount(MultiAmount.NUGGET)
									.withTagFolder("nuggets")
									.withMeltingTime(30)
									.mechanicalMixerMeltable(true)
									.withMold(Molds.INGOT)
									.build()),
			SHEET =
					INGOT.createFrom("sheet", b -> b.withTagFolder("plates").withMold(Molds.SHEET).build());

	// Basic compat forms
	public static BlazingForm
			ROD =
			BlazingForm.create("rod",
					b -> b
							.withAmount(MultiAmount.ROD)
							.withTagFolder("rods")
							.withMeltingTime(125)
							.mechanicalMixerMeltable(true)
							.withMold(Molds.ROD)
							.optional(true)
							.build()),
			WIRE =
					ROD.createFrom("wire", b -> b.withTagFolder("wires").withMold(null).build()),
			ZINC_SHEET =
					SHEET.asOptional(true);

	// Netherite
	public static BlazingForm
			ANCIENT_DEBRIS =
			BlazingForm.create("ancient_debris",
					b -> b
							.withAmount(MultiAmount.RAW_ORE)
							.withMeltingTime(300)
							.withCustomItem(Items.ANCIENT_DEBRIS)
							.build()),
			NETHERITE_SCRAP =
					INGOT.createFrom("netherite_scrap",
							b -> b.withCustomItem(Items.NETHERITE_SCRAP).mechanicalMixerMeltable(false).build()),
			NETHERITE_INGOT =
					INGOT.createFrom("ingot", b -> b.mechanicalMixerMeltable(false).build());

	public static BlazingForm BLAZE_GOLD_ROD = ROD.asOptional(false);
	public static BlazingForm
			ANDESITE_ALLOY =
			INGOT.createFrom("andesite_alloy", b -> b.withCustomItem(AllItems.ANDESITE_ALLOY.getId()).build());
}
