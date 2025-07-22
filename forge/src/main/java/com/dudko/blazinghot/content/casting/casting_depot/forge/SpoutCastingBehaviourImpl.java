package com.dudko.blazinghot.content.casting.casting_depot.forge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.mixin.accessor.SpoutBlockEntityAccessor;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
				processingTicks = 0;
				return;
			}
			if (state == State.NONE) return;
		}

		Level world = getWorld();
		CastingDepotBlockEntity depot = (CastingDepotBlockEntity) blockEntity;

		ItemStack stack = depot.getHeldItem();
		if (stack.isEmpty()) return;
		FluidStack availableFluid = getFluid();
		if (availableFluid.isEmpty()) return;
		int requiredAmount = CastingBySpout.getRequiredAmountForItem(world, stack, availableFluid);

		if (state == State.NONE) {
			if (!CastingBySpout.canItemBeCast(world, stack)) return;
			currentRecipe = CastingBySpout.findRecipe(world, requiredAmount, stack, availableFluid);
			state = State.SPOUTING;
		}
		else if (state == State.SPOUTING) {
			if (currentRecipe == null) {
				state = State.NONE;
				processingTicks = 0;
				return;
			}
			processingTicks++;
			int duration = currentRecipe.getProcessingDuration();

			if (processingTicks >= duration) {
				assert spout != null;
				SmartFluidTank spoutTank = ((SpoutBlockEntityAccessor) spout).getTank().getPrimaryHandler();
				state = State.COOLING;
				processingTicks = 0;
				coolingTicks = 0;
				spoutTank.drain(requiredAmount, IFluidHandler.FluidAction.EXECUTE);
				depot.setFluid(spoutTank.getFluid().getFluid(), requiredAmount);
				castItem = CastingBySpout.getCastingResult(currentRecipe);
			}
		}
		else if (state == State.COOLING) {
			if (currentRecipe == null) {
				state = State.NONE;
				coolingTicks = 0;
				return;
			}

			float coolingSpeed = depot.getCoolingSpeed();
			coolingTicks = Math.max(coolingTicks + coolingSpeed, 0);

			if (coolingTicks >= currentRecipe.getCoolingDuration()) {
				state = State.NONE;
				castItem = ItemStack.EMPTY;
				coolingTicks = 0;
				depot.resetFluid();
				CastingBySpout.finishCasting(currentRecipe, stack);
				currentRecipe = null;
			}
		}
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
