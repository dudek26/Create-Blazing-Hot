package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeType;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllRecipeTypes;

import net.minecraft.util.StringRepresentable;

public class BlazingRecipeTypes {

	public static final Codec<AllRecipeTypes> CODEC = StringRepresentable.fromEnum(AllRecipeTypes::values);

	public static final BlazingRecipeType
			BLAZE_MIXING =
			BlazingRecipeType.create("blaze_mixing", BlazeMixingRecipe::new),
			CASTING =
					BlazingRecipeType.create("casting", CastingRecipe::new);

	public static void register() {

	}


}
