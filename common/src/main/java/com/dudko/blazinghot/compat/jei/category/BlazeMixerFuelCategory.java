package com.dudko.blazinghot.compat.jei.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dudko.blazinghot.compat.jei.info.JEIBlazeMixerFuelRecipe;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity.MixingType;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.foundation.datamap.fuel.BlazeMixerFuelDataEntry;
import com.dudko.blazinghot.gui.BlazingGuiTextures;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.util.LangUtil;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public abstract class BlazeMixerFuelCategory extends AbstractRecipeCategory<JEIBlazeMixerFuelRecipe> {

	private static final int recipeWidth = 160;
	private static final int recipeHeight = 64;

	private static final int cellX = 30;
	private static final int cellY = 18;
	private static final int borders = 1;

	public static final List<JEIBlazeMixerFuelRecipe> RECIPES = new ArrayList<>();

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
		RECIPES.clear();
		RECIPES.addAll(getRecipes());
		registration.addRecipes(JEIBlazeMixerFuelRecipe.TYPE, RECIPES);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JEIBlazeMixerFuelRecipe recipe, IFocusGroup focuses) {
		IRecipeSlotBuilder
			fluidInputSlot = builder
			.addInputSlot(9, 5)
			.setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1);

		fluidInputSlot.addFluidStack(recipe.fluid());
	}

	@Override
	public void draw(JEIBlazeMixerFuelRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		int posX = 19 + 8;
		int posY = 24;
		BlazingGuiTextures.JEI_MIXER_FUEL_TABLE.render(graphics, 8, 4);
		for (MixingType mixingType : BlazeMixerBlockEntity.MixingType.values()) {
			BlazeMixerFuelDataEntry dataEntry = recipe.data().getForType(mixingType);
			Font font = Minecraft.getInstance().font;

			String speed = LangUtil.formattedPercentage(dataEntry.speed(), 0);
			String usage = LangUtil.formattedPercentage(dataEntry.usage(), 0);

			graphics.drawCenteredString(
				font,
				Component.literal(speed + "%"),
				posX + cellX / 2,
				posY + (cellY - font.lineHeight) / 2,
				ChatFormatting.YELLOW.getColor());

			if (mouseX > posX && mouseX < posX + cellX && mouseY > posY && mouseY < posY + cellY) {
				graphics.renderTooltip(font, List.of(mixingType.getComponent().withStyle(ChatFormatting.WHITE), BlazingLang.BLAZE_MIXER_FUEL_SPEED.get(speed).withStyle(ChatFormatting.YELLOW)), Optional.empty(), (int) mouseX + 4, (int) mouseY - 4);
			}

			graphics.drawCenteredString(
				font,
				Component.literal(usage + "%"),
				posX + cellX / 2,
				posY + cellY + borders + (cellY - font.lineHeight) / 2,
				ChatFormatting.YELLOW.getColor());

			if (mouseX > posX && mouseX < posX + cellX && mouseY > posY + cellY + borders && mouseY < posY + borders + 2 * cellY) {
				graphics.renderTooltip(font, List.of(mixingType.getComponent().withStyle(ChatFormatting.WHITE), BlazingLang.BLAZE_MIXER_FUEL_USAGE.get(usage).withStyle(ChatFormatting.YELLOW)), Optional.empty(), (int) mouseX + 4, (int) mouseY - 4);
			}

			posX += cellX + borders;
		}
	}


	public void registerCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(BlazingBlocks.BLAZE_MIXER, JEIBlazeMixerFuelRecipe.TYPE);
	}

}
