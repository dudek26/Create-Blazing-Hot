package com.dudko.blazinghot.content.block.modern_lamp;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.simibubi.create.foundation.block.IBE;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModernLampBlock extends Block implements IBE<ModernLampBlockEntity> {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	private final DyeColor color;

	public ModernLampBlock(Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
		registerDefaultState(defaultBlockState().setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(LIT));
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

		if (pPlayer.getItemInHand(pHand).isEmpty()) {
			float pitch = pState.getValue(LIT) ? 0.5F : 0.8F;
			if (!pLevel.isClientSide) BlazingAdvancements.MODERN_LAMP.awardTo(pPlayer);
			pLevel.setBlockAndUpdate(pPos, pState.cycle(LIT));
			pLevel.playLocalSound(pPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0F, pitch, false);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (level.isClientSide) return;

		boolean isPowered = isPowered(level, pos);
		if (isPowered == level.hasNeighborSignal(pos)) return;
		if (isPowered) {
			level.setBlock(pos, state.setValue(LIT, false), 2);
			setPowered(level, pos, false);
			return;
		}

		level.setBlock(pos, state.setValue(LIT, true), 2);
		setPowered(level, pos, true);
		scheduleActivation(level, pos);
	}

	private void scheduleActivation(Level pLevel, BlockPos pPos) {
		if (!pLevel.getBlockTicks().hasScheduledTick(pPos, this)) pLevel.scheduleTick(pPos, this, 1);
	}

	public DyeColor getColor() {
		return color;
	}

	public boolean isLocked(Level level, BlockPos pos) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		return be != null && be.locked;
	}

	public boolean isPowered(Level level, BlockPos pos) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		return be != null && be.powered;
	}

	public void setLocked(Level level, BlockPos pos, boolean locked) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		if (be != null) be.locked = locked;
	}

	public void setPowered(Level level, BlockPos pos, boolean powered) {
		ModernLampBlockEntity be = getBlockEntity(level, pos);
		if (be != null) be.powered = powered;
	}

	@Override
	public Class<ModernLampBlockEntity> getBlockEntityClass() {
		return ModernLampBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends ModernLampBlockEntity> getBlockEntityType() {
		return platformedBlockEntity();
	}

	@ExpectPlatform
	public static BlockEntityType<? extends ModernLampBlockEntity> platformedBlockEntity() {
		throw new AssertionError();
	}

}
