package com.dudko.blazinghot.content.block.modern_lamp;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.DyeColor;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModernLampBlock extends AbstractModernLamp {

	public ModernLampBlock(Properties properties, DyeColor color) {
		super(properties, color);
	}
	
}
