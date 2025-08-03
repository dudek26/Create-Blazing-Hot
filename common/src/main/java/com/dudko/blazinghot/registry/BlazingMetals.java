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
import net.minecraft.world.level.ItemLike;
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
											BlazingForms.COMPAT_ROD,
											BlazingForms.COMPAT_WIRE)
									.waterCobble()
									.build())
					.register(),
			COPPER =
					BlazingMetal
							.create("copper",
									b -> b
											.withForms(BlazingForms.OPTIONAL_ROD,
													BlazingForms.OPTIONAL_WIRE,
													BlazingForms.COMPAT_ROD,
													BlazingForms.COMPAT_WIRE,
													BlazingForms.INGOT,
													BlazingForms.CREATE_SHEET,
													BlazingForms.CREATE_NUGGET,
													BlazingForms.COPPER_NUGGET)
											.waterCobble()
											.build())
							.register(),
			GOLD =
					BlazingMetal
							.create("gold",
									b -> b
											.vanillaForms()
											.withForms(BlazingForms.OPTIONAL_ROD,
													BlazingForms.OPTIONAL_WIRE,
													BlazingForms.COMPAT_ROD,
													BlazingForms.COMPAT_WIRE,
													BlazingForms.OPTIONAL_SHEET,
													BlazingForms.GOLDEN_SHEET)
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
					.register(),
			STURDY_ALLOY =
					BlazingMetal
							.create("sturdy_alloy",
									b -> b
											.fromMods(Mods.BLAZINGHOT)
											.withForms(BlazingForms.STURDY_ALLOY, BlazingForms.STURDY_SHEET)
											.addFluidInteraction(Fluids.WATER,
													() -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
											.build())
							.register();

	/**
	 * @see BlazingAdvancements#ALL_MOLTEN_METALS
	 */
	public static Set<ItemLike> allBuckets(boolean includeCompat) {
		return ALL
				.stream()
				.filter(m -> m.mods.get(0).alwaysIncluded || includeCompat)
				.map(m -> m.getBucket().get())
				.collect(Collectors.toSet());
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {

		for (BlazingMetal metal : ALL) {
			for (CommonTags.Namespace namespace : CommonTags.Namespace.values()) {
				ResourceLocation fluidLoc = metal.getFluidTag().location();
				consumer.accept("tag.fluid." + namespace.namespace + "." + fluidLoc.getPath().replace('/', '.'),
						titleCaseConversion(metal.getFluidLocation().getPath().replace('_', ' ')));

//				for (BlazingForm form : metal.forms) {
//					if (form.customLocation != null) continue;
//					TagKey<Item> tag = itemTagOf(form.tagFolder, metal.name, namespace);
//					ResourceLocation loc = tag.location();
//					consumer.accept("tag.item." + namespace.namespace + "." + loc.getPath().replace('/', '.'),
//							titleCaseConversion((metal.name + ' ' + form.tagFolder).replace('_', ' ')));
//				}
			}
		}

	}
}
