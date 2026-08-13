package com.dudko.blazinghot.compat.jei.category;

import com.dudko.blazinghot.compat.jei.category.animations.AnimatedBlazeMixer;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity.MixingType;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.gui.BlazingGuiTextures;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class BlazeMixingCategory extends BasinCategory {

	private final AnimatedBlazeMixer mixer;
	private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
	protected MixingType type;

	protected BlazeMixingCategory(Info<BasinRecipe> info, MixingType type) {
		super(info, type != BlazeMixerBlockEntity.MixingType.AUTO_SHAPELESS);
		this.type = type;
		this.mixer = new AnimatedBlazeMixer(type == BlazeMixerBlockEntity.MixingType.BLAZE_MIXING);
	}

	public static BlazeMixingCategory fueled(Info<BasinRecipe> info) {
		return create(info, BlazeMixerBlockEntity.MixingType.MIXING);
	}

	public static BlazeMixingCategory autoShapeless(Info<BasinRecipe> info) {
		return create(info, BlazeMixerBlockEntity.MixingType.AUTO_SHAPELESS);
	}

	public static BlazeMixingCategory autoBrewing(Info<BasinRecipe> info) {
		return create(info, BlazeMixerBlockEntity.MixingType.AUTO_BREWING);
	}

	public static BlazeMixingCategory blazeMixing(Info<BasinRecipe> info) {
		return create(info, BlazeMixerBlockEntity.MixingType.BLAZE_MIXING);
	}

	@ExpectPlatform
	public static BlazeMixingCategory create(Info<BasinRecipe> info, MixingType type) {
		throw new AssertionError();
	}

	protected long getFuelAmount(BasinRecipe recipe) {
		if (type == BlazeMixerBlockEntity.MixingType.AUTO_SHAPELESS)
			return BlazingConfigs.server().recipes.fueledShapelessFuelUsage.get();
		if (recipe instanceof BlazeMixingRecipe bmRecipe) return bmRecipe.getLegacyMixerFuelAmount();

		assert Minecraft.getInstance().level != null;
		return BlazeMixingRecipe.getFuelCost(recipe, Minecraft.getInstance().level);
	}

	@ExpectPlatform
	public static int getFluidResultsSize(BasinRecipe recipe) {
		throw new AssertionError();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
		super.setRecipe(builder, recipe, focuses);

		long fuelAmount = getFuelAmount(recipe);

		int vRows = (1 + recipe.getFluidResults().size() + recipe.getRollableResults().size()) / 2;

		if (fuelAmount != 0) {
			IRecipeSlotBuilder fuelSlot = builder.addInputSlot(142, 11 - (19 * (vRows - 1)));
			BlazeMixerFuelCategory.RECIPES.forEach(fuel ->
				fuelSlot
					.addFluidStack(fuel.fluid(), fuel.data().calculateFuelUsage(type, fuelAmount))
					.setFluidRenderer(1, false, 16, 16)
					.setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
			);
			fuelSlot.addRichTooltipCallback(this::fuelTooltip);
		}
	}

	protected abstract void fuelTooltip(IRecipeSlotView view, ITooltipBuilder tooltip);

	@Override
	public void draw(BasinRecipe recipe,
	                 IRecipeSlotsView iRecipeSlotsView,
	                 GuiGraphics graphics,
	                 double mouseX,
	                 double mouseY) {
		super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);

		long fuelAmount = getFuelAmount(recipe);
		if (fuelAmount != 0) {
			int vRows = (1 + getFluidResultsSize(recipe) + recipe.getRollableResults().size()) / 2;
			BlazingGuiTextures.JEI_SHORT_ARROW_LEFT.render(graphics, 124, 16 - 19 * (vRows - 1));
		}

		HeatCondition requiredHeat = recipe.getRequiredHeat();
		if (requiredHeat != HeatCondition.NONE) heater
			.withHeat(requiredHeat.visualizeAsBlazeBurner())
			.draw(graphics, getBackground().getWidth() / 2 + 3, 55);
		mixer.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
	}

}
