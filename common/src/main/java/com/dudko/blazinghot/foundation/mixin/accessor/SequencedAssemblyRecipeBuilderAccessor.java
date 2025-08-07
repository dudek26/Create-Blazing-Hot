package com.dudko.blazinghot.foundation.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;

@Mixin(SequencedAssemblyRecipeBuilder.class)
public interface SequencedAssemblyRecipeBuilderAccessor {

	@Accessor(remap = false)
	SequencedAssemblyRecipe getRecipe();

}
