package com.dudko.blazinghot.neoforge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.BlazingTagGen;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.lang.BlazingLangGen;
import com.dudko.blazinghot.data.recipe.BlazingProcessingRecipeGen;
import com.dudko.blazinghot.data.recipe.CraftingRecipeGen;
import com.dudko.blazinghot.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class BlazingHotDataForge {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static void gatherData(GatherDataEvent event) {
		addExtraRegistrateData();

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();

		generator.addProvider(event.includeServer(), new BlazingAdvancements(output));
		generator.addProvider(event.includeServer(), new CraftingRecipeGen(output));
		generator.addProvider(event.includeServer(), BlazingProcessingRecipeGen.registerAll(output));
		generator.addProvider(event.includeServer(), new SequencedAssemblyRecipeGen(output));

	}

	private static void addExtraRegistrateData() {
		REGISTRATE.addDataGenerator(ProviderType.LANG, BlazingLangGen::generate);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BlazingTagGen::generateBlockTags);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BlazingTagGen::generateItemTags);
		REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, BlazingTagGen::generateFluidTags);
	}

}
