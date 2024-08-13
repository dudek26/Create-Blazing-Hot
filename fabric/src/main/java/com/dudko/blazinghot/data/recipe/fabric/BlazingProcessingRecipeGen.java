package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.defaultDurationToFuelCost;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public abstract class BlazingProcessingRecipeGen extends BlazingRecipeProvider {

    protected static final List<BlazingProcessingRecipeGen> GENERATORS = new ArrayList<>();
    protected static final long BUCKET = Constants.BUCKET.platformed();
    protected static final long INGOT = Constants.INGOT.platformed();
    protected static final long INGOT_COVER = Constants.INGOT.platformed() * 8;
    protected static final long NUGGET_COVER = Constants.NUGGET.platformed() * 8;
    protected static final long NUGGET = Constants.NUGGET.platformed();
    protected static final long BOTTLE = Constants.BOTTLE.platformed();

    protected enum Forms {
        INGOT(Constants.INGOT.platformed(), "ingots", 400),
        NUGGET(Constants.NUGGET.platformed(), "nuggets", 65),
        BLOCK(Constants.BUCKET.platformed(), "blocks", 2400, false),
        SHEET(Constants.INGOT.platformed(), "plates", 400),
        ROD(Constants.INGOT.platformed() / 2, "rods", 250);

        public final long amount;
        public final String tagSuffix;
        public final int meltingTime;
        public final long fuelCost;
        public final boolean mechanicalMixerMelting;

        Forms(long amount, String tagSuffix, int meltingTime, long fuelCost, boolean mechanicalMixerMelting) {
            this.amount = amount;
            this.tagSuffix = tagSuffix;
            this.meltingTime = meltingTime;
            this.fuelCost = fuelCost;
            this.mechanicalMixerMelting = mechanicalMixerMelting;
        }

        Forms(long amount, String tagSuffix, int meltingTime) {
            this(amount, tagSuffix, meltingTime, defaultDurationToFuelCost(meltingTime));
        }

        Forms(long amount, String tagSuffix, int meltingTime, boolean mechanicalMixerMelting) {
            this(amount,
                    tagSuffix,
                    meltingTime,
                    defaultDurationToFuelCost(meltingTime),
                    mechanicalMixerMelting);
        }

        Forms(long amount, String tagSuffix, int meltingTime, long fuelCost) {
            this(amount, tagSuffix, meltingTime, defaultDurationToFuelCost(meltingTime), true);
        }

        public TagKey<Item> tag(String material) {
            return CommonTags.internalItemTagOf(material + "_" + tagSuffix);
        }

        public TagKey<Item> tag(Meltables meltable) {
            return tag(meltable.name);
        }

    }

    protected enum Meltables {
        IRON("iron", BlazingFluidsImpl.MOLTEN_IRON.get()),
        GOLD("gold", BlazingFluidsImpl.MOLTEN_GOLD.get()),
        BLAZE_GOLD("blaze_gold", BlazingFluidsImpl.MOLTEN_BLAZE_GOLD.get());

        public final String name;
        public final Fluid fluid;

        Meltables(String name, Fluid fluid) {
            this.name = name;
            this.fluid = fluid;
        }
    }

    public BlazingProcessingRecipeGen(PackOutput output) {
        super(output);
    }

    public static DataProvider registerAll(PackOutput output) {
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
        GENERATORS.add(new BlazeMixingRecipeGen(output));

        return new DataProvider() {

            @Override
            public @NotNull String getName() {
                return "Blazing Hot Processing Recipes";
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

    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
                            new ResourceLocation(namespace,
                                    RegisteredObjects.getKeyOrThrow(itemLike.asItem()).getPath())).withItemIngredients(
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
