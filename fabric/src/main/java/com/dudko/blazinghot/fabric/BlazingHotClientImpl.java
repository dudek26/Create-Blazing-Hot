package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.world.item.Item;

import java.util.Objects;

import static com.dudko.blazinghot.BlazingHotClient.addEffectTooltip;

public class BlazingHotClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {
            if (Objects.equals(stack.getItem().getCreatorModId(stack), BlazingHot.ID)) {
                Item item = stack.getItem();
                if (item.getFoodProperties() != null) {
                    item
                            .getFoodProperties()
                            .getEffects()
                            .stream()
                            .map(Pair::getFirst)
                            .forEach(mobEffectInstance -> addEffectTooltip(lines, mobEffectInstance));
                }
            }
        }));
    }
}
