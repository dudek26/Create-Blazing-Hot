package com.dudko.blazinghot.foundation.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.util.TooltipUtil;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

@Mixin(Item.class)
public abstract class ItemMixin {

	@Unique
	private boolean blazinghot$isEligibleItem() {
		if (!BlazingConfigs.client().vanillaAppleTooltips.get()) return false;
		Item blazinghot$self = (Item) (Object) this;
		List<Item> blazinghot$targetTooltipItems = List.of(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
		return blazinghot$targetTooltipItems.contains(blazinghot$self);
	}

	@Inject(method = "appendHoverText", at = @At("RETURN"))
	public void blazinghot$tooltipInjector(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
		Item blazinghot$self = (Item) (Object) this;
		FoodProperties foodProperties = blazinghot$self.components().get(DataComponents.FOOD);
		if (blazinghot$isEligibleItem() && foodProperties != null) {
			foodProperties
					.effects()
					.forEach(effect -> TooltipUtil.addEffectTooltip(tooltipComponents, context, effect.effect()));
		}
	}

}
