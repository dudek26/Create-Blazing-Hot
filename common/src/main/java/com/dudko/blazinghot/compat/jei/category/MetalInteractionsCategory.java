package com.dudko.blazinghot.compat.jei.category;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.compat.jei.info.JEIMetalInteractionRecipe;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.gui.BlazingGuiTextures;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.createmod.catnip.data.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class MetalInteractionsCategory extends AbstractRecipeCategory<JEIMetalInteractionRecipe> {
	private static final int recipeWidth = 170;
	private static final int recipeHeight = 18;

	public MetalInteractionsCategory() {
		super(JEIMetalInteractionRecipe.TYPE,
				BlazingLang.METAL_INTERACTION.get(),
				new DoubleItemIcon(() -> AllPaletteStoneTypes.OCHRUM.getBaseBlock().get().asItem().getDefaultInstance(),
						() -> BlazingMetals.GOLD.getBucket().getDefaultInstance()),
				recipeWidth,
				recipeHeight);
	}

	private List<JEIMetalInteractionRecipe> getRecipes() {
		List<JEIMetalInteractionRecipe> recipes = new ArrayList<>();
		BlazingMetals.ALL.forEach(metal -> metal.fluidInteractions.forEach((fluid, results) -> {
			JEIMetalInteractionRecipe recipe = new JEIMetalInteractionRecipe(metal, fluid, results);
			recipes.add(recipe);
		}));
		return recipes;
	}

	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(JEIMetalInteractionRecipe.TYPE, getRecipes());
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JEIMetalInteractionRecipe recipe, IFocusGroup focuses) {

		IRecipeSlotBuilder
				metalInputSlotBuilder =
				builder.addInputSlot(3, 1).setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1);
		IRecipeSlotBuilder
				fluidInputSlotBuilder =
				builder.addInputSlot(41, 1).setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1);

		int outputPos = 112;

		metalInputSlotBuilder.addFluidStack(recipe.metal().getFluid());
		fluidInputSlotBuilder.addFluidStack(recipe.fluid().get());

		for (Pair<NonNullSupplier<Block>, Double> result : recipe.results()) {
			Block block = result.getFirst().get();
			ItemStack stack = block.asItem().getDefaultInstance();
			double chance = result.getSecond();

			IRecipeSlotBuilder
					outputSlotBuilder =
					builder
							.addOutputSlot(outputPos, 1)
							.setBackground(CreateRecipeCategory.getRenderedSlot((float) chance), -1, -1);
			outputPos += 20;

			if (chance < 1) {
				outputSlotBuilder.addRichTooltipCallback((v, t) -> t.add(CreateLang
						.translateDirect("recipe.processing.chance", chance < 0.01 ? "<1" : (int) (chance * 100))
						.withStyle(ChatFormatting.GOLD)));
			}

			outputSlotBuilder.addItemStack(stack);
		}
	}

	@Override
	public void draw(JEIMetalInteractionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		AllGuiTextures.JEI_ARROW.render(guiGraphics, (recipeWidth - AllGuiTextures.JEI_ARROW.getWidth()) / 2, 5);
		BlazingGuiTextures.JEI_PLUS.render(guiGraphics, 25, 5);
	}
}
