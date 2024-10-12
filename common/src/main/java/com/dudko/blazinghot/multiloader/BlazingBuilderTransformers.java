package com.dudko.blazinghot.multiloader;

import com.dudko.blazinghot.content.block.modern_lamp.AbstractModernLamp;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampDoublePanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampHalfPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.SmallModernLampPanelBlock;
import com.dudko.blazinghot.content.casting.Molds;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class BlazingBuilderTransformers {

	@ExpectPlatform
	public static <B extends AbstractModernLamp, P> NonNullUnaryOperator<BlockBuilder<B, P>> anyModernLamp(DyeColor color) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <B extends ModernLampBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampBlock(DyeColor color) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <B extends ModernLampPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampPanel(DyeColor color, String name) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <B extends ModernLampHalfPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampHalfPanel(DyeColor color, String name) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <B extends ModernLampDoublePanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampDirectionalPanel(DyeColor color, String name) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <B extends SmallModernLampPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampSmallPanel(DyeColor color, String name) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> mold(String name, Molds.MoldType type) {
		throw new AssertionError();
	}

}
