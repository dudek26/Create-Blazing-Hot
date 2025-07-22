package com.dudko.blazinghot.content.casting.casting_depot.forge;

import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;

import net.minecraft.MethodsReturnNonnullByDefault;
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
		return 9;
	}

	public ItemStack getStackInSlot(int slot) {
		return slot == 0 ?
			   this.behaviour.getHeldItemStack() :
			   this.behaviour.processingOutputBuffer.getStackInSlot(slot - 1);
	}

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
			ItemStack remainder = this.behaviour.insert(new TransportedItemStack(stack), simulate);
			if (!simulate && remainder != stack) {
				this.behaviour.blockEntity.notifyUpdate();
			}

			return remainder;
		}
	}

	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != 0) {
			return this.behaviour.processingOutputBuffer.extractItem(slot - 1, amount, simulate);
		}
		else {
			ItemStack held = this.behaviour.heldStack;
			if (held == null) {
				return ItemStack.EMPTY;
			}
			else {
				ItemStack stack = held.copy();
				ItemStack extracted = stack.split(amount);
				if (!simulate) {
					this.behaviour.heldStack = stack;
					if (stack.isEmpty()) {
						this.behaviour.heldStack = null;
					}

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
