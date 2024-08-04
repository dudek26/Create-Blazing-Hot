package com.dudko.blazinghot.mixin.fabric.accessor;

import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BasinOperatingBlockEntity.class)
public interface BasinOperatingBlockEntityAccessor {

    @Accessor
    Recipe<?> getCurrentRecipe();

}
