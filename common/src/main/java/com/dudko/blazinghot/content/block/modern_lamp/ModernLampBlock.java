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

/**
 * <p>Extend this if you want the panels to inherit the placement helper.</p>
 * <p>Otherwise, extend {@link AbstractModernLampPanel}</p>
 */
@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModernLampBlock extends Block {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	private final DyeColor color;

	public ModernLampBlock(Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
		registerDefaultState(defaultBlockState().setValue(LIT, false).setValue(LOCKED, false).setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder.add(LIT, LOCKED, POWERED));
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		boolean locked = pState.getValue(LOCKED);

		if (pPlayer.getItemInHand(pHand).isEmpty() && !locked) {
			float pitch = pState.getValue(LIT) ? 0.5F : 0.8F;
			if (!pLevel.isClientSide) BlazingAdvancements.MODERN_LAMP.awardTo(pPlayer);
			pLevel.setBlockAndUpdate(pPos, pState.cycle(LIT));
			pLevel.playLocalSound(pPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0F, pitch, false);
			return InteractionResult.SUCCESS;
		}

		if (pPlayer.getItemInHand(pHand).is(AllTags.AllItemTags.WRENCH.tag) && !pPlayer.isCrouching()) {
			Component action = locked ? BlazingLang.LAMP_UNLOCKED.get() : BlazingLang.LAMP_LOCKED.get();
			pPlayer.displayClientMessage(action, true);

			SoundEvent sound = locked ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
			pPlayer.playSound(sound, 1.0F, 1.0F);

			pLevel.setBlockAndUpdate(pPos, pState.cycle(LOCKED));
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		if (pLevel.isClientSide) return;

		boolean isPowered = pState.getValue(POWERED);
		if (isPowered == pLevel.hasNeighborSignal(pPos)) return;
		if (isPowered) {
			pLevel.setBlock(pPos, pState.setValue(POWERED, false).setValue(LIT, false), 2);
			return;
		}

		pLevel.setBlock(pPos, pState.setValue(POWERED, true).setValue(LIT, true), 2);
		scheduleActivation(pLevel, pPos);
	}

	private void scheduleActivation(Level pLevel, BlockPos pPos) {
		if (!pLevel.getBlockTicks().hasScheduledTick(pPos, this)) pLevel.scheduleTick(pPos, this, 1);
	}

	public DyeColor getColor() {
		return color;
	}
}
