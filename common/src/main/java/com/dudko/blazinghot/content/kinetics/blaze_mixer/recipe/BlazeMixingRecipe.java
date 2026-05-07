package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BlazeMixingRecipe extends BasinRecipe {

	protected BlazeMixingRecipe(ProcessingRecipeParams params) {
		super(BlazingRecipeTypes.BLAZE_MIXING, params);
	}

	@ExpectPlatform
	public static BlazeMixingRecipe create(ProcessingRecipeParams params) {
		throw new AssertionError();
	}

	/**
	 * @apiNote Already platformed.
	 */
	public static long getFuelCost(@Nullable Recipe<?> recipe, Level level) {
		switch (recipe) {
			// blaze mixing
			case BlazeMixingRecipe blazeMixingRecipe -> {
				return blazeMixingRecipe.getMixerFuelAmount();
			}

			case MixingRecipe mixingRecipe -> {
				// brewing
				for (Ingredient ingredient : mixingRecipe.getIngredients()) {
					for (ItemStack stack : ingredient.getItems()) {
						if (stack.isEmpty()) continue;

						List<MixingRecipe> list = PotionMixingRecipes.sortRecipesByItem(level).get(stack.getItem());
						if (list == null) continue;
						for (MixingRecipe potionRecipe : list)
							if (BlazeMixerBlockEntity.doInputsMatch(potionRecipe, mixingRecipe))
								return BlazingConfigs.server().recipes.fueledBrewingFuelUsage.get();
					}
				}

				// mixing
				return BlazeMixingRecipe.durationToFuelCost(mixingRecipe.getProcessingDuration());
			}
			case CraftingRecipe craftingRecipe -> {
				return BlazingConfigs.server().recipes.fueledShapelessFuelUsage.get();
			}
			case null -> {
				return MultiAmount.BUCKET.get() + 1;
			}
			default -> {
				return 0;
			}
		}
	}

	/**
	 * @apiNote Already platformed.
	 */
	public static long durationToFuelCost(int duration) {
		float recipeSpeed = 1;
		if (duration != 0) {
			recipeSpeed = duration / 100f;
		}
		return Mth.ceil(recipeSpeed * BlazingConfigs.server().recipes.fueledMixingFuelUsage.get());
	}

	@Override
	public List<String> validate() {
		List<String> errors = super.validate();
		if (super.getFluidIngredients().isEmpty()) errors.add("Recipe doesn't have any mixer fuel.");
		return errors;
	}

	public abstract long getMixerFuelAmount();

	@Override
	protected int getMaxFluidInputCount() {
		return super.getMaxFluidInputCount() + 1;
	}

//	public static MapCodec<BlazeMixingRecipe> codec(Factory factory, MapCodec<BlazeMixingRecipeParams> paramsCodec) {
//		return paramsCodec.xmap(factory::create, BlazeMixingRecipe::getParams).validate(recipe -> {
//			var errors = recipe.validate();
//			if (errors.isEmpty()) return DataResult.success(recipe);
//			errors.add(recipe.getClass().getSimpleName() + " failed validation:");
//			return DataResult.error(() -> Joiner.on('\n').join(errors), recipe);
//		});
//	}
//
//	public static StreamCodec<RegistryFriendlyByteBuf, BlazeMixingRecipe> streamCodec(Factory factory, StreamCodec<RegistryFriendlyByteBuf, BlazeMixingRecipeParams> streamCodec) {
//		return streamCodec.map(factory::create, BlazeMixingRecipe::getParams);
//	}
//
//	public static class Serializer implements RecipeSerializer<BlazeMixingRecipe> {
//		private final MapCodec<BlazeMixingRecipe> codec;
//		private final StreamCodec<RegistryFriendlyByteBuf, BlazeMixingRecipe> streamCodec;
//
//		public Serializer(Factory factory) {
//			this.codec = BlazeMixingRecipe.codec(factory, BlazeMixingRecipeParams.CODEC);
//			this.streamCodec = BlazeMixingRecipe.streamCodec(factory, BlazeMixingRecipeParams.STREAM_CODEC);
//		}
//
//		@Override
//		public MapCodec<BlazeMixingRecipe> codec() {
//			return codec;
//		}
//
//		@Override
//		public StreamCodec<RegistryFriendlyByteBuf, BlazeMixingRecipe> streamCodec() {
//			return streamCodec;
//		}
//
//	}

}
