package com.dudko.blazinghot.content.casting.casting_depot.neoforge;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CastingDepotBehaviourImpl extends CastingDepotBehaviour {

	ItemStackHandler processingOutputBuffer;
	CastingDepotItemHandler itemHandler;

	public CastingDepotBehaviourImpl(final CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		super(be, type);
		this.processingOutputBuffer = new ItemStackHandler(1) {
			protected void onContentsChanged(int slot) {
				be.notifyUpdate();
			}
		};
		itemHandler = new CastingDepotItemHandler(this);
	}

	public static CastingDepotBehaviour of(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		return new CastingDepotBehaviourImpl(be, type);
	}

	@Override
	public void tick() {
		super.tick();
		Level world = this.blockEntity.getLevel();
		if (world == null) return;
		Iterator<TransportedItemStack> iterator = this.incoming.iterator();

		while (iterator.hasNext()) {
			TransportedItemStack ts = iterator.next();
			if (!world.isClientSide || this.blockEntity.isVirtual()) {
				if (this.heldStack.isEmpty()) {
					this.heldStack = ts.stack;
				}
				else if (!ItemHelper.canItemStackAmountsStack(this.heldStack, ts.stack)) {
					Vec3 vec = VecHelper.getCenterOf(this.blockEntity.getBlockPos());
					Containers.dropItemStack(this.blockEntity.getLevel(),
							vec.x,
							vec.y + (double) 0.5F,
							vec.z,
							ts.stack);
				}
				else {
					this.heldStack.grow(ts.stack.getCount());
				}

				iterator.remove();
				this.blockEntity.notifyUpdate();
			}
		}

		if (!this.heldStack.isEmpty()) {
			if (!world.isClientSide) {
				this.handleBeltFunnelOutput();
			}
		}
	}

	@Override
	protected void handleBeltFunnelOutput() {
		BlockState funnel = getWorld().getBlockState(getPos().above());
		Direction funnelFacing = AbstractFunnelBlock.getFunnelFacing(funnel);
		if (funnelFacing == null || !canFunnelsPullFrom.test(funnelFacing.getOpposite())) return;

		for (int slot = 0; slot < processingOutputBuffer.getSlots(); slot++) {
			ItemStack previousItem = processingOutputBuffer.getStackInSlot(slot);
			if (previousItem.isEmpty()) continue;
			ItemStack
					afterInsert =
					blockEntity
							.getBehaviour(DirectBeltInputBehaviour.TYPE)
							.tryExportingToBeltFunnel(previousItem, null, false);
			if (afterInsert == null) return;
			if (previousItem.getCount() != afterInsert.getCount()) {
				processingOutputBuffer.setStackInSlot(slot, afterInsert);
				blockEntity.notifyUpdate();
				return;
			}
		}

	}

	@Override
	public void destroy() {
		super.destroy();
		Level level = this.getWorld();
		BlockPos pos = this.getPos();
		ItemHelper.dropContents(level, pos, this.processingOutputBuffer);

		for (TransportedItemStack transportedItemStack : this.incoming) {
			Block.popResource(level, pos, transportedItemStack.stack);
		}

		if (!this.getHeldItemStack().isEmpty()) {
			Block.popResource(level, pos, this.getHeldItemStack());
		}

	}

	@Override
	public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(compound, registries, clientPacket);
		compound.put("OutputBuffer", this.processingOutputBuffer.serializeNBT(registries));
	}

	@Override
	public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(compound, registries, clientPacket);
		this.processingOutputBuffer.deserializeNBT(registries, compound.getCompound("OutputBuffer"));
	}

	@Override
	public void addSubBehaviours(List<BlockEntityBehaviour> behaviours) {
		if (Objects.equals(behaviourType.getName(), TYPE.getName()))
			behaviours.add((new DirectBeltInputBehaviour(this.blockEntity))
					.allowingBeltFunnels()
					.setInsertionHandler(this::tryInsertingFromSide)
					.considerOccupiedWhen(this::isOccupied));
	}

	@Override
	public int getPresentStackSize() {
		int cumulativeStackSize = 0;
		cumulativeStackSize += this.getHeldItemStack().getCount();

		for (int slot = 0; slot < this.processingOutputBuffer.getSlots(); ++slot) {
			cumulativeStackSize += this.processingOutputBuffer.getStackInSlot(slot).getCount();
		}

		return cumulativeStackSize;
	}

	@Override
	public int getRemainingSpace() {
		int cumulativeStackSize = this.getPresentStackSize();

		for (TransportedItemStack transportedItemStack : this.incoming) {
			cumulativeStackSize += transportedItemStack.stack.getCount();
		}

		int
				fromGetter =
				Math.min(this.maxStackSize.get() == 0 ? 64 : this.maxStackSize.get(),
						this.getHeldItemStack().getMaxStackSize());
		return fromGetter - cumulativeStackSize;
	}

	@Override
	public ItemStack extract(int slot, int amount, boolean simulate) {
		return itemHandler.extractItem(slot, amount, simulate);
	}

	@Override
	public ItemStack insert(ItemStack heldItem, Direction insertedFrom, boolean simulate) {
		if (!(Boolean) this.canAcceptItems.get()) {
			return heldItem;
		}
		else if (!this.acceptedItems.test(heldItem)) {
			return heldItem;
		}
		else if (this.canMergeItems()) {
			int remainingSpace = this.getRemainingSpace();
			if (remainingSpace <= 0) {
				return heldItem;
			}
			else if (!this.heldStack.isEmpty() && !ItemHelper.canItemStackAmountsStack(this.heldStack, heldItem)) {
				return heldItem;
			}
			else {
				ItemStack returned = ItemStack.EMPTY;
				if (remainingSpace < heldItem.getCount()) {
					returned = heldItem.copyWithCount(heldItem.getCount() - remainingSpace);
					if (!simulate) {
						TransportedItemStack copy = new TransportedItemStack(heldItem.copy());
						copy.stack.setCount(remainingSpace);
						if (!this.heldStack.isEmpty()) {
							this.incoming.add(copy);
						}
						else {
							this.heldStack = copy.stack;
						}
					}
				}
				else if (!simulate) {
					if (!this.heldStack.isEmpty()) {
						TransportedItemStack transported = new TransportedItemStack(heldItem);
						this.incoming.add(transported);
					}
					else {
						this.heldStack = heldItem;
					}
				}

				return returned;
			}
		}
		else {
			ItemStack returned = ItemStack.EMPTY;
			int maxCount = 1;
			boolean stackTooLarge = maxCount < heldItem.getCount();
			if (stackTooLarge) {
				returned = heldItem.copyWithCount(heldItem.getCount() - maxCount);
			}

			if (!simulate) {
				if (this.isEmpty()) {
					if (insertedFrom.getAxis().isHorizontal()) {
						AllSoundEvents.DEPOT_SLIDE.playOnServer(this.getWorld(), this.getPos());
					}
					else {
						AllSoundEvents.DEPOT_PLOP.playOnServer(this.getWorld(), this.getPos());
					}
				}

				if (stackTooLarge) {
					heldItem = heldItem.copy();
					heldItem.setCount(maxCount);
				}

				this.heldStack = heldItem;
				this.onHeldInserted.accept(heldItem);
			}
			return returned;
		}
	}

	@Override
	protected boolean isOccupied(Direction side) {
		if (!this.getHeldItemStack().isEmpty() && !this.canMergeItems()) {
			return true;
		}
		else if (!this.isOutputEmpty() && !this.canMergeItems()) {
			return true;
		}
		else {
			return !(Boolean) this.canAcceptItems.get();
		}
	}

	private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
		ItemStack inserted = transportedStack.stack;
		if (this.isOccupied(side)) {
			return inserted;
		}
		else {
			int size = transportedStack.stack.getCount();
			transportedStack = transportedStack.copy();
			transportedStack.beltPosition = side.getAxis().isVertical() ? 0.5F : 0.0F;
			transportedStack.insertedFrom = side;
			transportedStack.prevSideOffset = transportedStack.sideOffset;
			transportedStack.prevBeltPosition = transportedStack.beltPosition;
			ItemStack remainder = this.insert(getHeldItemStack(), transportedStack.insertedFrom, simulate);
			if (remainder.getCount() != size) {
				this.blockEntity.notifyUpdate();
			}

			return remainder;
		}
	}

	@Override
	public boolean isOutputEmpty() {
		for (int i = 0; i < this.processingOutputBuffer.getSlots(); ++i) {
			if (!this.processingOutputBuffer.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public boolean isItemValid(ItemStack stack) {
		return this.acceptedItems.test(stack);
	}


}
