package com.dudko.blazinghot.compat.emi.fabric;

import static com.dudko.blazinghot.compat.emi.fabric.BlazingEmiAnimations.addBlazeMixer;
import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.getFuelCost;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.gui.BlazingGuiTextures;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.compat.emi.CreateEmiPlugin;
import com.simibubi.create.compat.emi.recipes.basin.BasinEmiRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

public class BlazeMixingEmiRecipe extends BasinEmiRecipe {

	protected List<EmiIngredient> fuels;

	public BlazeMixingEmiRecipe(EmiRecipeCategory category, BasinRecipe recipe) {
		super(category, recipe, category != CreateEmiPlugin.AUTOMATIC_SHAPELESS);
		FluidIngredient fuelFluid;
		if (recipe instanceof BlazeMixingRecipe bmRecipe) fuelFluid = bmRecipe.getFuelFluid();
		else {
			long calculatedCost = getFuelCost(recipe);
			fuelFluid =
					calculatedCost > 0 ?
					FluidIngredient.fromTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, calculatedCost) :
					FluidIngredient.EMPTY;

			ResourceLocation id = recipe.getId();
			this.id = new ResourceLocation("emi", "blazinghot/blaze_mixing/" + id.getNamespace() + "/" + id.getPath());
		}
		if (fuelFluid == FluidIngredient.EMPTY) this.fuels = new ArrayList<>();
		else this.fuels = List.of(firstFluidOrEmpty(fuelFluid.getMatchingFluidStacks()));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {

		int outputSize = output.size();
		int vRows = (1 + outputSize) / 2;

		if (!fuels.isEmpty() && !fuels.get(0).isEmpty() && fuels.get(0) != null) {
			addSlot(widgets, fuels.get(0), 140, 10 - ((vRows - 1) * 20)).appendTooltip(BlazingLang.BLAZE_MIXER_FUEL
					.get()
					.withStyle(ChatFormatting.DARK_GREEN));

			BlazingWidgets.addTexture(widgets, BlazingGuiTextures.JEI_SHORT_ARROW_LEFT, 122, 15 - ((vRows - 1) * 20));
		}

		super.addWidgets(widgets);

		HeatCondition requiredHeat = recipe.getRequiredHeat();
		if (requiredHeat != HeatCondition.NONE) {
			CreateEmiAnimations.addBlazeBurner(widgets,
					widgets.getWidth() / 2 + 3,
					55,
					requiredHeat.visualizeAsBlazeBurner());
		}
		addBlazeMixer(widgets, widgets.getWidth() / 2 + 3, 40);
	}

	@Override
	public List<EmiIngredient> getInputs() {
		List<EmiIngredient> inputs = new ArrayList<>(super.getInputs());
		inputs.addAll(fuels);
		return inputs;
	}
}
