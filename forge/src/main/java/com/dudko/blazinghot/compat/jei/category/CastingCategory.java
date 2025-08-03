package com.dudko.blazinghot.compat.jei.category;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.compat.jei.category.animations.AnimatedCastingSpout;
import com.dudko.blazinghot.content.casting.casting_depot.forge.CastingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CastingCategory extends CreateRecipeCategory<CastingRecipe> {

	private final AnimatedCastingSpout spout = new AnimatedCastingSpout();

	public CastingCategory(Info<CastingRecipe> info) {
		super(info);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CastingRecipe recipe, IFocusGroup focuses) {
		builder
				.addSlot(RecipeIngredientRole.INPUT, 27, 51)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(recipe.getIngredients().get(0));

		addFluidSlot(builder, 27, 32, recipe.getRequiredFluid());

		builder
				.addSlot(RecipeIngredientRole.OUTPUT, 132, 51)
				.setBackground(getRenderedSlot(), -1, -1)
				.addItemStack(getResultItem(recipe));
	}

	@Override
	public void draw(CastingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		AllGuiTextures.JEI_SHADOW.render(graphics, 62, 57);
		AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 126, 29);
		spout
				.withFluids(recipe.getRequiredFluid().getMatchingFluidStacks())
				.draw(graphics, getBackground().getWidth() / 2 - 13, 22);
	}
}
