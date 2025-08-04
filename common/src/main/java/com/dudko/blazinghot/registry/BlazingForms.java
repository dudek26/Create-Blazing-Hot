package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingForm.Flag;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;

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
							.withMold(Molds.INGOT)),
			NUGGET =
					BlazingForm.create("nugget",
							b -> b
									.withAmount(MultiAmount.NUGGET)
									.withTagFolder("nuggets")
									.withMeltingTime(30)
									.mechanicalMixerMeltable(true)
									.withMold(Molds.NUGGET)),
			SHEET =
					INGOT.createFrom("sheet", b -> b.withTagFolder("plates").withMold(Molds.SHEET));

	// Basic compat forms
	public static BlazingForm
			OPTIONAL_ROD =
			BlazingForm.create("rod",
					b -> b
							.withAmount(MultiAmount.ROD)
							.withTagFolder("rods")
							.withMeltingTime(125)
							.mechanicalMixerMeltable(true)
							.withMold(Molds.ROD)
							.optional(true)
							.setFlags(Flag.MELTING)),
			OPTIONAL_WIRE =
					OPTIONAL_ROD.createFrom("wire", b -> b.withTagFolder("wires")),
			OPTIONAL_SHEET =
					SHEET.withFlags(Flag.MELTING).asOptional(true);

	// Netherite
	public static BlazingForm
			ANCIENT_DEBRIS =
			BlazingForm.create("ancient_debris",
					b -> b.withAmount(MultiAmount.RAW_ORE).withMeltingTime(300).withCustomItem(Items.ANCIENT_DEBRIS)),
			NETHERITE_SCRAP =
					INGOT.createFrom("netherite_scrap",
							b -> b.withCustomItem(Items.NETHERITE_SCRAP).mechanicalMixerMeltable(false)),
			NETHERITE_INGOT =
					INGOT.createFrom("ingot", b -> b.mechanicalMixerMeltable(false));

	// Create
	public static BlazingForm
			ANDESITE_ALLOY =
			INGOT.createFrom("andesite_alloy", b -> b.withCustomItem(AllItems.ANDESITE_ALLOY.getId())),
			CREATE_NUGGET =
					NUGGET.withFlags(Flag.MELTING).asOptional(true),
			COPPER_NUGGET =
					NUGGET.withFlags(Flag.CASTING).fromMods(Mods.CREATE),
			GOLDEN_SHEET =
					SHEET.createFrom("golden_sheet", b -> b.withCustomItem(Create.asResource("golden_sheet"))),
			CREATE_SHEET =
					SHEET.fromMods(Mods.CREATE);

	// Create: Blazing Hot
	public static BlazingForm ROD = OPTIONAL_ROD.withFlags(Flag.MELTING, Flag.CASTING).asOptional(false),
			STURDY_ALLOY =
					INGOT.createFrom("sturdy_alloy",
							b -> b.withCustomItem(BlazingItems.STURDY_ALLOY.getId()).mechanicalMixerMeltable(false)),
			STURDY_SHEET =
					SHEET.createFrom("sturdy_sheet",
							b -> b.withCustomItem(AllItems.STURDY_SHEET.getId()).mechanicalMixerMeltable(false));

	// Compat forms
	public static BlazingForm COMPAT_ROD = ROD.withFlags(Flag.CASTING).fromMods(Mods.CREATE_ADDITIONS),
			COMPAT_WIRE =
					OPTIONAL_WIRE.asOptional(false).withFlags(Flag.CASTING).fromMods(Mods.CREATE_ADDITIONS),
			ZINC_SHEET =
					SHEET.withFlags(Flag.CASTING).fromMods(Mods.CREATE_ADDITIONS);
}
