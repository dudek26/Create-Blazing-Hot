package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class AbstractBlazeMixingRecipeGen extends BlazingRecipeProvider {

    protected static final List<AbstractBlazeMixingRecipeGen> GENERATORS = new ArrayList<>();

    public AbstractBlazeMixingRecipeGen(PackOutput output) {
        super(output);
    }

    protected IRecipeTypeInfo getRecipeType() {
        return BlazingRecipeTypes.BLAZE_MIXING;
    }

    public static DataProvider registerAll(PackOutput output) {
        GENERATORS.add(new BlazeMixingRecipeGen(output));

        return new DataProvider() {

            @Override
            public @NotNull String getName() {
                return "Blazing Hot Blaze Mixing Recipes";
            }

            @Override
            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS
                        .stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        };
    }

    protected GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<BlazeMixingRecipeBuilder> transform) {
        BlazeMixingRecipeSerializer serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform.apply(new BlazeMixingRecipeBuilder(serializer.getFactory(),
                    new ResourceLocation(namespace,
                            RegisteredObjects.getKeyOrThrow(itemLike.asItem()).getPath())).withItemIngredients(
                    Ingredient.of(itemLike))).build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<BlazeMixingRecipeBuilder> transform) {
        BlazeMixingRecipeSerializer serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> transform.apply(new BlazeMixingRecipeBuilder(serializer.getFactory(),
                name.get())).build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    protected GeneratedRecipe create(ResourceLocation name, UnaryOperator<BlazeMixingRecipeBuilder> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<BlazeMixingRecipeBuilder> transform) {
        return create(BlazingHot.asResource(name), transform);
    }

    protected BlazeMixingRecipeSerializer getSerializer() {
        return getRecipeType().getSerializer();
    }

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(item.get().asItem());
            return BlazingHot.asResource(registryName.getPath() + suffix);
        };
    }

}
