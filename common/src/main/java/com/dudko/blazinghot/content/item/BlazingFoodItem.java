package com.dudko.blazinghot.content.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.dudko.blazinghot.util.TooltipUtil.addEffectTooltip;

public class BlazingFoodItem extends Item {

    private boolean foil;
    private boolean extinguishing;
    private boolean effectTooltip = true;

    public BlazingFoodItem(Properties properties) {
        super(properties);
    }

    public BlazingFoodItem(Properties properties, BProperties... bProperties) {
        super(properties);
        for (BProperties property : bProperties) {
            switch (property) {
                case FOIL -> foil = true;
                case EXTINGUISHING -> extinguishing = true;
                case DISABLE_EFFECT_TOOLTIP -> effectTooltip = false;
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return foil || super.isFoil(stack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, LivingEntity livingEntity) {
        if (extinguishing) livingEntity.extinguishFire();
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag isAdvanced) {
        if (effectTooltip) {
            Item item = stack.getItem();
            if (item.getFoodProperties() != null) item
                    .getFoodProperties()
                    .getEffects()
                    .stream()
                    .map(Pair::getFirst)
                    .forEach(mobEffectInstance -> addEffectTooltip(lines, mobEffectInstance));
        }
        super.appendHoverText(stack, level, lines, isAdvanced);
    }

    public enum BProperties {
        EXTINGUISHING,
        FOIL,
        DISABLE_EFFECT_TOOLTIP
    }
}
