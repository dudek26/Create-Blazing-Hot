package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.foundation.registry.RegistryAliases;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class BlazingAliases {

	protected static RegistryAliases<Item> ITEMS = RegistryAliases.create(BuiltInRegistries.ITEM);
	protected static RegistryAliases<Block> BLOCKS = RegistryAliases.create(BuiltInRegistries.BLOCK);
	protected static RegistryAliases<Fluid> FLUIDS = RegistryAliases.create(BuiltInRegistries.FLUID);

	protected static void gatherAliases() {
		ITEMS.addAlias("nether_essence", BlazingItems.CRIMSON_ESSENCE);
		fluidAlias("nether_lava", "crimson_lava");
	}

	protected static void blockAlias(ResourceLocation from, ResourceLocation to) {
		ITEMS.addAlias(from, to);
		BLOCKS.addAlias(from, to);
	}

	protected static void blockAlias(String from, String to) {
		ITEMS.addAlias(from, to);
		BLOCKS.addAlias(from, to);
	}

	protected static void blockAlias(String from, BlockEntry<?> blockEntry) {
		ITEMS.addAlias(from, blockEntry.getId().getPath());
		BLOCKS.addAlias(from, blockEntry);
	}

	protected static void fluidAlias(ResourceLocation from, ResourceLocation to) {
		ITEMS.addAlias(from, to.withPath(to.getPath() + "_bucket"));
		BLOCKS.addAlias(from, to);
		FLUIDS.addAlias(from, to);
	}

	protected static void fluidAlias(String from, String to) {
		ITEMS.addAlias(from, to + "_bucket");
		BLOCKS.addAlias(from, to);
		FLUIDS.addAlias(from, to);
	}

	protected static void fluidAlias(String from, FluidEntry<?> fluidEntry) {
		String id = fluidEntry.getId().getPath();
		ITEMS.addAlias(from, id + "_bucket");
		BLOCKS.addAlias(from, id);
		FLUIDS.addAlias(from, fluidEntry);
	}

}
