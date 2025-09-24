package com.dudko.blazinghot.foundation.recipe;

import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * From {@link AllRecipeTypes}
 */
public abstract class BlazingRecipeType implements IRecipeTypeInfo, StringRepresentable {

	public final ResourceLocation id;
	public final Supplier<RecipeSerializer<?>> serializerSupplier;

	protected boolean isProcessingRecipe;

	protected BlazingRecipeType(String id, Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		this.id = BlazingHot.asResource(id);
		this.serializerSupplier = serializerSupplier;
		isProcessingRecipe = false;
	}

	protected BlazingRecipeType(String id, Supplier<RecipeSerializer<?>> serializerSupplier) {
		this.id = BlazingHot.asResource(id);
		this.serializerSupplier = serializerSupplier;
		isProcessingRecipe = false;
	}

	@ExpectPlatform
	public static BlazingRecipeType create(String id, Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static BlazingRecipeType create(String id, Supplier<RecipeSerializer<?>> serializerSupplier) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static BlazingRecipeType create(String id, StandardProcessingRecipe.Factory<?> processingFactory) {
		throw new AssertionError();
	}

//	@ExpectPlatform
//	public static BlazingRecipeType blazeMixing(String id, BlazeMixingRecipe.Factory blazeMixingFactory) {
//		throw new AssertionError();
//	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public BlazingRecipeType register() {
		BlazingRecipeTypes.ALL.add(this);
		return this;
	}

	public <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world) {
		return world.getRecipeManager().getRecipeFor(getType(), inv, world);
	}

	@Override
	public @NotNull String getSerializedName() {
		return id.toString();
	}

}
