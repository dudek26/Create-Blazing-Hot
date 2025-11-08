package com.dudko.blazinghot.content.casting.casting_depot.recipe;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CastingRecipe extends ProcessingRecipe<SingleRecipeInput, CastingRecipeParams> {

	public static final int SAFE_RENDERING_LIMIT = 10;

	public CastingRecipe(CastingRecipeParams params) {
		super(BlazingRecipeTypes.CASTING, params);
	}

	public boolean isKeepItem() {
		return params.keepMold;
	}

	public int getCoolingDuration() {
		return params.coolingDuration;
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

	@Override
	public List<String> validate() {
		List<String> errors = super.validate();
		if (fluidIngredients.isEmpty()) errors.add("Recipe has no fluid ingredient!");
		return errors;
	}

	public SizedFluidIngredient getRequiredFluid() {
		return fluidIngredients.getFirst();
	}

	public static class Serializer implements RecipeSerializer<CastingRecipe> {
		private final ProcessingRecipe.Factory<CastingRecipeParams, CastingRecipe> factory;
		private final MapCodec<CastingRecipe> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, CastingRecipe> streamCodec;

		public Serializer(ProcessingRecipe.Factory<CastingRecipeParams, CastingRecipe> factory) {
			this.factory = factory;
			this.codec = ProcessingRecipe.codec(factory, CastingRecipeParams.CODEC);
			this.streamCodec = ProcessingRecipe.streamCodec(factory, CastingRecipeParams.STREAM_CODEC);
		}

		@Override
		public MapCodec<CastingRecipe> codec() {
			return codec;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CastingRecipe> streamCodec() {
			return streamCodec;
		}

		public ProcessingRecipe.Factory<CastingRecipeParams, CastingRecipe> factory() {
			return factory;
		}
	}


}
