package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;

import static com.dudko.blazinghot.BlazingHot.gatherData;

public class BlazingHotDataFabric implements DataGeneratorEntrypoint {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        REGISTRATE.setupDatagen(generator.createPack(), helper);

        DataGenerator.PackGenerator pack = generator.createPack();
        gatherData(pack);
    }
}
