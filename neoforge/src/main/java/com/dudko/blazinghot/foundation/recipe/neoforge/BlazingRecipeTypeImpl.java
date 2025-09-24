package com.dudko.blazinghot.foundation.recipe.neoforge;

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.foundation.recipe.BlazingRecipeType;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * From {@link AllRecipeTypes}
 */
public class BlazingRecipeTypeImpl extends BlazingRecipeType {

	private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
	@Nullable
	private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
	private final Supplier<RecipeType<?>> type;

	protected BlazingRecipeTypeImpl(String id, Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		super(id, serializerSupplier, typeSupplier, registerType);
		serializerObject = Registers.SERIALIZER_REGISTER.register(id, serializerSupplier);
		if (registerType) {
			typeObject = Registers.TYPE_REGISTER.register(id, typeSupplier);
			type = typeObject;
		}
		else {
			typeObject = null;
			type = typeSupplier;
		}
	}

	protected BlazingRecipeTypeImpl(String id, Supplier<RecipeSerializer<?>> serializerSupplier) {
		super(id, serializerSupplier);
		serializerObject = Registers.SERIALIZER_REGISTER.register(id, serializerSupplier);
		typeObject = Registers.TYPE_REGISTER.register(id, () -> RecipeType.simple(this.getId()));
		type = typeObject;
	}

	protected BlazingRecipeTypeImpl(String id, StandardProcessingRecipe.Factory<?> processingFactory) {
		this(id, () -> new StandardProcessingRecipe.Serializer<>(processingFactory));
		isProcessingRecipe = true;
	}

//	protected BlazingRecipeTypeImpl(String id, BlazeMixingRecipe.Factory blazeMixingFactory) {
//		this(id, () -> new BlazeMixingRecipe.Serializer(blazeMixingFactory));
//		isProcessingRecipe = true;
//	}

	public static BlazingRecipeType create(String id, Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		return new BlazingRecipeTypeImpl(id, serializerSupplier, typeSupplier, registerType);
	}

	public static BlazingRecipeType create(String id, Supplier<RecipeSerializer<?>> serializerSupplier) {
		return new BlazingRecipeTypeImpl(id, serializerSupplier);
	}

	public static BlazingRecipeType create(String id, StandardProcessingRecipe.Factory<?> processingFactory) {
		return new BlazingRecipeTypeImpl(id, processingFactory);
	}

//	public static BlazingRecipeType blazeMixing(String id, BlazeMixingRecipe.Factory blazeMixingFactory) {
//		return new BlazingRecipeTypeImpl(id, blazeMixingFactory);
//	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
		return (RecipeType<R>) type.get();
	}

	@ApiStatus.Internal
	public static void registerAll(IEventBus modEventBus) {
		ShapedRecipePattern.setCraftingSize(9, 9);
		Registers.SERIALIZER_REGISTER.register(modEventBus);
		Registers.TYPE_REGISTER.register(modEventBus);
	}

	private static class Registers {
		private static final DeferredRegister<RecipeSerializer<?>>
				SERIALIZER_REGISTER =
				DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Create.ID);
		private static final DeferredRegister<RecipeType<?>>
				TYPE_REGISTER =
				DeferredRegister.create(Registries.RECIPE_TYPE, Create.ID);
	}
}
