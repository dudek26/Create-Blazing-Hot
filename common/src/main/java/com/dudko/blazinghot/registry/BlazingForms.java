package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.metal.BlazingForm;
import com.dudko.blazinghot.content.metal.BlazingForm.Flag;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.util.NullableSupplier;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

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
				.optional(true)
				.setFlags(Flag.MELTING)),
		OPTIONAL_WIRE =
			OPTIONAL_ROD.createFrom("wire", b -> b.withTagFolder("wires")),
		OPTIONAL_SHEET =
			SHEET.withFlags(Flag.MELTING).asOptional(true),
		OPTIONAL_INGOT = INGOT.withFlags(Flag.MELTING).asOptional(true),
		OPTIONAL_NUGGET = NUGGET.withFlags(Flag.MELTING).asOptional(true);

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

	// Raw Ores
	public static BlazingForm RAW_IRON = rawOre(() -> Items.RAW_IRON), RAW_GOLD = rawOre(Items.RAW_GOLD),
		RAW_COPPER =
			rawOre(Items.RAW_COPPER), RAW_ZINC = rawOre("raw_zinc", () -> getItemKey(AllItems.RAW_ZINC));

	public static BlazingForm CRUSHED_RAW_IRON = rawOre("crushed_raw_iron", () -> getItemKey(AllItems.CRUSHED_IRON)),
		CRUSHED_RAW_GOLD =
			rawOre("crushed_raw_gold", () -> getItemKey(AllItems.CRUSHED_GOLD)),
		CRUSHED_RAW_COPPER =
			rawOre("crushed_raw_copper", () -> getItemKey(AllItems.CRUSHED_COPPER)),
		CRUSHED_RAW_ZINC =
			rawOre("crushed_raw_zinc", () -> getItemKey(AllItems.CRUSHED_ZINC));

	// Create
	public static BlazingForm
		ANDESITE_ALLOY =
		INGOT.createFrom("andesite_alloy", b -> b.withCustomItem(() -> getItemKey(AllItems.ANDESITE_ALLOY))),
		CREATE_NUGGET =
			NUGGET.withFlags(Flag.MELTING).asOptional(true),
		COPPER_NUGGET =
			NUGGET.withFlags(Flag.CASTING).fromMods(Mods.CREATE),
		GOLDEN_SHEET =
			SHEET.createFrom("golden_sheet", b -> b.withCustomItem(() -> Create.asResource("golden_sheet"))),
		CREATE_SHEET =
			SHEET.fromMods(Mods.CREATE);

	// Create: Blazing Hot
	public static BlazingForm ROD = OPTIONAL_ROD.withFlags(Flag.MELTING, Flag.CASTING).asOptional(false),
		STURDY_ALLOY =
			INGOT.createFrom("sturdy_alloy",
				b -> b.withCustomItem(BlazingItems.STURDY_ALLOY).mechanicalMixerMeltable(false)),
		STURDY_SHEET =
			SHEET.createFrom("sturdy_sheet",
				b -> b
					.withCustomItem(() -> getItemKey(AllItems.STURDY_SHEET))
					.mechanicalMixerMeltable(false)),
		STURDY_MOLD =
			INGOT.createFrom("sturdy_mold",
				b -> b.withAmount(MultiAmount.INGOT.multiply(2)).withMeltingTime(300));

	// Compat forms
	public static BlazingForm COMPAT_ROD =
		ROD.createFrom("rod", b -> b
			.setFlags(Flag.CASTING)
			.withMold(Molds.ROD)),
		COMPAT_NUGGET = NUGGET.createFrom("nugget", b -> b
			.setFlags(Flag.CASTING)
			.withMold(Molds.NUGGET)),
		COMPAT_INGOT = INGOT.createFrom("ingot", b -> b
			.setFlags(Flag.CASTING)
			.withMold(Molds.INGOT)),
		COMPAT_PLATE = SHEET.createFrom("plate", b -> b
			.setFlags(Flag.CASTING)
			.withMold(Molds.SHEET));
	// Create: Crafts & Additions
	public static BlazingForm
		COMPAT_CA_WIRE =
		OPTIONAL_WIRE.asOptional(false).withFlags(Flag.CASTING).fromMods(Mods.CREATE_ADDITIONS),
		ZINC_CA_SHEET =
			SHEET.withFlags(Flag.CASTING).fromMods(Mods.CREATE_ADDITIONS);


	private static ResourceLocation getItemKey(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem());
	}

	public static BlazingForm rawOre(ItemLike item, Mods... mods) {
		ResourceLocation key = getItemKey(item);
		return rawOre(key.getPath(), () -> key, mods);
	}

	public static BlazingForm rawOre(String name, NullableSupplier<ResourceLocation> rawOre, Mods... mods) {
		return BlazingForm.create(name,
			b -> b
				.withAmount(MultiAmount.RAW_ORE)
				.withCustomItem(rawOre)
				.withMeltingTime(250)
				.fromMods(mods)
				.setFlags(Flag.MELTING)
				.mechanicalMixerMeltable(false)
				.overrideFuelCost(MultiAmount.fromBucketFraction(1, 20)));
	}
}
