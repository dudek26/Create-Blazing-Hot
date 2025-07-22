package com.dudko.blazinghot.registry;

import java.util.Optional;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

// TODO - replace this when porting to NeoForge in 1.21
public enum BlazingRecipeTypes {
	BLAZE_MIXING,
	CASTING;

	BlazingRecipeTypes() {
	}

	public <T extends RecipeType<?>> T getType() {
		return BlazingRecipeTypes.getType(this);
	}

	public IRecipeTypeInfo get() {
		return BlazingRecipeTypes.get(this);
	}

	@ExpectPlatform
	public static <T extends RecipeType<?>> T getType(BlazingRecipeTypes recipe) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static IRecipeTypeInfo get(BlazingRecipeTypes recipe) {
		throw new AssertionError();
	}

	public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
		return world.getRecipeManager().getRecipeFor(getType(), inv, world);
	}

	public static void register() {

	}


}
