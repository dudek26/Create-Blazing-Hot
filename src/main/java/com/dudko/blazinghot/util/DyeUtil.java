package com.dudko.blazinghot.util;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;

public class DyeUtil {


    public static ShapedRecipeBuilder dyeingMultiple(RecipeCategory category, TagKey<Item> tag, ItemLike result, DyeColor dyeColor) {
        return ShapedRecipeBuilder
                .shaped(category, result, 8)
                .pattern("###")
                .pattern("#d#")
                .pattern("###")
                .define('#', tag)
                .define('d', dyeColor.getTag())
                .unlockedBy("has_" + tag.location().getPath(), RegistrateRecipeProvider.has(tag));
    }

    public static ShapelessRecipeBuilder dyeingSingle(RecipeCategory category, TagKey<Item> tag, ItemLike result, DyeColor dyeColor) {
        return ShapelessRecipeBuilder
                .shapeless(category, result, 1)
                .requires(tag)
                .requires(dyeColor.getTag())
                .unlockedBy("has_" + tag.location().getPath(), RegistrateRecipeProvider.has(tag));
    }

    public enum Dyes {
        BLACK(DyeColor.BLACK, Tags.Items.DYES_BLACK, Items.BLACK_DYE),
        BLUE(DyeColor.BLUE, Tags.Items.DYES_BLUE, Items.BLUE_DYE),
        BROWN(DyeColor.BROWN, Tags.Items.DYES_BROWN, Items.BROWN_DYE),
        WHITE(DyeColor.WHITE, Tags.Items.DYES_WHITE, Items.WHITE_DYE),
        GREEN(DyeColor.GREEN, Tags.Items.DYES_GREEN, Items.GREEN_DYE),
        RED(DyeColor.RED, Tags.Items.DYES_RED, Items.RED_DYE),
        LIME(DyeColor.LIME, Tags.Items.DYES_LIME, Items.LIME_DYE),
        YELLOW(DyeColor.YELLOW, Tags.Items.DYES_YELLOW, Items.YELLOW_DYE),
        ORANGE(DyeColor.ORANGE, Tags.Items.DYES_ORANGE, Items.ORANGE_DYE),
        LIGHT_BLUE(DyeColor.LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE, Items.LIGHT_BLUE_DYE),
        MAGENTA(DyeColor.MAGENTA, Tags.Items.DYES_MAGENTA, Items.MAGENTA_DYE),
        PINK(DyeColor.PINK, Tags.Items.DYES_PINK, Items.PINK_DYE),
        GRAY(DyeColor.GRAY, Tags.Items.DYES_GRAY, Items.GRAY_DYE),
        LIGHT_GRAY(DyeColor.LIGHT_GRAY, Tags.Items.DYES_LIGHT_GRAY, Items.LIGHT_GRAY_DYE),
        CYAN(DyeColor.CYAN, Tags.Items.DYES_CYAN, Items.CYAN_DYE),
        PURPLE(DyeColor.PURPLE, Tags.Items.DYES_PURPLE, Items.PURPLE_DYE);

        private final DyeColor color;
        private final TagKey<Item> tag;
        private final Item item;

        Dyes(DyeColor color, TagKey<Item> tag, Item item) {
            this.color = color;
            this.tag = tag;
            this.item = item;
        }
    }

    public static TagKey<Item> getDyeTag(DyeColor color) {
        return Arrays.stream(Dyes.values()).filter(dye -> dye.color == color).findFirst().orElse(Dyes.WHITE).tag;
    }

    public static Item getDyeItem(DyeColor color) {
        return Arrays.stream(Dyes.values()).filter(dye -> dye.color == color).findFirst().orElse(Dyes.WHITE).item;
    }

}
