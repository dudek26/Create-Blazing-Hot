package com.dudko.blazinghot.content.casting.casting_depot;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CastingRecipe extends StandardProcessingRecipe<SingleRecipeInput> {

	public static final int SAFE_RENDERING_LIMIT = 10;

	protected int coolingDuration;
	protected boolean keepItem;

	public CastingRecipe(ProcessingRecipeParams params) {
		super(BlazingRecipeTypes.CASTING, params);
		coolingDuration = 0;
		keepItem = false;
	}

	public boolean isKeepItem() {
		return keepItem;
	}

	public int getCoolingDuration() {
		return coolingDuration;
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level level) {
		return ingredients.getFirst().test(input.getItem(0));
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
		if (fluidIngredients.isEmpty()) throw new IllegalStateException("Casting Recipe has no fluid ingredient!");
		return fluidIngredients.getFirst();
	}
	
	// TODO: finish this
	public static class Serializer extends StandardProcessingRecipe.Serializer<CastingRecipe> {

		public Serializer(Factory<CastingRecipe> factory) {
			super(factory);
		}

		@Override
		public Factory<CastingRecipe> factory() {
			return super.factory();
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CastingRecipe> streamCodec() {
			return super.streamCodec();
		}

		@Override
		public MapCodec<CastingRecipe> codec() {
			return super.codec();
		}
	}


}
