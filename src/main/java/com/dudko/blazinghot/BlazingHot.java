package com.dudko.blazinghot;

import com.dudko.blazinghot.data.BlazingLangGen;
import com.dudko.blazinghot.data.BlazingTagGen;
import com.dudko.blazinghot.data.recipe.BlazingCraftingRecipeGen;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen;
import com.dudko.blazinghot.data.recipe.BlazingSequencedAssemblyRecipeGen;
import com.dudko.blazinghot.registry.*;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlazingHot implements ModInitializer {
    public static final String ID = "blazinghot";
    public static final String NAME = "Create: Blazing Hot";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

    static {
        REGISTRATE.setTooltipModifierFactory(
                item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE).andThen(
                        TooltipModifier.mapNull(KineticStats.create(item))));
    }

    @Override
    public void onInitialize() {

        BlazingCreativeTabs.register();
        BlazingItems.register();
        BlazingBlocks.register();
        BlazingFluids.register();
        BlazingFluids.registerFluidInteractions();
        BlazingEntityTypes.register();
        BlazingTags.register();

        REGISTRATE.register();

        LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", NAME, Create.VERSION);
        LOGGER.info(EnvExecutor.unsafeRunForDist(() -> () -> "{} is accessing Porting Lib from the client!",
                                                 () -> () -> "{} is accessing Porting Lib from the server!"), NAME);


    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }

    public static void gatherData(FabricDataGenerator.Pack pack) {
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BlazingTagGen::generateBlockTags);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BlazingTagGen::generateItemTags);
        REGISTRATE.addDataGenerator(ProviderType.LANG, BlazingLangGen::generate);

        pack.addProvider(BlazingSequencedAssemblyRecipeGen::new);
        pack.addProvider(BlazingCraftingRecipeGen::new);
        pack.addProvider(BlazingProcessingRecipeGen::registerAll);
    }
}
