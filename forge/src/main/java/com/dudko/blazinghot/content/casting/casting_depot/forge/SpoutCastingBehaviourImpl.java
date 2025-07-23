package com.dudko.blazinghot.content.casting.casting_depot.forge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.mixin.accessor.SpoutBlockEntityAccessor;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpoutCastingBehaviourImpl extends SpoutCastingBehaviour {

	CastingRecipe currentRecipe;

	public SpoutCastingBehaviourImpl(CastingDepotBlockEntity depot) {
		super(depot);
		currentRecipe = null;
	}

	public static SpoutCastingBehaviour of(CastingDepotBlockEntity depot) {
		return new SpoutCastingBehaviourImpl(depot);
	}

	@Override
	public int getRecipeCoolingDuration() {
		return currentRecipe == null ? -1 : currentRecipe.getCoolingDuration();
	}

	@Override
	public int getRecipeProcessingDuration() {
		return currentRecipe == null ? -1 : currentRecipe.getProcessingDuration();
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
			if (state == State.SPOUTING) {
				state = State.NONE;
				processingTicks = -1;
				return;
			}
			if (state == State.NONE) return;
		}

		Level level = getWorld();
		CastingDepotBlockEntityImpl depot = (CastingDepotBlockEntityImpl) blockEntity;

		ItemStack stack = depot.getHeldItem();
		if (stack.isEmpty()) {
			reset();
			return;
		}

		FluidStack availableFluid = getFluid();
		if (availableFluid.isEmpty()) return;
		int requiredAmount = CastingBySpout.getRequiredAmountForItem(level, stack, availableFluid);

		if (state == State.NONE) {
			if (!depot.getOutputItem().isEmpty()) return;
			if (!CastingBySpout.canItemBeCast(level, stack) || availableFluid.getAmount() < requiredAmount) return;
			currentRecipe = CastingBySpout.findRecipe(level, requiredAmount, stack, availableFluid);
			state = State.SPOUTING;
		}
		else if (state == State.SPOUTING) {
			if (currentRecipe == null || spout == null) {
				reset();
				return;
			}
			processingTicks++;
			int duration = currentRecipe.getProcessingDuration();

			if (processingTicks == 8)
				AllSoundEvents.SPOUTING.playOnServer(level, getPos(), 0.75f, 0.9f + 0.2f * (float) Math.random());

			if (processingTicks >= 8 && level.isClientSide) {
				depot.spawnProcessingParticles(availableFluid);

				if (processingTicks >= 12 && processingTicks % 4 == 0) {
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
			}
		}
		else if (state == State.COOLING) {
			if (currentRecipe == null) {
				state = State.NONE;
				coolingTicks = -1;
				return;
			}

			if (level.isClientSide && ((int) coolingTicks) % 3 == 0) {
				depot.spawnCoolingParticles();
			}

			float coolingSpeed = depot.getCoolingSpeed();
			coolingTicks = Math.max(coolingTicks + coolingSpeed, 0);

			if (coolingTicks >= currentRecipe.getCoolingDuration()) {
				((CastingDepotBehaviourImpl) depot.getBehaviour(CastingDepotBehaviour.TYPE)).processingOutputBuffer.insertItem(
						0,
						castItem,
						false);
				depot.resetFluid();
				CastingBySpout.finishCasting(currentRecipe, stack);
				reset();
				if (level.isClientSide) {
					level.playLocalSound(getPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.6f, 2f, false);
				}
			}
		}
	}

	@Override
	public void reset() {
		state = State.NONE;
		processingTicks = -1;
		coolingTicks = -1;
		currentRecipe = null;
		castItem = ItemStack.EMPTY;
	}

	@Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
		if (currentRecipe != null) nbt.putString("ProcessedRecipe", currentRecipe.getId().toString());
		if (!castItem.isEmpty()) nbt.put("CastItem", castItem.serializeNBT());
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
		if (nbt.contains("ProcessedRecipe")) {
			ResourceLocation id = ResourceLocation.tryParse(nbt.getString("ProcessedRecipe"));
			if (id == null) {
				currentRecipe = null;
			}
			else currentRecipe = CastingBySpout.findRecipe(getWorld(), id);
		}
		if (nbt.contains("CastItem")) {
			castItem = ItemStack.of(nbt.getCompound("CastItem"));
		}
	}
}
