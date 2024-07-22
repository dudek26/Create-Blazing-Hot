package com.dudko.blazinghot;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import static com.dudko.blazinghot.BlazingHot.REGISTRATE;
import static com.dudko.blazinghot.BlazingHot.gatherData;

public class BlazingHotData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        REGISTRATE.setupDatagen(generator.createPack(), helper);

        FabricDataGenerator.Pack pack = generator.createPack();
        gatherData(pack);
    }
}
