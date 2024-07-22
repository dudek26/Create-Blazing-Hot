package com.dudko.blazinghot.content.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public enum Foods {

    BLAZE_CARROT(new FoodProperties.Builder().alwaysEat().nutrition(6).saturationMod(1.2F).build()),
    STELLAR_GOLDEN_APPLE(new FoodProperties.Builder()
                                 .alwaysEat()
                                 .nutrition(4)
                                 .saturationMod(1.2F)
                                 .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 60 * 20), 1)
                                 .effect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 1), 1)
                                 .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 5 * 60 * 20), 1)
                                 .build()),
    BLAZE_APPLE(new FoodProperties.Builder()
                        .alwaysEat()
                        .nutrition(4)
                        .saturationMod(1.2F)
                        .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 60 * 20), 1)
                        .effect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 1), 1)
                        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 5 * 60 * 20), 1)
                        .build()),
    STELLAR_BLAZE_APPLE(new FoodProperties.Builder()
                                .alwaysEat()
                                .nutrition(4)
                                .saturationMod(1.2F)
                                .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 60 * 20), 1)
                                .effect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 1), 1)
                                .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 8 * 60 * 20), 1)
                                .build()),
    ENCHANTED_BLAZE_APPLE(new FoodProperties.Builder()
                                  .alwaysEat()
                                  .nutrition(4)
                                  .saturationMod(1.2F)
                                  .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2 * 60 * 20, 2), 1)
                                  .effect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 1), 1)
                                  .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 8 * 60 * 20), 1)
                                  .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5 * 60 * 20), 1)
                                  .build()),
    IRON_CARROT(new FoodProperties.Builder().nutrition(6).saturationMod(0.9F).build()),
    IRON_APPLE(new FoodProperties.Builder()
                       .nutrition(4)
                       .alwaysEat()
                       .saturationMod(1.1F)
                       .effect(new MobEffectInstance(MobEffects.ABSORPTION, 60 * 20), 1)
                       .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30 * 20), 1)
                       .build());

    public final FoodProperties foodProperties;

    Foods(FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
    }

}
