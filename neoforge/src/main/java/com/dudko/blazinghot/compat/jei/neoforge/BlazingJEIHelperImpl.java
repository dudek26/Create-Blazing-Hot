package com.dudko.blazinghot.compat.jei.neoforge;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.dudko.blazinghot.compat.jei.BlazingJEIHelper;
import com.simibubi.create.AllFluids;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

/**
 * @see BlazingJEIHelper
 */
public class BlazingJEIHelperImpl {

	@SuppressWarnings("removal")
	public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, SizedFluidIngredient ingredient) {
		return builder
				.addSlot(ingredientRole, x, y)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(ingredient.getFluids()).toList())
				.setFluidRenderer(ingredient.amount(), false, 16, 16) // make fluid take up the full slot
				.addTooltipCallback(BlazingJEIHelperImpl::addPotionTooltip);
	}

	/**
	 * from {@link CreateRecipeCategory CreateRecipeCategory::addPotionTooltip}
	 */
	protected static void addPotionTooltip(IRecipeSlotView view, List<Component> tooltip) {
		Optional<FluidStack> displayed = view.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
		if (displayed.isEmpty()) return;

		FluidStack fluidStack = displayed.get();

		if (fluidStack.getFluid().isSame(AllFluids.POTION.get())) {
			List<Component> potionTooltip = new ArrayList<>();
			PotionFluidHandler.addPotionTooltip(fluidStack, potionTooltip::add, 1);
			// append after item name
			tooltip.addAll(1, potionTooltip.stream().toList());
		}
	}

}
