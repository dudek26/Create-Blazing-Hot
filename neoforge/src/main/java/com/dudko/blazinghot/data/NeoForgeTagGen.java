package com.dudko.blazinghot.data;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.neoforge.BlazingFluidsImpl;
import com.dudko.blazinghot.util.DyeUtil;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("UnusedReturnValue")
public class NeoForgeTagGen {

	public static void generateBlockTags(RegistrateTagsProvider<Block> prov) {
		for (BlazingTags.Blocks tag : BlazingTags.Blocks.values()) {
			if (tag.alwaysDatagen) {
				tagAppender(prov, tag);
			}
		}

		prov.addTag(BlazingTags.Blocks.STORAGE_BLOCKS.tag()).addTag(BlazingTags.Blocks.BLAZE_GOLD_BLOCKS.tag());

		tagAppender(prov, BlazingTags.Blocks.MODERN_LAMPS)
				.addTag(BlazingTags.Blocks.MODERN_LAMP_BLOCKS.tag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_PANELS.tag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_QUAD_PANELS.tag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_DOUBLE_PANELS.tag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_HALF_PANELS.tag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_SMALL_PANELS.tag());
	}

	public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov) {
		prov
				.addTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag())
				.add(fluidKey(Fluids.LAVA))
				.add(fluidKey(BlazingFluidsImpl.NETHER_LAVA.getSource()));

		for (BlazingTags.Fluids tag : BlazingTags.Fluids.values()) {
			if (tag.alwaysDatagen) {
				tagAppender(prov, tag);
			}
		}

		for (BlazingMetal metal : BlazingMetals.ALL) {
			TagKey<Fluid> tag = BlazingTags.fluidTag(BlazingTags.Namespace.COMMON.asResource(metal.getMoltenName()));
			tagAppender(prov, tag);
		}

	}

	public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
		prov
				.addTag(BlazingTags.Items.NETHER_FLORA.tag())
				.add(itemKey(Items.WARPED_FUNGUS),
						itemKey(Items.CRIMSON_FUNGUS),
						itemKey(Items.WARPED_ROOTS),
						itemKey(Items.CRIMSON_ROOTS),
						itemKey(Items.WEEPING_VINES),
						itemKey(Items.TWISTING_VINES),
						itemKey(Items.NETHER_SPROUTS));

		prov.addTag(BlazingTags.Items.METAL_CARROTS.tag()).add(itemKey(Items.GOLDEN_CARROT));
		prov.addTag(BlazingTags.Items.METAL_APPLES.tag()).add(itemKey(Items.GOLDEN_APPLE));
		prov.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag()).add(itemKey(Items.ENCHANTED_GOLDEN_APPLE));

		tagAppender(prov, BlazingTags.Items.METAL_FOODS.tag())
				.addTag(BlazingTags.Items.METAL_CARROTS.tag())
				.addTag(BlazingTags.Items.METAL_APPLES.tag())
				.addTag(BlazingTags.Items.STELLAR_METAL_APPLES.tag())
				.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag());

		for (BlazingTags.Items tag : BlazingTags.Items.values()) {
			if (tag.alwaysDatagen) tagAppender(prov, tag);
		}

		for (BlazingTags.Blocks tag : BlazingTags.Blocks.values()) {
			if (tag.alwaysDatagen && tag.item) blockItemTagAppender(prov, tag);
		}

		for (DyeUtil.Dyes dye : DyeUtil.Dyes.values()) {
			tagAppender(prov, dye.tag);
		}

		blockItemTagAppender(prov,
				BlazingTags.Blocks.STORAGE_BLOCKS).addTag(BlazingTags.Blocks.BLAZE_GOLD_BLOCKS.itemTag());

		blockItemTagAppender(prov, BlazingTags.Blocks.MODERN_LAMPS)
				.addTag(BlazingTags.Blocks.MODERN_LAMP_BLOCKS.itemTag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_PANELS.itemTag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_QUAD_PANELS.itemTag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_DOUBLE_PANELS.itemTag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_HALF_PANELS.itemTag())
				.addTag(BlazingTags.Blocks.MODERN_LAMP_SMALL_PANELS.itemTag());
	}

	private static ResourceKey<Block> blockKey(Block block) {
		return BuiltInRegistries.BLOCK.wrapAsHolder(block).getKey();
	}

	private static ResourceKey<Item> itemKey(Item item) {
		return BuiltInRegistries.ITEM.wrapAsHolder(item).getKey();
	}

	private static ResourceKey<Fluid> fluidKey(Fluid fluid) {
		return BuiltInRegistries.FLUID.wrapAsHolder(fluid).getKey();
	}

	public static TagsProvider.TagAppender<Item> tagAppender(RegistrateTagsProvider<Item> prov, BlazingTags.Items tag) {
		return tagAppender(prov, tag.tag());
	}

	public static TagsProvider.TagAppender<Item> blockItemTagAppender(RegistrateTagsProvider<Item> prov, BlazingTags.Blocks tag) {
		return tagAppender(prov, tag.itemTag());
	}

	public static TagsProvider.TagAppender<Block> tagAppender(RegistrateTagsProvider<Block> prov, BlazingTags.Blocks tag) {
		return tagAppender(prov, tag.tag());
	}

	public static TagsProvider.TagAppender<Fluid> tagAppender(RegistrateTagsProvider<Fluid> prov, BlazingTags.Fluids tag) {
		return tagAppender(prov, tag.tag());
	}

	public static <T> TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
		return prov.addTag(tag);
	}
}
