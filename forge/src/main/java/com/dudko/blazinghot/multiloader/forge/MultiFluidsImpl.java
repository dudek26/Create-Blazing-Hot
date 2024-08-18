package com.dudko.blazinghot.multiloader.forge;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class MultiFluidsImpl {
    public static long platformedAmount(long droplets) {
        return droplets / 81;
    }

    public static String platformedName() {
        return "milibuckets";
    }

    public static String conversionNote() {
        return "";
    }

    public static boolean recipeResultContains(ProcessingRecipe<?> r, TagKey<Fluid> fluid) {
        return r.getFluidResults().stream().anyMatch(fs -> fs.getFluid().defaultFluidState()
                .is(MoltenMetal.GOLD.fluidTag()));
    }
}
