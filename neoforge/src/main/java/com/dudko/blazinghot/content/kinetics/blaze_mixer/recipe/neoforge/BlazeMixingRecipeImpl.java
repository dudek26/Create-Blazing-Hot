package com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.neoforge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingTags.Fluids;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazeMixingRecipeImpl extends BlazeMixingRecipe {

	protected BlazeMixingRecipeImpl(ProcessingRecipeParams params) {
		super(params);
	}

	@Override
	public NonNullList<SizedFluidIngredient> getFluidIngredients() {
		NonNullList<SizedFluidIngredient> fluidIngredients = NonNullList.create();
		fluidIngredients.addAll(super.getFluidIngredients());
		fluidIngredients.removeIf(BlazeMixingRecipeImpl::isFuelIngredient);
		return fluidIngredients;
	}

	public static BlazeMixingRecipe create(ProcessingRecipeParams params) {
		return new BlazeMixingRecipeImpl(params);
	}

	@Override
	public long getMixerFuelAmount() {
		if (super.getFluidIngredients().isEmpty()) {
			return 0;
		}
		SizedFluidIngredient fuel = super.getFluidIngredients()
			.stream()
			.filter(BlazeMixingRecipeImpl::isFuelIngredient)
			.findFirst()
			.orElse(null);
		if (fuel == null) {
			return 0;
		}
		return fuel.amount();
	}

	public static boolean isFuelIngredient(SizedFluidIngredient fluidIngredient) {
		return fluidIngredient.ingredient().isEmpty() || fluidIngredient.ingredient() instanceof TagFluidIngredient tagIngredient && tagIngredient.tag() == Fluids.BLAZE_MIXER_FUEL.tag();
	}

}
