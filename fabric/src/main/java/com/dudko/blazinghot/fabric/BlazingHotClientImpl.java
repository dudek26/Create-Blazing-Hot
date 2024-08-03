package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.BlazingHotClient;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.world.item.Item;

import java.util.Objects;

import static com.dudko.blazinghot.BlazingHotClient.addEffectTooltip;

public class BlazingHotClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlazingHotClient.init();
    }
}
