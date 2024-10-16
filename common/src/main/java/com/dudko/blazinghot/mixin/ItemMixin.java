package com.dudko.blazinghot.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.util.TooltipUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
	public void blazinghot$tooltipInjector(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
		Item blazinghot$self = (Item) (Object) this;
		if (blazinghot$isEligibleItem() && blazinghot$self.getFoodProperties() != null) {
			blazinghot$self
					.getFoodProperties()
					.getEffects()
					.forEach(effect -> TooltipUtil.addEffectTooltip(tooltipComponents, effect.getFirst()));
		}
	}

}
