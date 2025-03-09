package com.dudko.blazinghot.data.fabric;

import static com.dudko.blazinghot.data.BlazingTagGen.OPTIONAL_TAGS;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;

import com.dudko.blazinghot.content.metal.Forms;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.data.BlazingTagGen;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.dudko.blazinghot.util.DyeUtil;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

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
				.add(Fluids.LAVA)
				.add(BlazingFluidsImpl.NETHER_LAVA.getSource());

		for (BlazingTags.Fluids tag : BlazingTags.Fluids.values()) {
			if (tag.alwaysDatagen) {
				BlazingTagGen.tagAppender(prov, tag);
			}
		}

		for (MoltenMetal metal : MoltenMetals.ALL) {
			TagKey<Fluid> tag = CommonTags.fluidTagOf(metal.moltenName(), CommonTags.Namespace.platform());

			tagAppender(prov, tag);
		}

		for (CommonTags.Fluids tag : CommonTags.Fluids.values()) {
			tagAppender(prov, tag.tag());
		}
	}

	public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
		prov
				.addTag(BlazingTags.Items.NETHER_FLORA.tag)
				.add(Items.WARPED_FUNGUS,
						Items.CRIMSON_FUNGUS,
						Items.WARPED_ROOTS,
						Items.CRIMSON_ROOTS,
						Items.WEEPING_VINES,
						Items.TWISTING_VINES,
						Items.NETHER_SPROUTS);

		prov.addTag(BlazingTags.Items.METAL_CARROTS.tag).add(Items.GOLDEN_CARROT);
		prov.addTag(BlazingTags.Items.METAL_APPLES.tag).add(Items.GOLDEN_APPLE);
		prov.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag).add(Items.ENCHANTED_GOLDEN_APPLE);

		BlazingTagGen
				.tagAppender(prov, BlazingTags.Items.METAL_FOOD)
				.addTag(BlazingTags.Items.METAL_CARROTS.tag)
				.addTag(BlazingTags.Items.METAL_APPLES.tag)
				.addTag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
				.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag);

		for (BlazingTags.Items tag : BlazingTags.Items.values()) {
			if (tag.alwaysDatagen) BlazingTagGen.tagAppender(prov, tag);
		}

		for (MoltenMetal metal : MoltenMetals.ALL) {
			if (metal.ignoreTagGen) continue;
			for (Forms form : metal.nonCustomForms()) {
				TagKey<Item>
						tag =
						itemTagOf(CommonTags.Namespace.platform().tagPath(form.tagFolder, metal.name),
								CommonTags.Namespace.platform());
				tagAppender(prov, tag);
			}
		}

		for (CommonTags.Items tag : CommonTags.Items.values()) {
			tagAppender(prov, tag.tag());
		}

		for (DyeUtil.Dyes dye : DyeUtil.Dyes.values()) {
			tagAppender(prov, dye.tag);
		}
	}

	public static <T> TagsProvider.TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
		return prov.addTag(tag);
	}

}
