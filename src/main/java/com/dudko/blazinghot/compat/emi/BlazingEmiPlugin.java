package com.dudko.blazinghot.compat.emi;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.simibubi.create.compat.emi.CreateEmiPlugin;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;

public class BlazingEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {

        registry.addWorkstation(CreateEmiPlugin.MIXING, EmiStack.of(BlazingBlocks.BLAZE_MIXER));

    }
}
