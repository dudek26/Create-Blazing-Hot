package com.dudko.blazinghot.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.foundation.recipe.BlazingRecipeType;
import com.mojang.serialization.Codec;
import com.simibubi.create.AllRecipeTypes;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * @see AllRecipeTypes
 */
public class BlazingRecipeTypes {

	public static final List<BlazingRecipeType> ALL = new ArrayList<>();

	public static final BlazingRecipeType
		BLAZE_MIXING =
		BlazingRecipeType.create("blaze_mixing", BlazeMixingRecipe::new).register(),
		CASTING =
			BlazingRecipeType
				.create("casting", () -> new CastingRecipe.Serializer(CastingRecipe::new))
				.register();

	public static final Predicate<RecipeHolder<?>> CAN_BE_BLAZE_MIXED = r -> !r.id().getPath().endsWith("_mixer_only");

	public static boolean shouldAllowBlazeMixing(RecipeHolder<?> recipe) {
		return CAN_BE_BLAZE_MIXED.test(recipe);
	}

	public static final Codec<BlazingRecipeType>
		CODEC =
		StringRepresentable.fromValues(() -> ALL.toArray(new BlazingRecipeType[]{}));

	public static void register() {

	}


}
