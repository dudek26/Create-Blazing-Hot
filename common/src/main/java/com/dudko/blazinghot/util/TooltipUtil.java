package com.dudko.blazinghot.util;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class TooltipUtil {

	public static void addEffectTooltip(List<Component> lines, MobEffectInstance effect) {
		Component
				amplifier =
				effect.getAmplifier() == 0 ?
				Component.empty() :
				Component.translatable("potion.potency." + effect.getAmplifier()).append(" ");
		lines.add(Component
				.translatable(effect.getDescriptionId())
				.append(" ")
				.append(amplifier)
				.append("(")
				.append(MobEffectUtil.formatDuration(effect, 1))
				.append(")")
				.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
	}

	public static MutableComponent asciiProgressBar(int length, float value, float max) {
		if (max == 0) return Component.empty();
		float percent = Math.max(0, value) / max;
		int filled = Math.round(length * percent);
		int empty = length - filled;
		Component filledBar = Component.literal("█".repeat(filled)).withStyle(ChatFormatting.GRAY);
		Component emptyBar = Component.literal("█".repeat(empty)).withStyle(ChatFormatting.DARK_GRAY);
		return Component
				.literal("[")
				.withStyle(ChatFormatting.GRAY)
				.append(filledBar)
				.append(emptyBar)
				.append("]")
				.withStyle(ChatFormatting.GRAY);
	}
}
