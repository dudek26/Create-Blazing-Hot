package com.dudko.blazinghot.neoforge;

import static com.dudko.blazinghot.foundation.recipe.BlazingRecipeProvider.GENERATORS;

import java.util.concurrent.CompletableFuture;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.NeoForgeTagGen;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.lang.BlazingLangGen;
import com.dudko.blazinghot.data.recipe.StandardRecipeGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazingHotDataNeoForge {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static void gatherDataHighPriority(GatherDataEvent event) {
		if (event.getMods().contains(BlazingHot.ID)) addExtraRegistrateData();
	}

	public static void gatherData(GatherDataEvent event) {
		if (!event.getMods().contains(BlazingHot.ID)) return;

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(event.includeServer(), new DataProvider() {
			@Override
			public String getName() {
				return "Create: Blazing Hot's Processing Recipes";
			}

			@Override
			public CompletableFuture<?> run(CachedOutput dc) {
				return CompletableFuture.allOf(GENERATORS
						.stream()
						.map(gen -> gen.run(dc))
						.toArray(CompletableFuture[]::new));
			}
		});

		generator.addProvider(event.includeServer(), new BlazingAdvancements(output, lookupProvider));
		generator.addProvider(event.includeServer(), new StandardRecipeGen(output, lookupProvider));
//		generator.addProvider(event.includeServer(), new SequencedAssemblyRecipeGen(output, lookupProvider));

	}

	private static void addExtraRegistrateData() {
		REGISTRATE.addDataGenerator(ProviderType.LANG, BlazingLangGen::generate);
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, NeoForgeTagGen::generateBlockTags);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, NeoForgeTagGen::generateItemTags);
		REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, NeoForgeTagGen::generateFluidTags);
	}

}
