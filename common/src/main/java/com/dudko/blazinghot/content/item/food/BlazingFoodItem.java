package com.dudko.blazinghot.content.item.food;

import static com.dudko.blazinghot.util.TooltipUtil.addEffectTooltip;

import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.registry.BlazingConfigs;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazingFoodItem extends Item {

	private boolean effectTooltip = true;
	private final BiConsumer<Level, LivingEntity> onUse;

	public BlazingFoodItem(Properties properties, BiConsumer<Level, LivingEntity> onUse) {
		super(properties);
		this.onUse = onUse;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		onUse.accept(level, livingEntity);
		return super.finishUsingItem(stack, level, livingEntity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		if (effectTooltip && BlazingConfigs.client().foodTooltips.get()) {
			FoodProperties foodProperties = components().get(DataComponents.FOOD);
			if (foodProperties != null) foodProperties
					.effects()
					.stream()
					.map(FoodProperties.PossibleEffect::effect)
					.forEach(mobEffectInstance -> addEffectTooltip(tooltipComponents, context, mobEffectInstance));
		}
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
	}

}
