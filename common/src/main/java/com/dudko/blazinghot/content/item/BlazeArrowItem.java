package com.dudko.blazinghot.content.item;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import com.dudko.blazinghot.registry.BlazingEntities;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeArrowItem extends ArrowItem {

	public BlazeArrowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
		return new BlazeArrowEntity(BlazingEntities.BLAZE_ARROW.get(), shooter, shooter.level(), ammo, weapon);
	}
}
