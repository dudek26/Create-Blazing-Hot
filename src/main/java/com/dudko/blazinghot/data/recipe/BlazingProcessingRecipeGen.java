package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public abstract class BlazingProcessingRecipeGen extends BlazingRecipeProvider {

    protected static final List<BlazingProcessingRecipeGen> GENERATORS = new ArrayList<>();
    protected static final long BUCKET = FluidConstants.BUCKET;
    protected static final long INGOT = FluidConstants.INGOT;
    protected static final long INGOT_COVER = FluidConstants.INGOT * 8;
    protected static final long NUGGET_COVER = FluidConstants.NUGGET * 8;
    protected static final long NUGGET = FluidConstants.NUGGET;
    protected static final long BOTTLE = FluidConstants.BOTTLE;

    protected enum Forms {
        INGOT(BlazingProcessingRecipeGen.INGOT, "ingots", 300),
        NUGGET(BlazingProcessingRecipeGen.NUGGET, "nuggets", 40),
        BLOCK(BlazingProcessingRecipeGen.BUCKET, "blocks", 2400),
        SHEET(BlazingProcessingRecipeGen.INGOT, "plates", 300);

        public final long amount;
        public final String tagSuffix;
        public final int meltingTime;

        Forms(long amount, String tagSuffix, int meltingTime) {
            this.amount = amount;
            this.tagSuffix = tagSuffix;
            this.meltingTime = meltingTime;
        }

        public String tag(String material) {
            return material + "_" + tagSuffix;
        }

    }

    public BlazingProcessingRecipeGen(FabricDataOutput output) {
        super(output);
    }

    public static DataProvider registerAll(FabricDataOutput output) {
        GENERATORS.add(new BlazingPressingRecipeGen(output));
        GENERATORS.add(new BlazingCompactingRecipeGen(output));
        GENERATORS.add(new BlazingCrushingRecipeGen(output));
        GENERATORS.add(new BlazingCuttingRecipeGen(output));
        GENERATORS.add(new BlazingDeployingRecipeGen(output));
        GENERATORS.add(new BlazingMillingRecipeGen(output));
        GENERATORS.add(new BlazingMixingRecipeGen(output));
        GENERATORS.add(new BlazingFillingRecipeGen(output));
        GENERATORS.add(new BlazingHauntingRecipeGen(output));
        GENERATORS.add(new BlazingItemApplicationRecipeGen(output));

        return new DataProvider() {

            @Override
            public @NotNull String getName() {
                return "Blazing Hot Processing Recipes";
            }

            @Override
            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
                return CompletableFuture.allOf(
                        GENERATORS.stream().map(gen -> gen.run(dc)).toArray(CompletableFuture[]::new));
            }
        };
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), new ResourceLocation(namespace,
                                                                                                       RegisteredObjects
                                                                                                               .getKeyOrThrow(
                                                                                                                       itemLike.asItem())
                                                                                                               .getPath())).withItemIngredients(
                            Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    <T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(BlazingHot.ID, singleIngredient, transform);
    }

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe
                generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get())).build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(BlazingHot.asResource(name), transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return getRecipeType().getSerializer();
    }

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(item.get().asItem());
            return BlazingHot.asResource(registryName.getPath() + suffix);
        };
    }

    @Override
    public @NotNull String getName() {
        return "Blazing Hot Processing Recipes";
    }
}
