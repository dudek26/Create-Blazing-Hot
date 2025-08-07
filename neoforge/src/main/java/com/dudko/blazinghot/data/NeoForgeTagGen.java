package com.dudko.blazinghot.data;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingTagsV2;
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
		for (BlazingTagsV2.Blocks tag : BlazingTagsV2.Blocks.values()) {
			if (tag.alwaysDatagen) {
				tagAppender(prov, tag);
			}
		}

		prov.addTag(BlazingTagsV2.Blocks.STORAGE_BLOCKS.tag()).addTag(BlazingTagsV2.Blocks.BLAZE_GOLD_BLOCKS.tag());

		tagAppender(prov, BlazingTagsV2.Blocks.MODERN_LAMPS)
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_BLOCKS.tag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_PANELS.tag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_QUAD_PANELS.tag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_DOUBLE_PANELS.tag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_HALF_PANELS.tag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_SMALL_PANELS.tag());
	}

	public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov) {
		prov
				.addTag(BlazingTagsV2.Fluids.BLAZE_MIXER_FUEL.tag())
				.add(fluidKey(Fluids.LAVA))
				.add(fluidKey(BlazingFluidsImpl.NETHER_LAVA.getSource()));

		for (BlazingTagsV2.Fluids tag : BlazingTagsV2.Fluids.values()) {
			if (tag.alwaysDatagen) {
				tagAppender(prov, tag);
			}
		}

		for (BlazingMetal metal : BlazingMetals.ALL) {
			TagKey<Fluid>
					tag =
					BlazingTagsV2.fluidTag(BlazingTagsV2.Namespace.COMMON.asResource(metal.getMoltenName()));
			tagAppender(prov, tag);
		}

	}

	public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
		prov
				.addTag(BlazingTagsV2.Items.NETHER_FLORA.tag())
				.add(itemKey(Items.WARPED_FUNGUS),
						itemKey(Items.CRIMSON_FUNGUS),
						itemKey(Items.WARPED_ROOTS),
						itemKey(Items.CRIMSON_ROOTS),
						itemKey(Items.WEEPING_VINES),
						itemKey(Items.TWISTING_VINES),
						itemKey(Items.NETHER_SPROUTS));

		prov.addTag(BlazingTagsV2.Items.METAL_CARROTS.tag()).add(itemKey(Items.GOLDEN_CARROT));
		prov.addTag(BlazingTagsV2.Items.METAL_APPLES.tag()).add(itemKey(Items.GOLDEN_APPLE));
		prov.addTag(BlazingTagsV2.Items.ENCHANTED_METAL_APPLES.tag()).add(itemKey(Items.ENCHANTED_GOLDEN_APPLE));

		tagAppender(prov, BlazingTagsV2.Items.METAL_FOODS.tag())
				.addTag(BlazingTagsV2.Items.METAL_CARROTS.tag())
				.addTag(BlazingTagsV2.Items.METAL_APPLES.tag())
				.addTag(BlazingTagsV2.Items.STELLAR_METAL_APPLES.tag())
				.addTag(BlazingTagsV2.Items.ENCHANTED_METAL_APPLES.tag());

		for (BlazingTagsV2.Items tag : BlazingTagsV2.Items.values()) {
			if (tag.alwaysDatagen) tagAppender(prov, tag);
		}

		for (BlazingTagsV2.Blocks tag : BlazingTagsV2.Blocks.values()) {
			if (tag.alwaysDatagen && tag.item) blockItemTagAppender(prov, tag);
		}

		for (DyeUtil.Dyes dye : DyeUtil.Dyes.values()) {
			tagAppender(prov, dye.tag);
		}

		blockItemTagAppender(prov,
				BlazingTagsV2.Blocks.STORAGE_BLOCKS).addTag(BlazingTagsV2.Blocks.BLAZE_GOLD_BLOCKS.itemTag());

		blockItemTagAppender(prov, BlazingTagsV2.Blocks.MODERN_LAMPS)
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_BLOCKS.itemTag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_PANELS.itemTag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_QUAD_PANELS.itemTag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_DOUBLE_PANELS.itemTag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_HALF_PANELS.itemTag())
				.addTag(BlazingTagsV2.Blocks.MODERN_LAMP_SMALL_PANELS.itemTag());
	}

	private static ResourceKey<Block> blockKey(Block block) {
		return BuiltInRegistries.BLOCK
				.getResourceKey(block)
				.orElseThrow(() -> new NullPointerException("Couldn't get block's ResourceKey."));
	}

	private static ResourceKey<Item> itemKey(Item item) {
		return BuiltInRegistries.ITEM
				.getResourceKey(item)
				.orElseThrow(() -> new NullPointerException("Couldn't get item's ResourceKey."));
	}

	private static ResourceKey<Fluid> fluidKey(Fluid fluid) {
		return BuiltInRegistries.FLUID
				.getResourceKey(fluid)
				.orElseThrow(() -> new NullPointerException("Couldn't get fluid's ResourceKey."));
	}

	public static TagsProvider.TagAppender<Item> tagAppender(RegistrateTagsProvider<Item> prov, BlazingTagsV2.Items tag) {
		return tagAppender(prov, tag.tag());
	}

	public static TagsProvider.TagAppender<Item> blockItemTagAppender(RegistrateTagsProvider<Item> prov, BlazingTagsV2.Blocks tag) {
		return tagAppender(prov, tag.itemTag());
	}

	public static TagsProvider.TagAppender<Block> tagAppender(RegistrateTagsProvider<Block> prov, BlazingTagsV2.Blocks tag) {
		return tagAppender(prov, tag.tag());
	}

	public static TagsProvider.TagAppender<Fluid> tagAppender(RegistrateTagsProvider<Fluid> prov, BlazingTagsV2.Fluids tag) {
		return tagAppender(prov, tag.tag());
	}

	public static <T> TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
		return prov.addTag(tag);
	}
}
