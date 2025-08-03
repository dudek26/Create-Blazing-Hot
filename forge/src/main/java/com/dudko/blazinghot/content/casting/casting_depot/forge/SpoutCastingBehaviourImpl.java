package com.dudko.blazinghot.content.casting.casting_depot.forge;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.mixin.accessor.SpoutBlockEntityAccessor;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpoutCastingBehaviourImpl extends SpoutCastingBehaviour {

	public SpoutCastingBehaviourImpl(CastingDepotBlockEntity depot) {
		super(depot);
	}

	public static SpoutCastingBehaviour of(CastingDepotBlockEntity depot) {
		return new SpoutCastingBehaviourImpl(depot);
	}

	@Override
	public int getRecipeCoolingDuration() {
		return getCurrentRecipe() == null ? -1 : getCurrentRecipe().getCoolingDuration();
	}

	@Override
	public int getRecipeProcessingDuration() {
		return getCurrentRecipe() == null ? -1 : getCurrentRecipe().getProcessingDuration();
	}

	private FluidStack getFluid() {
		SpoutBlockEntity spout = getSpout();
		if (spout == null) return FluidStack.EMPTY;
		return ((SpoutBlockEntityAccessor) spout).getTank().getPrimaryHandler().getFluid();
	}

	@Override
	public void tick() {
		super.tick();

		SpoutBlockEntity spout = getSpout();
		if (spout == null) {
			if (state == State.FILLING) {
				resetProcessing();
				return;
			}
			else if (state == State.NONE) return;
		}

		Level level = getWorld();
		CastingDepotBlockEntityImpl depot = (CastingDepotBlockEntityImpl) blockEntity;

		ItemStack stack = depot.getHeldItem();
		if (stack.isEmpty()) {
			resetProcessing();
			return;
		}

		FluidStack availableFluid = getFluid();
		if (availableFluid.isEmpty() && state != State.COOLING) return;
		int requiredAmount = CastingBySpout.getRequiredAmountForItem(level, stack, availableFluid);
		CastingRecipe currentRecipe = getCurrentRecipe();

		if (state == State.NONE) {
			if (!canSpout()) return;
			if (!depot.getOutputItem().isEmpty()) return;
			if (!CastingBySpout.canItemBeCast(level, stack) || availableFluid.getAmount() < requiredAmount) return;
			currentRecipe = CastingBySpout.findRecipe(depot, level, requiredAmount, stack, availableFluid);
			if (currentRecipe == null) return;
			currentRecipeId = currentRecipe.getId();
			state = State.FILLING;
			depot.setVisualFluid(availableFluid.getFluid());
			depot.notifyUpdate();
		}
		else if (state == State.FILLING) {
			if (currentRecipe == null || spout == null || !canSpout()) {
				resetProcessing();
				return;
			}
			if (processingTicks == 0)
				AllSoundEvents.SPOUTING.playOnServer(level, getPos(), 0.75f, 0.9f + 0.2f * (float) Math.random());

			processingTicks++;
			int duration = currentRecipe.getProcessingDuration();

			if (getVisualFluid() == Fluids.EMPTY) depot.setVisualFluid(availableFluid.getFluid());

			if (level.isClientSide) {
				if (processingTicks >= 4 && duration - processingTicks > 8) {
					depot.spawnProcessingParticles(availableFluid);
				}
				if (processingTicks >= 5 && processingTicks % 4 == 0) {
					depot.spawnSplash(availableFluid);
				}
			}

			if (processingTicks >= duration) {
				SmartFluidTank spoutTank = ((SpoutBlockEntityAccessor) spout).getTank().getPrimaryHandler();
				state = State.COOLING;
				processingTicks = -1;
				spoutTank.drain(requiredAmount, IFluidHandler.FluidAction.EXECUTE);
				depot.setFluid(spoutTank.getFluid().getFluid(), requiredAmount);
				castItem = CastingBySpout.getCastingResult(currentRecipe);
				coolingDuration = currentRecipe.getCoolingDuration();
				keepMold = currentRecipe.isKeepItem();
			}
		}
		else if (state == State.COOLING) {
			if (level.isClientSide && ((int) coolingTicks) % 3 == 0) {
				depot.spawnCoolingParticles();
			}

			float coolingSpeed = depot.getCoolingSpeed();
			coolingTicks = Math.max(coolingTicks + coolingSpeed, 0);

			if (coolingTicks >= coolingDuration) {
				if (!keepMold) stack.shrink(1);
				CastingDepotBehaviourImpl
						depotBehaviour =
						((CastingDepotBehaviourImpl) depot.getBehaviour(CastingDepotBehaviour.TYPE));
				if (BlazingTags.Items.MOLDS.matches(castItem)) {
					depotBehaviour.setHeldStack(castItem);
				}
				else {
					depotBehaviour.processingOutputBuffer.insertItem(0, castItem, false);
				}
				depot.resetFluid();

				resetProcessing();
				if (level.isClientSide) {
					level.playLocalSound(getPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.6f, 2f, false);
				}
			}
		}
	}

	@Override
	public void resetProcessing() {
		state = State.NONE;
		processingTicks = -1;
		coolingTicks = -1;
		coolingDuration = -1;
		keepMold = false;
		currentRecipeId = null;
		castItem = ItemStack.EMPTY;
		((CastingDepotBlockEntity) blockEntity).setVisualFluid(Fluids.EMPTY);
	}

	@Nullable
	public CastingRecipe getCurrentRecipe() {
		Level level = getWorld();
		if (level == null) return null;
		return CastingBySpout.findRecipe((CastingDepotBlockEntity) blockEntity, level, currentRecipeId);
	}

}
