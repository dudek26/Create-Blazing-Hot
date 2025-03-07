package com.dudko.blazinghot.data.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.BlazingHot;
<<<<<<<< HEAD:common/src/main/java/com/dudko/blazinghot/data/recipe/BlazingProcessingRecipeGen.java
========
import com.dudko.blazinghot.multiloader.fluid.MultiFluids.Constants;
>>>>>>>> 9fe0e00 (wip recipe datagen rework):fabric/src/main/java/com/dudko/blazinghot/data/recipe/fabric/BlazingProcessingRecipeGen.java
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.multiloader.fluid.MultiFluids.Constants;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
public abstract class BlazingProcessingRecipeGen extends BlazingRecipeProvider {

	protected static final List<BlazingProcessingRecipeGen> GENERATORS = new ArrayList<>();
	protected static final long INGOT_COVER = Constants.INGOT.platformed() * 6;
	protected static final long NUGGET_COVER = Constants.NUGGET.platformed() * 6;

	public BlazingProcessingRecipeGen(PackOutput output) {
		super(output);
	}

	public static DataProvider registerAll(PackOutput output) {
		GENERATORS.add(new PressingRecipeGen(output));
		GENERATORS.add(new CompactingRecipeGen(output));
		GENERATORS.add(new CrushingRecipeGen(output));
		GENERATORS.add(new CuttingRecipeGen(output));
		GENERATORS.add(new DeployingRecipeGen(output));
		GENERATORS.add(new MillingRecipeGen(output));
		GENERATORS.add(new MixingRecipeGen(output));
		GENERATORS.add(new FillingRecipeGen(output));
		GENERATORS.add(new HauntingRecipeGen(output));
		GENERATORS.add(new ItemApplicationRecipeGen(output));
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

	protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<BlazingProcessingRecipeBuilder<T>> transform) {
		ProcessingRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe = c -> {
			ItemLike itemLike = singleIngredient.get();
			transform
					.apply(new BlazingProcessingRecipeBuilder<>(serializer.getFactory(),
							new ResourceLocation(namespace,
									MultiRegistries
											.getRegisteredObjectsHelper()
											.getKeyOrThrow(itemLike.asItem())
											.getPath())).withItemIngredients(Ingredient.of(itemLike)))
					.build(c);
		};
		all.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Create a processing recipe with a single itemstack ingredient, using its id
	 * as the name of the recipe
	 */
	<T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<BlazingProcessingRecipeBuilder<T>> transform) {
		return create(BlazingHot.ID, singleIngredient, transform);
	}

	protected <T extends ProcessingRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<BlazingProcessingRecipeBuilder<T>> transform) {
		ProcessingRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe
				generatedRecipe =
				c -> transform
						.apply(new BlazingProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
						.build(c);
		all.add(generatedRecipe);
		return generatedRecipe;
	}

	/**
	 * Create a new processing recipe, with recipe definitions provided by the
	 * function
	 */
	protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name, UnaryOperator<BlazingProcessingRecipeBuilder<T>> transform) {
		return createWithDeferredId(() -> name, transform);
	}

	/**
	 * Create a new processing recipe, with recipe definitions provided by the
	 * function
	 */
	<T extends ProcessingRecipe<?>> GeneratedRecipe create(String name, UnaryOperator<BlazingProcessingRecipeBuilder<T>> transform) {
		return create(BlazingHot.asResource(name), transform);
	}

	protected abstract IRecipeTypeInfo getRecipeType();

	protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
		return getRecipeType().getSerializer();
	}

	protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
		return () -> {
			ResourceLocation
					registryName =
					MultiRegistries.getRegisteredObjectsHelper().getKeyOrThrow(item.get().asItem());
			return BlazingHot.asResource(registryName.getPath() + suffix);
		};
	}

	@Override
	public @NotNull String getName() {
		return "Blazing Hot Processing Recipes";
	}
}
