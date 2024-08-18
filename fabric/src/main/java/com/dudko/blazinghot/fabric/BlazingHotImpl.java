package com.dudko.blazinghot.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.fabric.BlazingConfigsImpl;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.fabric.BlazingLangGen;
import com.dudko.blazinghot.data.fabric.BlazingTagGen;
import com.dudko.blazinghot.data.recipe.fabric.CraftingRecipeGen;
import com.dudko.blazinghot.data.recipe.fabric.BlazingProcessingRecipeGen;
import com.dudko.blazinghot.data.recipe.fabric.SequencedAssemblyRecipeGen;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.dudko.blazinghot.registry.fabric.BlazingRecipeTypesImpl;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.data.DataGenerator;

public class BlazingHotImpl implements ModInitializer {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    @Override
    public void onInitialize() {
        BlazingHot.init();
        BlazingConfigsImpl.register();
        BlazingHot.LOGGER.info(EnvExecutor.unsafeRunForDist(() -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"), BlazingHot.NAME);
    }

    public static void finalizeRegistrate() {
        BlazingRecipeTypesImpl.platformRegister();
        BlazingHot.registrate().register();

        BlazingFluidsImpl.registerFluidInteractions();
    }

    public static void gatherData(DataGenerator.PackGenerator pack) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BlazingTagGen::generateBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BlazingTagGen::generateItemTags);
        REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BlazingTagGen::generateFluidTags);
        REGISTRATE.addDataGenerator(ProviderType.LANG, BlazingLangGen::generate);

        pack.addProvider(SequencedAssemblyRecipeGen::new);
        pack.addProvider(CraftingRecipeGen::new);
        pack.addProvider(BlazingProcessingRecipeGen::registerAll);
        pack.addProvider(BlazingAdvancements::new);
    }
}
