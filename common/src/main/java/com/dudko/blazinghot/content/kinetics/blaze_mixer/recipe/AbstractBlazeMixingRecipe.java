package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluidIngredient;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeType;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AbstractBlazeMixingRecipe extends BasinRecipe {

	public AbstractBlazeMixingRecipe(BlazingRecipeType recipeType, ProcessingRecipeParams params) {
		super(recipeType, params);
	}

	@Override
	public List<String> validate() {
		List<String> errors = super.validate();
		if (super.getFluidIngredients().isEmpty()) errors.add("Recipe doesn't have any mixer fuel.");
		return errors;
	}

	@Override
	public NonNullList<SizedFluidIngredient> getFluidIngredients() {
		NonNullList<SizedFluidIngredient> fluidIngredients = NonNullList.create();
		fluidIngredients.addAll(super.getFluidIngredients());
		fluidIngredients.removeLast();
		return fluidIngredients;
	}

	public static SizedFluidIngredient mixerFuelPlaceholder() {
		return MultiFluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_PLACEHOLDER.tag(),
			MultiAmount.fromBucketFraction(1, 10));
	}

	@Deprecated
	public SizedFluidIngredient getMixerFuel() {
		if (super.getFluidIngredients().isEmpty()) {
			return MultiFluidIngredient.empty();
		}
		return super.getFluidIngredients()
			.stream()
			.filter(AbstractBlazeMixingRecipe::isPlaceholder)
			.findFirst()
			.orElse(MultiFluidIngredient.empty());
	}

	public int getMixerFuelAmount() {
		SizedFluidIngredient fuel = getMixerFuel();
		if (fuel.ingredient().isEmpty()) return 0;
		return fuel.amount();
	}

	@Override
	protected int getMaxFluidInputCount() {
		return super.getMaxFluidInputCount() + 1;
	}

	@ExpectPlatform
	public static boolean isPlaceholder(SizedFluidIngredient fluidIngredient) {
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
