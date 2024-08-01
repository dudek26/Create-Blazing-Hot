package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
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
        BlazingHot.registrate().register();
    }
}
