package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.foundation.mixin_interfaces.IProcessingRecipeParams;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

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
				return blazeMixingRecipe.getLegacyMixerFuelAmount();
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

	public Optional<MixerFuel> getFuel() {
		return IProcessingRecipeParams.cast(params).blazinghot$getMixerFuel();
	}

	public long getFuelAmount(Either<TagKey<Fluid>, ResourceKey<Fluid>> fluid) {
		return getFuel().map(fuel -> fuel.amount()).orElse(0L);
	}

	public abstract long getLegacyMixerFuelAmount();

	@Override
	protected int getMaxFluidInputCount() {
		return super.getMaxFluidInputCount() + 1;
	}

	@Override
	public List<String> validate() {
		BlazingHot.LOGGER.warn("A Blaze Mixing Recipe uses legacy mixer fuel definition! This definition won't be supported in the future.");
		return super.validate();
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
