package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

public class BlazingBlocks {
	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static final BlockEntry<Block> EXAMPLE_BLOCK = REGISTRATE.block("example_block", Block::new).register();

	public static void init() {
		// load the class and register everything
		BlazingHot.LOGGER.info("Registering blocks for " + BlazingHot.NAME);
	}
}
