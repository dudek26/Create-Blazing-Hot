package com.dudko.blazinghot.compat.jei.category;

import java.util.List;

import com.dudko.blazinghot.compat.jei.category.animations.AnimatedCastingSpout;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.material.FluidState;

public class CastingCategory extends CreateRecipeCategory<CastingRecipe> {

	protected final AnimatedCastingSpout spout = new AnimatedCastingSpout();

	public CastingCategory(Info<CastingRecipe> info) {
		super(info);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CastingRecipe recipe, IFocusGroup focuses) {
		IRecipeSlotBuilder
				ingredientSlot =
				builder
						.addSlot(RecipeIngredientRole.INPUT, 27, 51)
						.setBackground(getRenderedSlot(), -1, -1)
						.addIngredients(recipe.getIngredients().getFirst());

		if (!recipe.isKeepItem()) ingredientSlot.addRichTooltipCallback((v, t) -> t.add(BlazingLang.MOLD_CONSUMED
				.get()
				.withStyle(ChatFormatting.RED)));

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
		spout.withFluids(getRequiredFluids(recipe)).draw(graphics, getBackground().getWidth() / 2 - 13, 22);
	}

	@ExpectPlatform
	public static List<FluidState> getRequiredFluids(CastingRecipe recipe) {
		throw new AssertionError();
	}
}
