package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeMixingRecipe extends BasinRecipe {

	protected final FluidIngredient mixerFuel;

	public BlazeMixingRecipe(ProcessingRecipeParams params) {
		super(BlazingRecipeTypes.BLAZE_MIXING, params);

		if (fluidIngredients.isEmpty()) this.mixerFuel = FluidIngredient.EMPTY;
		else this.mixerFuel = fluidIngredients.removeLast(); // temporary solution
	}

	public FluidIngredient getMixerFuel() {
		return mixerFuel;
	}

	/**
	 * @apiNote Already platformed.
	 */
	public static long getFuelCost(Recipe<?> recipe, Level level) {
		if (recipe instanceof MixingRecipe mixingRecipe) {
			for (Ingredient ingredient : mixingRecipe.getIngredients()) {
				for (ItemStack stack : ingredient.getItems()) {
					// TODO fix StackOverflow
//					if (stack.isEmpty()) continue;
//
//					List<MixingRecipe> list = PotionMixingRecipes.sortRecipesByItem(level).get(stack.getItem());
//					if (list == null) continue;
//					for (MixingRecipe potionRecipe : list)
//						if (BlazeMixerBlockEntity.doInputsMatch(potionRecipe, mixingRecipe))
//							return BlazingConfigs.server().recipes.blazeBrewingFuelUsage.get();
				}
			}
		}

		if (recipe.getType() == AllRecipeTypes.MIXING.getType())
			return durationToFuelCost(((ProcessingRecipe<?, ?>) recipe).getProcessingDuration());

		else if ((recipe instanceof CraftingRecipe
				&& !(recipe instanceof ShapedRecipe)
				&& BlazingConfigs.server().recipes.allowShapelessInBlazeMixer.get()
				&& recipe.getIngredients().size() > 1
				&& !MechanicalPressBlockEntity.canCompress(recipe)))
			return BlazingConfigs.server().recipes.blazeShapelessFuelUsage.get();

		return 0;
	}

	/**
	 * @apiNote Already platformed.
	 */
	public static long durationToFuelCost(int duration) {
		float recipeSpeed = 1;
		if (duration != 0) {
			recipeSpeed = duration / 100f;
		}
		return Mth.ceil(recipeSpeed * BlazingConfigs.server().recipes.blazeMixingFuelUsage.get());
	}

	@FunctionalInterface
	public interface Factory {
		BlazeMixingRecipe create(BlazeMixingRecipeParams params);
	}

	@ExpectPlatform
	public static boolean isMeltingRecipe(Recipe<?> recipe) {
		return true;
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
