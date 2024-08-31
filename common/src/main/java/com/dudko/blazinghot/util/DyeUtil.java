package com.dudko.blazinghot.util;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class DyeUtil {

	public static ShapedRecipeBuilder dyeingMultiple(RecipeCategory category, TagKey<Item> tag, ItemLike result, DyeColor dyeColor) {
		return ShapedRecipeBuilder
				.shaped(category, result, 8)
				.pattern("###")
				.pattern("#d#")
				.pattern("###")
				.define('#', tag)
				.define('d', getDyeTag(dyeColor))
				.unlockedBy("has_" + tag.location().getPath(), RegistrateRecipeProvider.has(tag));
	}

	public static ShapelessRecipeBuilder dyeingSingle(RecipeCategory category, TagKey<Item> tag, ItemLike result, DyeColor dyeColor) {
		return ShapelessRecipeBuilder
				.shapeless(category, result, 1)
				.requires(tag)
				.requires(getDyeTag(dyeColor))
				.unlockedBy("has_" + tag.location().getPath(), RegistrateRecipeProvider.has(tag));
	}

	public enum Dyes {
		BLACK(DyeColor.BLACK, Items.BLACK_DYE),
		BLUE(DyeColor.BLUE, Items.BLUE_DYE),
		BROWN(DyeColor.BROWN, Items.BROWN_DYE),
		WHITE(DyeColor.WHITE, Items.WHITE_DYE),
		GREEN(DyeColor.GREEN, Items.GREEN_DYE),
		RED(DyeColor.RED, Items.RED_DYE),
		LIME(DyeColor.LIME, Items.LIME_DYE),
		YELLOW(DyeColor.YELLOW, Items.YELLOW_DYE),
		ORANGE(DyeColor.ORANGE, Items.ORANGE_DYE),
		LIGHT_BLUE(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_DYE),
		MAGENTA(DyeColor.MAGENTA, Items.MAGENTA_DYE),
		PINK(DyeColor.PINK, Items.PINK_DYE),
		GRAY(DyeColor.GRAY, Items.GRAY_DYE),
		LIGHT_GRAY(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_DYE),
		CYAN(DyeColor.CYAN, Items.CYAN_DYE),
		PURPLE(DyeColor.PURPLE, Items.PURPLE_DYE);

		public final DyeColor color;
		public final TagKey<Item> internalTag;
		public final TagKey<Item> forgeTag;
		public final TagKey<Item> commonTag;
		public final Item item;
		public final Supplier<Block> stainedGlass;

		Dyes(DyeColor color, Item item) {
			this.color = color;
			this.internalTag = CommonTags.itemTagOf(this + "_dyes", CommonTags.Namespace.INTERNAL);
			this.forgeTag = CommonTags.itemTagOf("dyes/" + this, CommonTags.Namespace.FORGE);
			this.commonTag = CommonTags.itemTagOf(this + "_dyes", CommonTags.Namespace.COMMON);
			this.item = item;
			this.stainedGlass = MultiRegistries.getBlockFromRegistry(Mods.VANILLA.asResource(this + "_stained_glass"));
		}

		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}

	public static TagKey<Item> getDyeTag(DyeColor color) {
		return get(color).internalTag;
	}

	public static Item getDyeItem(DyeColor color) {
		return get(color).item;
	}

	public static Block getStainedGlass(DyeColor color) {
		return get(color).stainedGlass.get();
	}

	public static Dyes get(DyeColor color) {
		return Arrays.stream(Dyes.values()).filter(dye -> dye.color == color).findFirst().orElse(Dyes.WHITE);
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {
		for (Dyes dye : Dyes.values()) {
			ResourceLocation loc = dye.internalTag.location();
			consumer.accept("tag.item." + BlazingHot.ID + "." + loc.getPath().replace('/', '.'),
					LangUtil.titleCaseConversion(dye.toString().replace('_', ' ')) + " Dyes");
		}
	}

	public static boolean isIn(DyedBlockList<?> list, ItemStack itemStack) {
		return Arrays.stream(list.toArray()).anyMatch(block -> block.isIn(itemStack));
	}

}
