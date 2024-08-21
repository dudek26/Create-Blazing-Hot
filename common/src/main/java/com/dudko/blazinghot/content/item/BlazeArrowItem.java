package com.dudko.blazinghot.content.item;

import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import com.dudko.blazinghot.registry.BlazingEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlazeArrowItem extends ArrowItem {
    public BlazeArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        return new BlazeArrowEntity(BlazingEntities.BLAZE_ARROW.get(), shooter, level);
    }
}
