package com.dudko.blazinghot.content.item;

import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import com.dudko.blazinghot.registry.BlazingEntityTypes;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.content.contraptions.ContraptionWorld;
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
        BlazeArrowEntity
                arrow =
                new BlazeArrowEntity(BlazingEntityTypes.BLAZE_ARROW.get(), shooter, level,
                                     BlazingItems.BLAZE_ARROW.get());
        if (level.dimension() == ContraptionWorld.NETHER) arrow.setBaseDamage(arrow.getBaseDamage() * 1.5);

        return arrow;
    }
}
