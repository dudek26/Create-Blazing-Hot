package com.dudko.blazinghot.compat.jei.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.fabric.BlazingRecipeTypesImpl;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.common.accessor.RecipeManagerAccessor;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public class BlazingJEI implements IModPlugin {

    private static final ResourceLocation ID = BlazingHot.asResource("jei_plugin");

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    @SuppressWarnings("unused")
    private void loadCategories() {
        allCategories.clear();

        CreateRecipeCategory<?>
                blazeMixing =
                builder(BasinRecipe.class)
                        .addTypedRecipes(BlazingRecipeTypesImpl.BLAZE_MIXING)
                        .addTypedRecipesExcluding(AllRecipeTypes.MIXING::getType,
                                BlazingRecipeTypes.BLAZE_MIXING::getType)
                        .catalyst(BlazingBlocks.BLAZE_MIXER::get)
                        .catalyst(AllBlocks.BASIN::get)
                        .doubleItemIcon(BlazingBlocks.BLAZE_MIXER.get(), AllBlocks.BASIN.get())
                        .emptyBackground(177, 103)
                        .build("blaze_mixing", BlazeMixingCategory::standard),

                blazeAutoShapeless =
                        builder(BasinRecipe.class)
                                .enableWhen(c -> c.allowShapelessInMixer)
                                .addAllRecipesIf(r -> r instanceof CraftingRecipe
                                        && !(r instanceof ShapedRecipe)
                                        && r.getIngredients().size() > 1
                                        && !MechanicalPressBlockEntity.canCompress(r)
                                        && !AllRecipeTypes.shouldIgnoreInAutomation(r), BasinRecipe::convertShapeless)
                                .catalyst(BlazingBlocks.BLAZE_MIXER::get)
                                .catalyst(AllBlocks.BASIN::get)
                                .doubleItemIcon(BlazingBlocks.BLAZE_MIXER.get(), Items.CRAFTING_TABLE)
                                .emptyBackground(177, 103)
                                .build("blaze_automatic_shapeless", BlazeMixingCategory::autoShapeless),

                blazeBrewing =
                        builder(BasinRecipe.class)
                                .enableWhen(c -> c.allowBrewingInMixer)
                                .addRecipes(() -> PotionMixingRecipes.ALL)
                                .catalyst(BlazingBlocks.BLAZE_MIXER::get)
                                .catalyst(AllBlocks.BASIN::get)
                                .doubleItemIcon(BlazingBlocks.BLAZE_MIXER.get(), Blocks.BREWING_STAND)
                                .emptyBackground(177, 103)
                                .build("blaze_automatic_brewing", BlazeMixingCategory::autoBrewing);
    }

    private <T extends Recipe<?>> BlazingJEI.CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new BlazingJEI.CategoryBuilder<>(recipeClass);
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @SuppressWarnings({"unused", "UnusedReturnValue", "unchecked"})
    private class CategoryBuilder<T extends Recipe<?>> {
        private final Class<? extends T> recipeClass;
        private Predicate<CRecipes> predicate = cRecipes -> true;

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public BlazingJEI.CategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
            this.predicate = predicate;
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
            predicate = c -> configValue.apply(c).get();
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
            return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
        }

        public BlazingJEI.CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add((T) recipe);
                }
            }));
        }

        public BlazingJEI.CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(converter.apply(recipe));
                }
            }));
        }

        public BlazingJEI.CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public BlazingJEI.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> BlazingJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
        }

        public BlazingJEI.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
            return addRecipeListConsumer(recipes -> BlazingJEI.<T>consumeTypedRecipes(recipe -> recipes.add(converter.apply(
                    recipe)), recipeType.get()));
        }

        public BlazingJEI.CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
            return addRecipeListConsumer(recipes -> BlazingJEI.<T>consumeTypedRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(recipe);
                }
            }, recipeType.get()));
        }

        public BlazingJEI.CategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType, Supplier<RecipeType<? extends T>> excluded) {
            return addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = getTypedRecipes(excluded.get());
                BlazingJEI.<T>consumeTypedRecipes(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (doInputsMatch(recipe, excludedRecipe)) {
                            return;
                        }
                    }
                    recipes.add(recipe);
                }, recipeType.get());
            });
        }

        public BlazingJEI.CategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = getTypedRecipes(recipeType.get());
                recipes.removeIf(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes)
                        if (doInputsMatch(recipe, excludedRecipe) && doOutputsMatch(recipe, excludedRecipe))
                            return true;
                    return false;
                });
            });
        }

        public BlazingJEI.CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            catalysts.add(supplier);
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get().asItem()));
        }

        public BlazingJEI.CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> itemIcon(ItemLike item) {
            icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public BlazingJEI.CategoryBuilder<T> emptyBackground(int width, int height) {
            background(new EmptyBackground(width, height));
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (predicate.test(AllConfigs.server().recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList<>();
                    for (Consumer<List<T>> consumer : recipeListConsumers)
                        consumer.accept(recipes);
                    return recipes;
                };
            }
            else {
                recipesSupplier = Collections::emptyList;
            }

            CreateRecipeCategory.Info<T>
                    info =
                    new CreateRecipeCategory.Info<>(new mezz.jei.api.recipe.RecipeType<>(BlazingHot.asResource(name),
                            recipeClass),
                            Component.translatable(BlazingHot.ID + ".recipe." + name),
                            background,
                            icon,
                            recipesSupplier,
                            catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            allCategories.add(category);
            return category;
        }
    }

    public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
        Minecraft.getInstance().getConnection().getRecipeManager().getRecipes().forEach(consumer);
    }

    public static <T extends Recipe<?>> void consumeTypedRecipes(Consumer<T> consumer, RecipeType<?> type) {
        Map<ResourceLocation, Recipe<?>>
                map =
                ((RecipeManagerAccessor) Minecraft.getInstance().getConnection().getRecipeManager())
                        .port_lib$getRecipes()
                        .get(type);
        if (map != null) {
            map.values().forEach(recipe -> consumer.accept((T) recipe));
        }
    }

    public static List<Recipe<?>> getTypedRecipes(RecipeType<?> type) {
        List<Recipe<?>> recipes = new ArrayList<>();
        consumeTypedRecipes(recipes::add, type);
        return recipes;
    }

    public static List<Recipe<?>> getTypedRecipesExcluding(RecipeType<?> type, Predicate<Recipe<?>> exclusionPred) {
        List<Recipe<?>> recipes = getTypedRecipes(type);
        recipes.removeIf(exclusionPred);
        return recipes;
    }

    public static boolean doInputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        if (recipe1 instanceof MixingRecipe mixing && recipe2 instanceof BlazeMixingRecipe blazeMixing) {
            return BlazeMixerBlockEntity.doInputsMatch(mixing, blazeMixing);
        }
        if (recipe1.getIngredients().isEmpty() || recipe2.getIngredients().isEmpty()) {
            return false;
        }
        ItemStack[] matchingStacks = recipe1.getIngredients().get(0).getItems();
        if (matchingStacks.length == 0) {
            return false;
        }
        return recipe2.getIngredients().get(0).test(matchingStacks[0]);
    }

    public static boolean doOutputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        return ItemHelper.sameItem(recipe1.getResultItem(registryAccess), recipe2.getResultItem(registryAccess));
    }
}
