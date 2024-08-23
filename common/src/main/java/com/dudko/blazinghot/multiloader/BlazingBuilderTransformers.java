package com.dudko.blazinghot.multiloader;

import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.DyeColor;

public class BlazingBuilderTransformers {

	@ExpectPlatform
	public static <B extends ModernLampBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampBlock(DyeColor color) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <B extends ModernLampPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampPanel(DyeColor color) {
		throw new AssertionError();
	}

}
