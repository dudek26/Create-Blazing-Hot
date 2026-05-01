package com.dudko.blazinghot.compat.jei.category;

import java.util.List;

import com.dudko.blazinghot.compat.jei.info.JEIBlazeMixerFuelRecipe;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Items;

public abstract class BlazeMixerFuelCategory extends AbstractRecipeCategory<JEIBlazeMixerFuelRecipe> {

	private static final int recipeWidth = 170;
	private static final int recipeHeight = 29;

	protected BlazeMixerFuelCategory() {
		super(JEIBlazeMixerFuelRecipe.TYPE,
			BlazingLang.BLAZE_MIXER_FUEL_CATEGORY.get(),
			new DoubleItemIcon(Items.LAVA_BUCKET::getDefaultInstance,
				BlazingBlocks.BLAZE_MIXER::asStack),
			recipeWidth,
			recipeHeight);
	}

	@ExpectPlatform
	public static BlazeMixerFuelCategory create() {
		throw new AssertionError();
	}

	protected abstract List<JEIBlazeMixerFuelRecipe> getRecipes();

	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(JEIBlazeMixerFuelRecipe.TYPE, getRecipes());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JEIBlazeMixerFuelRecipe recipe, IFocusGroup focuses) {
		IRecipeSlotBuilder
			fluidInputSlot =
			builder.addInputSlot(6, 6).setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1);
		
		fluidInputSlot.addFluidStack(recipe.fluid());
	}

	@Override
	public void draw(JEIBlazeMixerFuelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		graphics.drawString(Minecraft.getInstance().font, BlazingLang.BLAZE_MIXER_FUEL_SPEED.get((int) (recipe.speed() * 100)), 32, 3, ChatFormatting.YELLOW.getColor());
		graphics.drawString(Minecraft.getInstance().font, BlazingLang.BLAZE_MIXER_FUEL_USAGE.get((int) (recipe.usage() * 100)), 32, 16, ChatFormatting.GOLD.getColor());
	}

	public void registerCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(BlazingBlocks.BLAZE_MIXER, JEIBlazeMixerFuelRecipe.TYPE);
	}

}
