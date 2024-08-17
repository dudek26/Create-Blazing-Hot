package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.dudko.blazinghot.content.metal.MoltenMetal.BLAZE_GOLD;
import static com.dudko.blazinghot.content.metal.Form.INGOT;
import static com.dudko.blazinghot.content.metal.Form.NUGGET;
import static com.dudko.blazinghot.content.metal.MoltenMetal.IRON;
import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;
import static com.dudko.blazinghot.registry.BlazingBlocks.BLAZE_GOLD_BLOCK;
import static com.dudko.blazinghot.registry.BlazingItems.BLAZE_GOLD_INGOT;
import static com.dudko.blazinghot.registry.BlazingItems.BLAZE_GOLD_NUGGET;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public class BlazingCraftingRecipeGen extends BlazingRecipeProvider {

    public BlazingCraftingRecipeGen(PackOutput output) {
        super(output);
        generate();
    }


    private GeneratedRecipe compressing(ItemLike ingredient, ItemLike result, ItemLike unlockedBy) {
        return create(() -> result)
                .unlockedBy(() -> unlockedBy)
                .withSuffix("_from_" + ingredient.asItem())
                .viaShaped(b -> b.define('X', ingredient).pattern("XXX").pattern("XXX").pattern("XXX"));
    }

    private GeneratedRecipe decompressing(ItemLike ingredient, ItemLike result, int count, ItemLike unlockedBy) {
        return create(() -> result)
                .returns(count)
                .unlockedBy(() -> unlockedBy)
                .withSuffix("_from_" + ingredient.asItem())
                .viaShapeless(b -> b.requires(ingredient));
    }

    private GeneratedRecipe covering(ItemLike ingredient, TagKey<Item> cover, ItemLike result, TagKey<Item> unlockedBy) {
        return create(() -> result)
                .unlockedByTag(() -> unlockedBy)
                .viaShaped(b -> b
                        .define('Y', ingredient)
                        .define('X', cover)
                        .pattern("XXX")
                        .pattern("XYX")
                        .pattern("XXX"));
    }

    private void generate() {
        compressing(BLAZE_GOLD_INGOT, BLAZE_GOLD_BLOCK, BLAZE_GOLD_INGOT);
        compressing(BLAZE_GOLD_NUGGET, BLAZE_GOLD_INGOT, BLAZE_GOLD_INGOT);

        decompressing(BLAZE_GOLD_BLOCK, BLAZE_GOLD_INGOT, 9, BLAZE_GOLD_INGOT);
        decompressing(BLAZE_GOLD_INGOT, BLAZE_GOLD_NUGGET, 9, BLAZE_GOLD_INGOT);
    }

    GeneratedRecipe
            IRON_APPLE = metalApple(IRON, BlazingItems.IRON_APPLE),
            IRON_CARROT = metalCarrot(IRON, BlazingItems.IRON_CARROT),
            BLAZE_APPLE = metalApple(BLAZE_GOLD, BlazingItems.BLAZE_APPLE),
            BLAZE_CARROT = metalCarrot(BLAZE_GOLD, BlazingItems.BLAZE_CARROT),
            WHITE_MODERN_LAMP =
                    create(BlazingBlocks.MODERN_LAMP_BLOCKS.get(DyeColor.WHITE))
                            .unlockedByTag(Ingredients::blazeGoldRod)
                            .returns(2)
                            .viaShaped(b -> b
                                    .define('X', blazeGoldRod())
                                    .define('Y', glowstone())
                                    .pattern(" X ")
                                    .pattern("XYX")
                                    .pattern(" X ")),
            BLAZE_ARROW =
                    create(BlazingItems.BLAZE_ARROW)
                            .unlockedByTag(Ingredients::blazeGoldRod)
                            .returns(4)
                            .viaShaped(b -> b
                                    .define('X', coal())
                                    .define('Y', blazeGoldRod())
                                    .define('Z', feather())
                                    .pattern(" X ")
                                    .pattern(" Y ")
                                    .pattern(" Z ")),
            BLAZE_WHISK =
                    create(BlazingItems.BLAZE_WHISK)
                            .unlockedByTag(Ingredients::blazeGoldIngot)
                            .viaShaped(b -> b
                                    .define('X', brassIngot())
                                    .define('Y', blazeGoldSheet())
                                    .pattern(" X ")
                                    .pattern("YXY")
                                    .pattern("YYY"));

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder("/", result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder("/", result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }

    private GeneratedRecipe metalApple(MoltenMetal metal, ItemLike result) {
        return covering(apple(), INGOT.internalTag(metal), result, INGOT.internalTag(metal));
    }

    private GeneratedRecipe metalCarrot(MoltenMetal metal, ItemLike result) {
        return covering(carrot(), NUGGET.internalTag(metal), result, INGOT.internalTag(metal));
    }

    @Override
    public @NotNull String getName() {
        return "Blazing Hot Crafting Recipes";
    }

    class GeneratedRecipeBuilder {

        private final String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;

        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item().of(item.get()).build();
            return this;
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item().of(tag.get()).build();
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder
                        b =
                        builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null) b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(consumer -> {
                ShapelessRecipeBuilder
                        b =
                        builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null) b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private static ResourceLocation clean(ResourceLocation loc) {
            String path = loc.getPath();
            while (path.contains("//")) path = path.replaceAll("//", "/");
            return new ResourceLocation(loc.getNamespace(), path);
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            return clean(BlazingHot.asResource(recipeType + "/" + getRegistryName().getPath() + suffix));
        }

        private ResourceLocation createLocation(String recipeType) {
            return clean(BlazingHot.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix));
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ?
                    RegisteredObjects.getKeyOrThrow(result.get().asItem()) :
                    compatDatagenOutput;
        }

    }

}
