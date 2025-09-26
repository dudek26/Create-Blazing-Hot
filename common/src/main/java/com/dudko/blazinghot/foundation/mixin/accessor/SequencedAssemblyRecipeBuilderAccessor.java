package com.dudko.blazinghot.foundation.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;

@Mixin(value = SequencedAssemblyRecipeBuilder.class, remap = false)
public interface SequencedAssemblyRecipeBuilderAccessor {

	@Accessor("recipe")
	SequencedAssemblyRecipe blazinghot$getRecipe();

}
