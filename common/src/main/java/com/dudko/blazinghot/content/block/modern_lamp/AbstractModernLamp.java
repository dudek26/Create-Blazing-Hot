package com.dudko.blazinghot.content.block.modern_lamp;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.simibubi.create.AllTags;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class AbstractModernLamp extends Block {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	private final DyeColor color;

	public AbstractModernLamp(Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
		registerDefaultState(defaultBlockState().setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(LIT));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

		if (player.getItemInHand(hand).isEmpty()) {
			float pitch = state.getValue(LIT) ? 0.5F : 0.8F;
			if (!level.isClientSide) BlazingAdvancements.MODERN_LAMP.awardTo(player);
			level.setBlockAndUpdate(pos, state.cycle(LIT));
			level.playLocalSound(pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0F, pitch, false);
			return InteractionResult.SUCCESS;
		}

		if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag) && !player.isCrouching()) {
			boolean locked = isLocked(level, pos);
			Component action = locked ? BlazingLang.LAMP_UNLOCKED.get() : BlazingLang.LAMP_LOCKED.get();
			player.displayClientMessage(action, true);

			SoundEvent sound = locked ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
			player.playSound(sound, 1.0F, 1.0F);

			setLocked(level, pos, !locked);
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

	public abstract boolean isLocked(Level level, BlockPos pos);

	public abstract boolean isPowered(Level level, BlockPos pos);

	public abstract void setLocked(Level level, BlockPos pos, boolean locked);

	public abstract void setPowered(Level level, BlockPos pos, boolean powered);

}
