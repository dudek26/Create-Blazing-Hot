package com.dudko.blazinghot.compat.rei;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.Create;
import com.simibubi.create.compat.rei.display.BasinDisplay;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class BlazingBasinDisplay extends BasinDisplay {

    public BlazingBasinDisplay(BasinRecipe recipe, CategoryIdentifier<CreateDisplay<BasinRecipe>> id) {
        super(recipe, id);
    }

    public static BasinDisplay mixing(BasinRecipe recipe) {
        return new BasinDisplay(recipe, CategoryIdentifier.of(BlazingHot.asResource("mixing")));
    }

    public static BasinDisplay autoShapeless(BasinRecipe recipe) {
        return new BasinDisplay(recipe, CategoryIdentifier.of(BlazingHot.asResource("automatic_shapeless")));
    }

    public static BasinDisplay brewing(BasinRecipe recipe) {
        return new BasinDisplay(recipe, CategoryIdentifier.of(BlazingHot.asResource("automatic_brewing")));
    }

    public static BasinDisplay packing(BasinRecipe recipe) {
        return new BasinDisplay(recipe, CategoryIdentifier.of(BlazingHot.asResource("packing")));
    }

    public static BasinDisplay autoSquare(BasinRecipe recipe) {
        return new BasinDisplay(recipe, CategoryIdentifier.of(BlazingHot.asResource("automatic_packing")));
    }
}
