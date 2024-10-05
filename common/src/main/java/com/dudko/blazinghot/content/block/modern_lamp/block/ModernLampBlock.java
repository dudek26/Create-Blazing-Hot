package com.dudko.blazinghot.content.block.modern_lamp.block;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.modern_lamp.AbstractModernLamp;
import com.dudko.blazinghot.content.block.modern_lamp.IModernLampBE;
import com.dudko.blazinghot.registry.BlazingBlockEntityTypes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModernLampBlock extends AbstractModernLamp implements IModernLampBE {

	public ModernLampBlock(Properties properties, DyeColor color) {
		super(properties, color);
	}

	@Override
	public boolean isLocked(Level level, BlockPos pos) {
		return IModernLampBE.super.iIsLocked(level, pos);
	}

	@Override
	public boolean isPowered(Level level, BlockPos pos) {
		return IModernLampBE.super.iIsPowered(level, pos);
	}

	@Override
	public void setLocked(Level level, BlockPos pos, boolean locked) {
		IModernLampBE.super.iSetLocked(level, pos, locked);
	}

	@Override
	public void setPowered(Level level, BlockPos pos, boolean powered) {
		IModernLampBE.super.iSetPowered(level, pos, powered);
	}

	@Override
	public BlockEntityType<? extends ModernLampBlockEntity> getBlockEntityType() {
		return BlazingBlockEntityTypes.MODERN_LAMP.get();
	}
}
