package com.dudko.blazinghot.compat.jei.forge;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.gui.BlazingGuiTextures;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.fluids.FluidStack;

public class BlazeMixingCategory extends BasinCategory {

	private final AnimatedBlazeMixer mixer = new AnimatedBlazeMixer();
	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
	MixingType type;

	protected enum MixingType {
		MIXING,
		AUTO_SHAPELESS,
		AUTO_BREWING
	}

	public static BlazeMixingCategory standard(Info<BasinRecipe> info) {
		return new BlazeMixingCategory(info, MixingType.MIXING);
	}

	public static BlazeMixingCategory autoShapeless(Info<BasinRecipe> info) {
		return new BlazeMixingCategory(info, MixingType.AUTO_SHAPELESS);
	}

	public static BlazeMixingCategory autoBrewing(Info<BasinRecipe> info) {
		return new BlazeMixingCategory(info, MixingType.AUTO_BREWING);
	}

	protected BlazeMixingCategory(Info<BasinRecipe> info, MixingType type) {
		super(info, type != MixingType.AUTO_SHAPELESS);
		this.type = type;
	}

	private FluidIngredient getFuelFromRecipe(BasinRecipe recipe) {
		if (type == MixingType.AUTO_SHAPELESS) return FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag,
				BlazingConfigs.server().blazeShapelessFuelUsage.get());
		if (recipe instanceof BlazeMixingRecipe bmRecipe) return bmRecipe.getFuelFluid();
		else {
			int calculatedCost = (int) BlazeMixingRecipe.getFuelCost(recipe);
			return calculatedCost > 0 ?
				   FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, calculatedCost) :
				   FluidIngredient.EMPTY;
		}
	}

	private boolean includeFuel(BasinRecipe recipe) {
		FluidIngredient fuelFluid = getFuelFromRecipe(recipe);

		List<FluidStack> fuels;
		if (fuelFluid == FluidIngredient.EMPTY) fuels = new ArrayList<>();
		else fuels = new ArrayList<>(fuelFluid.getMatchingFluidStacks());
		return (!fuels.isEmpty() && !fuels.get(0).isEmpty() && fuels.get(0) != null) || (type
				== MixingType.AUTO_SHAPELESS && BlazingConfigs.server().blazeShapelessFuelUsage.get() != 0);
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BasinRecipe recipe, @NotNull IFocusGroup focuses) {
		super.setRecipe(builder, recipe, focuses);

		FluidIngredient fuelFluid = getFuelFromRecipe(recipe);

		List<FluidStack> fuels;
		if (fuelFluid == FluidIngredient.EMPTY) fuels = new ArrayList<>();
		else fuels = new ArrayList<>(fuelFluid.getMatchingFluidStacks());

		int vRows = (1 + recipe.getFluidResults().size() + recipe.getRollableResults().size()) / 2;

		if (includeFuel(recipe)) builder
				.addSlot(RecipeIngredientRole.INPUT, 142, 11 - (19 * (vRows - 1)))
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fuels))
				.addTooltipCallback(addFluidTooltip(fuelFluid.getRequiredAmount()))
				.addTooltipCallback((v, t) -> t.add(BlazingLang.BLAZE_MIXER_FUEL
						.get()
						.withStyle(ChatFormatting.DARK_GREEN)));

	}

	@Override
	public void draw(@NotNull BasinRecipe recipe, @NotNull IRecipeSlotsView iRecipeSlotsView, @NotNull GuiGraphics graphics, double mouseX, double mouseY) {
		super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);

		if (includeFuel(recipe)) {
			int vRows = (1 + recipe.getFluidResults().size() + recipe.getRollableResults().size()) / 2;
			BlazingGuiTextures.JEI_SHORT_ARROW_LEFT.render(graphics, 124, 16 - 19 * (vRows - 1));
		}

		HeatCondition requiredHeat = recipe.getRequiredHeat();
		if (requiredHeat != HeatCondition.NONE) heater
				.withHeat(requiredHeat.visualizeAsBlazeBurner())
				.draw(graphics, getBackground().getWidth() / 2 + 3, 55);
		mixer.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
	}
}
