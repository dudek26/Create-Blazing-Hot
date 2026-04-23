package com.dudko.blazinghot.compat.jei.category;

import com.dudko.blazinghot.compat.jei.info.JEIMetalInteractionRecipe;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.simibubi.create.compat.jei.ItemIcon;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.world.item.Items;

public class MetalInteractionsCategory extends AbstractRecipeCategory<JEIMetalInteractionRecipe> {
	private static final int recipeWidth = 170;
	private static final int recipeHeight = 125;

	public MetalInteractionsCategory() {
		super(JEIMetalInteractionRecipe.TYPE, BlazingLang.METAL_INTERACTION.get(), new ItemIcon(Items.COBBLESTONE::getDefaultInstance), recipeWidth, recipeHeight);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JEIMetalInteractionRecipe recipe, IFocusGroup focuses) {

		IRecipeSlotBuilder metalInputSlotBuilder = builder.addInputSlot(1, 1)
				.setStandardSlotBackground();
		IRecipeSlotBuilder fluidInputSlotBuilder = builder.addInputSlot(25, 1).setStandardSlotBackground();

		IIngredientAcceptor<?> outputSlotBuilder = builder.addOutputSlot(60, 1);

		metalInputSlotBuilder.addFluidStack(recipe.metal().getFluid());
		fluidInputSlotBuilder.addFluidStack(recipe.fluid().get());
	}
}
