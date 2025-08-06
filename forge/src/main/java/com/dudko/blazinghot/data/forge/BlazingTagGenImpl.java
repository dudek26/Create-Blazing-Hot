package com.dudko.blazinghot.data.forge;

import static com.dudko.blazinghot.data.BlazingTagGen.OPTIONAL_TAGS;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.data.BlazingTagGen;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.forge.BlazingFluidsImpl;
import com.dudko.blazinghot.util.DyeUtil;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class BlazingTagGenImpl {

	public static void generateBlockTags(RegistrateTagsProvider<Block> prov) {
		for (BlazingTags.Blocks tag : BlazingTags.Blocks.values()) {
			if (tag.alwaysDatagen) {
				BlazingTagGen.tagAppender(prov, tag);
			}
		}
		for (TagKey<Block> tag : OPTIONAL_TAGS.keySet()) {
			var appender = tagAppender(prov, tag);
			for (ResourceLocation loc : OPTIONAL_TAGS.get(tag))
				appender.addOptional(loc);
		}
		for (CommonTags.Blocks tag : CommonTags.Blocks.values()) {
			tagAppender(prov, tag.tag());
		}
	}

	public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov) {
		prov
				.addTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag)
				.add(ForgeRegistries.FLUIDS.getResourceKey(Fluids.LAVA).get())
				.add(ForgeRegistries.FLUIDS.getResourceKey(BlazingFluidsImpl.NETHER_LAVA.get()).get());

		for (BlazingTags.Fluids tag : BlazingTags.Fluids.values()) {
			if (tag.alwaysDatagen) {
				BlazingTagGen.tagAppender(prov, tag);
			}
		}

		for (BlazingMetal metal : BlazingMetals.ALL) {
			TagKey<Fluid> tag = CommonTags.fluidTagOf(metal.getMoltenName(), CommonTags.Namespace.platform());
			tagAppender(prov, tag);
		}

		for (CommonTags.Fluids tag : CommonTags.Fluids.values()) {
			tagAppender(prov, tag.tag());
		}
	}

	public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
		prov
				.addTag(BlazingTags.Items.NETHER_FLORA.tag)
				.add(itemKey(Items.WARPED_FUNGUS),
						itemKey(Items.CRIMSON_FUNGUS),
						itemKey(Items.WARPED_ROOTS),
						itemKey(Items.CRIMSON_ROOTS),
						itemKey(Items.WEEPING_VINES),
						itemKey(Items.TWISTING_VINES),
						itemKey(Items.NETHER_SPROUTS));

		prov.addTag(BlazingTags.Items.METAL_CARROTS.tag).add(itemKey(Items.GOLDEN_CARROT));
		prov.addTag(BlazingTags.Items.METAL_APPLES.tag).add(itemKey(Items.GOLDEN_APPLE));
		prov.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag).add(itemKey(Items.ENCHANTED_GOLDEN_APPLE));

		BlazingTagGen
				.tagAppender(prov, BlazingTags.Items.METAL_FOOD)
				.addTag(BlazingTags.Items.METAL_CARROTS.tag)
				.addTag(BlazingTags.Items.METAL_APPLES.tag)
				.addTag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
				.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag);

		for (BlazingTags.Items tag : BlazingTags.Items.values()) {
			if (tag.alwaysDatagen) BlazingTagGen.tagAppender(prov, tag);
		}

		/*
		for (MoltenMetal metal : MoltenMetals.ALL) {
			if (metal.ignoreTagGen) continue;
			for (Forms form : metal.nonCustomForms()) {
				TagKey<Item> tag = itemTagOf(form.tagFolder, metal.name, CommonTags.Namespace.platform());
				tagAppender(prov, tag);

			}
		}
		 */

		for (CommonTags.Items tag : CommonTags.Items.values()) {
			tagAppender(prov, tag.tag());
		}

		for (DyeUtil.Dyes dye : DyeUtil.Dyes.values()) {
			tagAppender(prov, dye.tag);
		}
	}

	private static @NotNull ResourceKey<Item> itemKey(Item item) {
		return ForgeRegistries.ITEMS.getResourceKey(item).get();
	}

	public static <T> TagsProvider.TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
		return prov.addTag(tag);
	}

}
