package com.dudko.blazinghot.content.item;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.mojang.datafixers.util.Pair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.dudko.blazinghot.util.TooltipUtil.addEffectTooltip;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazingFoodItem extends Item {

    private boolean foil;
    private boolean extinguishing;
    private boolean effectTooltip = true;

    private int oxygen;

    public BlazingFoodItem(Properties properties) {
        super(properties);
    }

    public BlazingFoodItem(Properties properties, ExtraProperties... extraProperties) {
        super(properties);
        for (ExtraProperties property : extraProperties) {
            switch (property) {
                case FOIL -> foil = true;
                case EXTINGUISHING -> extinguishing = true;
                case DISABLE_EFFECT_TOOLTIP -> effectTooltip = false;
                case OXYGEN -> oxygen = property.value;
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return foil || super.isFoil(stack);
    }

    public boolean isExtinguishing() {
        return extinguishing;
    }

    public int getOxygen() {
        return oxygen;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (extinguishing) {
            if (livingEntity instanceof Player player && !level.isClientSide() &&
                    livingEntity.getRemainingFireTicks() > 0 &&
                    livingEntity.getHealth() < 4) {
                BlazingAdvancements.EXTINGUISHING_FOOD_SAVE.awardTo(player);
            }
            livingEntity.extinguishFire();
        }
        if (oxygen > 0) {
            livingEntity.setAirSupply(Math.min(livingEntity.getAirSupply() + oxygen, livingEntity.getMaxAirSupply()));
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag isAdvanced) {
        if (effectTooltip && BlazingConfigs.client().foodTooltips.get()) {
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

    public enum ExtraProperties {
        EXTINGUISHING,
        FOIL,
        DISABLE_EFFECT_TOOLTIP,
        OXYGEN(100);

        private final int value;

        ExtraProperties() {
            this(0);
        }

        ExtraProperties(int value) {
            this.value = value;
        }
    }
}
