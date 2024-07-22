package com.dudko.blazinghot.content.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ExtinguishingItem extends FoilableItem {

    public ExtinguishingItem(Properties properties) {
        super(properties, false);
    }

    public ExtinguishingItem(Properties properties, boolean foil) {
        super(properties, foil);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, LivingEntity livingEntity) {
        livingEntity.extinguishFire();
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
