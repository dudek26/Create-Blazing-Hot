package com.dudko.blazinghot.content.casting.casting_depot.forge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CastingRecipe extends ProcessingRecipe<RecipeWrapper> {

	protected int coolingDuration;

	@Override
	public boolean matches(RecipeWrapper container, Level level) {
		return ingredients.get(0).test(container.getItem(0));
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 1;
	}

	@Override
	protected int getMaxFluidInputCount() {
		return 1;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	public FluidIngredient getRequiredFluid() {
		if (fluidIngredients.isEmpty())
			throw new IllegalStateException("Casting Recipe: " + id.toString() + " has no fluid ingredient!");
		return fluidIngredients.get(0);
	}

	public CastingRecipe(ProcessingRecipeParams params) {
		super(BlazingRecipeTypes.CASTING.get(), params);
	}

	@Override
	public void writeAdditional(JsonObject json) {
		json.addProperty("coolingDuration", coolingDuration);
	}

	@Override
	public void writeAdditional(FriendlyByteBuf buffer) {
		super.writeAdditional(buffer);
		buffer.writeVarInt(coolingDuration);
	}

	@Override
	public void readAdditional(JsonObject json) {
		if (!json.has("coolingDuration")) coolingDuration = json.get("coolingDuration").getAsInt();
		else coolingDuration = 0;
		super.readAdditional(json);
	}

	@Override
	public void readAdditional(FriendlyByteBuf buffer) {
		super.readAdditional(buffer);
		coolingDuration = buffer.readVarInt();
	}
}
