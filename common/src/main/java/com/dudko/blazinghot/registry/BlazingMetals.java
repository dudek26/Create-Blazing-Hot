package com.dudko.blazinghot.registry;

import static com.dudko.blazinghot.util.LangUtil.titleCaseConversion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
					.withForms(BlazingForms.OPTIONAL_ROD,
						BlazingForms.OPTIONAL_WIRE,
						BlazingForms.COMPAT_ROD.fromMods(Mods.CREATE_ADDITIONS, Mods.IMMERSIVE_ENGINEERING),
						BlazingForms.COMPAT_CA_WIRE,
						BlazingForms.RAW_IRON,
						BlazingForms.CRUSHED_RAW_IRON)
					.crystalMixtureInteraction(() -> AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get())
					.waterCobble())
			.register(),
		COPPER =
			BlazingMetal
				.create("copper",
					b -> b
						.withForms(BlazingForms.OPTIONAL_ROD,
							BlazingForms.OPTIONAL_WIRE,
							BlazingForms.COMPAT_ROD.fromMods(Mods.CREATE_ADDITIONS),
							BlazingForms.COMPAT_CA_WIRE,
							BlazingForms.INGOT,
							BlazingForms.CREATE_SHEET,
							BlazingForms.CREATE_NUGGET,
							BlazingForms.COPPER_NUGGET,
							BlazingForms.RAW_COPPER,
							BlazingForms.CRUSHED_RAW_COPPER)
						.crystalMixtureInteraction(() -> AllPaletteStoneTypes.VERIDIUM
							.getBaseBlock()
							.get())
						.waterCobble())
				.register(),
		GOLD =
			BlazingMetal
				.create("gold",
					b -> b
						.vanillaForms()
						.withForms(BlazingForms.OPTIONAL_ROD,
							BlazingForms.OPTIONAL_WIRE,
							BlazingForms.COMPAT_ROD.fromMods(Mods.CREATE_ADDITIONS),
							BlazingForms.COMPAT_CA_WIRE,
							BlazingForms.OPTIONAL_SHEET,
							BlazingForms.GOLDEN_SHEET,
							BlazingForms.RAW_GOLD,
							BlazingForms.CRUSHED_RAW_GOLD)
						.crystalMixtureInteraction(() -> AllPaletteStoneTypes.OCHRUM
							.getBaseBlock()
							.get())
						.waterCobble())
				.register(),
		ANCIENT_DEBRIS =
			BlazingMetal
				.create("ancient_debris",
					b -> b
						.withForms(BlazingForms.ANCIENT_DEBRIS, BlazingForms.NETHERITE_SCRAP)
						.addFluidInteraction(() -> Fluids.WATER,
							() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get()))
				.register(),
		NETHERITE =
			BlazingMetal
				.create("netherite",
					b -> b
						.withForms(BlazingForms.NETHERITE_INGOT)
						.addFluidInteraction(() -> Fluids.WATER,
							() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
						.crystalMixtureInteraction(() -> AllPaletteStoneTypes.OCHRUM
							.getBaseBlock()
							.get()))
				.register();

	// Create
	public static BlazingMetal
		BRASS =
		BlazingMetal
			.create("brass",
				b -> b
					.fromMods(Mods.CREATE)
					.createForms()
					.withForms(BlazingForms.OPTIONAL_ROD, BlazingForms.COMPAT_ROD.fromMods(Mods.CREATE_ADDITIONS))
					.crystalMixtureInteraction(() -> AllPaletteStoneTypes.ASURINE.getBaseBlock().get(),
						0.5)
					.crystalMixtureInteraction(() -> AllPaletteStoneTypes.VERIDIUM.getBaseBlock().get(),
						0.5)
					.waterCobble())
			.register(),
		ANDESITE =
			BlazingMetal
				.create("andesite",
					b -> b
						.fromMods(Mods.CREATE)
						.withForms(BlazingForms.ANDESITE_ALLOY)
						.crystalMixtureInteraction(() -> Blocks.ANDESITE)
						.waterCobble())
				.register(),
		ZINC =
			BlazingMetal
				.create("zinc",
					b -> b
						.fromMods(Mods.CREATE)
						.vanillaForms()
						.withForms(BlazingForms.OPTIONAL_SHEET,
							BlazingForms.ZINC_CA_SHEET,
							BlazingForms.RAW_ZINC,
							BlazingForms.CRUSHED_RAW_ZINC)
						.crystalMixtureInteraction(() -> AllPaletteStoneTypes.ASURINE
							.getBaseBlock()
							.get())
						.waterCobble())
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
					.crystalMixtureInteraction(() -> AllPaletteStoneTypes.OCHRUM.getBaseBlock().get())
					.addFluidInteraction(() -> Fluids.WATER, () -> Blocks.NETHERRACK))
			.register(),
		STURDY_ALLOY =
			BlazingMetal
				.create("sturdy_alloy",
					b -> b
						.fromMods(Mods.BLAZINGHOT)
						.withForms(BlazingForms.STURDY_ALLOY, BlazingForms.STURDY_SHEET)
						.crystalMixtureInteraction(() -> Blocks.OBSIDIAN, 0.5)
						.crystalMixtureInteraction(() -> Blocks.CRYING_OBSIDIAN, 0.5)
						.addFluidInteraction(() -> Fluids.WATER, () -> Blocks.COBBLED_DEEPSLATE))
				.register();

	// Compat
	public static BlazingMetal STEEL =
		BlazingMetal.create("steel", b -> b
				.fromMods(Mods.MEKANISM, Mods.IMMERSIVE_ENGINEERING, Mods.CREATE_TFMG, Mods.CREATE_BIG_CANNONS)
				.basicCompatForms(Mods.MEKANISM, Mods.IMMERSIVE_ENGINEERING, Mods.CREATE_TFMG, Mods.CREATE_BIG_CANNONS)
				.withForms(BlazingForms.COMPAT_ROD.fromMods(Mods.IMMERSIVE_ENGINEERING),
					BlazingForms.COMPAT_NUGGET.fromMods(Mods.MEKANISM, Mods.IMMERSIVE_ENGINEERING, Mods.CREATE_TFMG),
					BlazingForms.COMPAT_PLATE.fromMods(Mods.IMMERSIVE_ENGINEERING)))
			.register();

	/**
	 * @see BlazingAdvancements#ALL_MOLTEN_METALS
	 */
	public static Set<Item> allBuckets(boolean includeCompat) {
		return ALL
			.stream()
			.filter(m -> m.mods.getFirst().alwaysIncluded || includeCompat)
			.map(BlazingMetal::getBucket)
			.collect(Collectors.toSet());
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {

		for (BlazingMetal metal : ALL) {
			ResourceLocation fluidLoc = metal.getFluidTag().location();
			consumer.accept("tag.fluid." + BlazingTags.Namespace.COMMON.id + "." + fluidLoc.getPath().replace('/', '.'),
				titleCaseConversion(metal.getFluidLocation().getPath().replace('_', ' ')));
		}

	}
}
