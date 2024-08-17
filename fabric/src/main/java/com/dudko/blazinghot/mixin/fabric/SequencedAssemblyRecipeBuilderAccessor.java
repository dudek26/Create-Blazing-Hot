package com.dudko.blazinghot.mixin.fabric;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SequencedAssemblyRecipeBuilder.class, remap = false)
public interface SequencedAssemblyRecipeBuilderAccessor {

    @Accessor
    SequencedAssemblyRecipe getRecipe();

}
