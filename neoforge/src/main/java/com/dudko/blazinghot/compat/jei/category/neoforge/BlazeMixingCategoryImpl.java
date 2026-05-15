package com.dudko.blazinghot.compat.jei.category.neoforge;

import java.util.Optional;

import com.dudko.blazinghot.compat.jei.category.BlazeMixingCategory;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity.MixingType;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelData;
import com.simibubi.create.content.processing.basin.BasinRecipe;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import net.minecraft.ChatFormatting;

import net.neoforged.neoforge.fluids.FluidStack;

public class BlazeMixingCategoryImpl extends BlazeMixingCategory {

	protected BlazeMixingCategoryImpl(Info<BasinRecipe> info, MixingType type) {
		super(info, type);
	}

	public static int getFluidResultsSize(BasinRecipe recipe) {
		return recipe.getFluidResults().size();
	}

	@Override
	protected void fuelTooltip(IRecipeSlotView view, ITooltipBuilder tooltip) {
		Optional<FluidStack> optionalFluid = view.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
		if (optionalFluid.isPresent()) {
			FluidStack fluidStack = optionalFluid.get();
			BlazeMixerFuelData fuelData = BlazeMixerFuelData.getFuelData(fluidStack.getFluid());
			if (fuelData != null) {
				tooltip.add(BlazingLang.BLAZE_MIXER_FUEL_SPEED.get((int) (fuelData.getSpeed(type) * 100)).withStyle(ChatFormatting.GREEN));
			}
		}
	}

	public static BlazeMixingCategory create(Info<BasinRecipe> info, MixingType type) {
		return new BlazeMixingCategoryImpl(info, type);
	}
}
