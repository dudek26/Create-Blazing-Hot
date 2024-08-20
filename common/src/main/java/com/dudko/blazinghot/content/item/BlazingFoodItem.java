package com.dudko.blazinghot.content.item;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.dudko.blazinghot.content.item.BlazingFoodItem.ExtraProperties.REMOVE_SLOWNESS_ANY;
import static com.dudko.blazinghot.util.TooltipUtil.addEffectTooltip;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazingFoodItem extends Item {

    private boolean foil;
    private boolean extinguishing;
    private boolean effectTooltip = true;

    private int oxygen;
    private int removeSlowness = -1;

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
                case REMOVE_SLOWNESS_0, REMOVE_SLOWNESS_1, REMOVE_SLOWNESS_2, REMOVE_SLOWNESS_ANY ->
                        removeSlowness = property.value;
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

    public int getMaxRemovedSlowness() {
        return removeSlowness;
    }

    public String getRemovedSlownessDescription() {
        return switch (removeSlowness) {
            case 0 -> ItemDescriptions.SLOWNESS_REMOVING_FOOD_0.getKey();
            case 1 -> ItemDescriptions.SLOWNESS_REMOVING_FOOD_1.getKey();
            case 2 -> ItemDescriptions.SLOWNESS_REMOVING_FOOD_2.getKey();
            default -> ItemDescriptions.SLOWNESS_REMOVING_FOOD_ANY.getKey();
        };
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
        if (removeSlowness == REMOVE_SLOWNESS_ANY.value ||
                (removeSlowness >= 0 && livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) &&
                        livingEntity.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() <= removeSlowness)) {
            livingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
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
        OXYGEN(50),
        REMOVE_SLOWNESS_0(0),
        REMOVE_SLOWNESS_1(1),
        REMOVE_SLOWNESS_2(2),
        REMOVE_SLOWNESS_ANY(255);

        private final int value;

        ExtraProperties() {
            this(0);
        }

        ExtraProperties(int value) {
            this.value = value;
        }
    }
}
