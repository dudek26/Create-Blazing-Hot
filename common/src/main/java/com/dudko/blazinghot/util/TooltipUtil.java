package com.dudko.blazinghot.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

import java.util.List;

public class TooltipUtil {

    public static void addEffectTooltip(List<Component> lines, MobEffectInstance effect) {
        Component amplifier = effect.getAmplifier() == 0 ? Component.empty() : Component.translatable("potion.potency."
                                                                                                              + effect.getAmplifier()).append(" ");
        lines.add(Component
                          .translatable(effect.getDescriptionId())
                          .append(" ")
                          .append(amplifier)
                          .append("(")
                          .append(MobEffectUtil.formatDuration(effect, 1))
                          .append(")")
                          .withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
    }
}
