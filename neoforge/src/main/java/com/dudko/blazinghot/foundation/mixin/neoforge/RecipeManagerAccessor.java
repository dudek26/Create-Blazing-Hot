package com.dudko.blazinghot.foundation.mixin.neoforge;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {

	@Accessor
	Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> getRecipes();

}
