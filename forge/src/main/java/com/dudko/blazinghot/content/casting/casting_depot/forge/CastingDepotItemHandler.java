package com.dudko.blazinghot.content.casting.casting_depot.forge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlock;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("UnstableApiUsage")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CastingDepotItemHandler implements IItemHandler {
	private static final int MAIN_SLOT = 0;
	private final CastingDepotBehaviourImpl behaviour;

	public CastingDepotItemHandler(CastingDepotBehaviourImpl behaviour) {
		this.behaviour = behaviour;
	}

	public int getSlots() {
		return 2;
	}

	public ItemStack getStackInSlot(int slot) {
		return slot == 0 ?
			   this.behaviour.getHeldItemStack() :
			   this.behaviour.processingOutputBuffer.getStackInSlot(slot - 1);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot != 0) {
			return stack;
		}
		else if (!this.behaviour.getHeldItemStack().isEmpty() && !this.behaviour.canMergeItems()) {
			return stack;
		}
		else if (!this.behaviour.isOutputEmpty() && !this.behaviour.canMergeItems()) {
			return stack;
		}
		else {
			ItemStack remainder = this.behaviour.insert(stack, Direction.UP, simulate);
			if (!simulate && remainder != stack) {
				this.behaviour.blockEntity.notifyUpdate();
			}

			return remainder;
		}
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != 0) {
			return this.behaviour.processingOutputBuffer.extractItem(slot - 1, amount, simulate);
		}
		else {
			CastingDepotBlockEntityImpl depot = (CastingDepotBlockEntityImpl) behaviour.blockEntity;
			if (!depot.getBlockState().getValue(CastingDepotBlock.POWERED)) {
				return ItemStack.EMPTY;
			}
			if (depot.getState() != SpoutCastingBehaviour.State.NONE || !depot.getOutputItem().isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack held = this.behaviour.heldStack;
			if (held == ItemStack.EMPTY) {
				return held;
			}
			else {
				ItemStack stack = held.copy();
				ItemStack extracted = stack.split(amount);
				if (!simulate) {
					this.behaviour.heldStack = stack;
					this.behaviour.blockEntity.notifyUpdate();
				}

				return extracted;
			}
		}
	}

	public int getSlotLimit(int slot) {
		return slot == 0 ? this.behaviour.maxStackSize.get() : 64;
	}

	public boolean isItemValid(int slot, ItemStack stack) {
		return slot == 0 && this.behaviour.isItemValid(stack);
	}
}
