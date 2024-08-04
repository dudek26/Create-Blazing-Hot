package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.dudko.blazinghot.registry.fabric.BlazingRecipeTypesImpl;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;

public class BlazingHotImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        BlazingHot.init();
        BlazingHot.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), BlazingHot.NAME);
    }

    public static void finalizeRegistrate() {
        BlazingRecipeTypesImpl.platformRegister();
        BlazingHot.registrate().register();

        BlazingFluidsImpl.registerFluidInteractions();
    }
}
