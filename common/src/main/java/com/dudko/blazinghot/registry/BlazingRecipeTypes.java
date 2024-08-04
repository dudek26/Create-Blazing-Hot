package com.dudko.blazinghot.registry;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.crafting.RecipeType;

// TODO - replace this when porting to NeoForge in 1.21
public enum BlazingRecipeTypes {
    BLAZE_MIXING()
    ;

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

    public static void register() {

    }


}
