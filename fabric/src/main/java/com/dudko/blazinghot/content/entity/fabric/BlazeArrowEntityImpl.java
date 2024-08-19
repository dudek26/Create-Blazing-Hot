package com.dudko.blazinghot.content.entity.fabric;

import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BlazeArrowEntityImpl extends BlazeArrowEntity {


    public BlazeArrowEntityImpl(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BlazeArrowEntityImpl(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level, Item referenceItem) {
        super(entityType, shooter, level);
    }

    public static BlazeArrowEntity create(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level, Item referenceItem) {
        return new BlazeArrowEntityImpl(entityType, shooter, level, referenceItem);
    }

    public static BlazeArrowEntity create(EntityType<? extends AbstractArrow> entityType, Level level) {
        return new BlazeArrowEntityImpl(entityType, level);
    }
}
