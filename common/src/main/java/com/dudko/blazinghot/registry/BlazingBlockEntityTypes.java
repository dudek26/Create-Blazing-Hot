package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.block.ModernLampBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class BlazingBlockEntityTypes {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static final BlockEntityEntry<ModernLampBlockEntity>
			MODERN_LAMP =
			REGISTRATE
					.blockEntity("modern_lamp", ModernLampBlockEntity::new)
					.validBlocks(BlazingBlocks.MODERN_LAMP_BLOCKS.toArray())
					.register(),
			MODERN_LAMP_PANEL =
					REGISTRATE
							.blockEntity("modern_lamp_panel", ModernLampBlockEntity::new)
							.validBlocks(BlazingBlocks.MODERN_LAMP_PANELS.toArray())
							.register(),
			MODERN_LAMP_QUAD_PANEL =
					REGISTRATE
							.blockEntity("modern_lamp_quad_panel", ModernLampBlockEntity::new)
							.validBlocks(BlazingBlocks.MODERN_LAMP_QUAD_PANELS.toArray())
							.register();

	public static void register() {
		platformRegister();
	}

	@ExpectPlatform
	public static void platformRegister() {
		throw new AssertionError();
	}
}
