package com.dudko.blazinghot.registry.neoforge;

import com.dudko.blazinghot.foundation.registry.neoforge.RegistryAliasesImpl;
import com.dudko.blazinghot.registry.BlazingAliases;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import net.neoforged.bus.api.IEventBus;

public class BlazingAliasesNeoForge extends BlazingAliases {

	public static void register(IEventBus bus) {
		gatherAliases();
		((RegistryAliasesImpl<Item>) ITEMS).neoForgeRegistration(bus);
		((RegistryAliasesImpl<Block>) BLOCKS).neoForgeRegistration(bus);
		((RegistryAliasesImpl<Fluid>) FLUIDS).neoForgeRegistration(bus);
	}

}
