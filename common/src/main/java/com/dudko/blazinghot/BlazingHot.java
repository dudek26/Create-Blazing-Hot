package com.dudko.blazinghot;

import com.dudko.blazinghot.data.BlazingLangGen;
import com.dudko.blazinghot.data.BlazingTagGen;
import com.dudko.blazinghot.registry.*;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlazingHot {
    public static final String ID = "blazinghot";
    public static final String NAME = "Create: Blazing Hot";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(BlazingHot.ID);

    public static void init() {
        LOGGER.info("Create mod addon {} initializing! Create version: {}", NAME, Create.VERSION);

        BlazingTags.register();
        CommonTags.register();
        BlazingCreativeTabs.register();
        BlazingBlocks.register();
        BlazingItems.register();
        BlazingFluids.register();
        BlazingEntityTypes.register();
        BlazingBlockEntityTypes.register();
        BlazingRecipeTypes.register();

        finalizeRegistrate();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static void gatherData(DataGenerator.PackGenerator pack) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BlazingTagGen::generateBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BlazingTagGen::generateItemTags);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BlazingTagGen::generateFluidTags);
        REGISTRATE.addDataGenerator(ProviderType.LANG, BlazingLangGen::generate);


    }

    @ExpectPlatform
    public static void finalizeRegistrate() {
        throw new AssertionError();
    }
}
