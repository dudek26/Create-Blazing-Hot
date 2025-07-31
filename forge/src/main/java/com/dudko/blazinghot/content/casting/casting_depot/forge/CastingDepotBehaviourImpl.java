package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CastingDepotBehaviourImpl extends CastingDepotBehaviour {

	List<TransportedItemStack> incoming;
	ItemStackHandler processingOutputBuffer;
	CastingDepotItemHandler itemHandler;
	LazyOptional<CastingDepotItemHandler> lazyItemHandler;

	public CastingDepotBehaviourImpl(final CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		super(be, type);
		this.processingOutputBuffer = new ItemStackHandler(1) {
			protected void onContentsChanged(int slot) {
				be.notifyUpdate();
			}
		};
		incoming = new ArrayList<>();
		itemHandler = new CastingDepotItemHandler(this);
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}

	public static CastingDepotBehaviour of(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		return new CastingDepotBehaviourImpl(be, type);
	}

	public void enableMerging() {
		this.allowMerge = true;
	}

	public CastingDepotBehaviourImpl withCallback(Consumer<ItemStack> changeListener) {
		this.onHeldInserted = changeListener;
		return this;
	}

	public CastingDepotBehaviourImpl onlyAccepts(Predicate<ItemStack> filter) {
		this.acceptedItems = filter;
		return this;
	}

	@Override
	public void tick() {
		super.tick();
		Level world = this.blockEntity.getLevel();
		if (world == null) return;
		Iterator<TransportedItemStack> iterator = this.incoming.iterator();

		while (iterator.hasNext()) {
			TransportedItemStack ts = (TransportedItemStack) iterator.next();
			if (!world.isClientSide || this.blockEntity.isVirtual()) {
				if (this.heldStack == null) {
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

		if (this.heldStack != null) {
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

	public void unload() {
		if (this.lazyItemHandler != null) {
			this.lazyItemHandler.invalidate();
		}

	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		if (this.heldStack != null) {
			compound.put("HeldStack", this.heldStack.serializeNBT());
		}

		compound.put("OutputBuffer", this.processingOutputBuffer.serializeNBT());
		if (this.canMergeItems() && !this.incoming.isEmpty()) {
			compound.put("Incoming", NBTHelper.writeCompoundList(this.incoming, TransportedItemStack::serializeNBT));
		}

	}

	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		this.heldStack = null;
		if (compound.contains("HeldStack")) {
			this.heldStack = ItemStack.of(compound.getCompound("HeldStack"));
		}

		this.processingOutputBuffer.deserializeNBT(compound.getCompound("OutputBuffer"));
		if (this.canMergeItems()) {
			ListTag list = compound.getList("Incoming", 10);
			this.incoming = NBTHelper.readCompoundList(list, TransportedItemStack::read);
		}

	}

	@Override
	public void addSubBehaviours(List<BlockEntityBehaviour> behaviours) {
		if (Objects.equals(behaviourType.getName(), TYPE.getName()))
			behaviours.add((new DirectBeltInputBehaviour(this.blockEntity))
					.allowingBeltFunnels()
					.setInsertionHandler(this::tryInsertingFromSide)
					.considerOccupiedWhen(this::isOccupied));
	}

	public ItemStack getHeldItemStack() {
		return this.heldStack == null ? ItemStack.EMPTY : this.heldStack;
	}

	public boolean canMergeItems() {
		return this.allowMerge;
	}

	public int getPresentStackSize() {
		int cumulativeStackSize = 0;
		cumulativeStackSize += this.getHeldItemStack().getCount();

		for (int slot = 0; slot < this.processingOutputBuffer.getSlots(); ++slot) {
			cumulativeStackSize += this.processingOutputBuffer.getStackInSlot(slot).getCount();
		}

		return cumulativeStackSize;
	}

	public int getRemainingSpace() {
		int cumulativeStackSize = this.getPresentStackSize();

		for (TransportedItemStack transportedItemStack : this.incoming) {
			cumulativeStackSize += transportedItemStack.stack.getCount();
		}

		int
				fromGetter =
				Math.min((Integer) this.maxStackSize.get() == 0 ? 64 : (Integer) this.maxStackSize.get(),
						this.getHeldItemStack().getMaxStackSize());
		return fromGetter - cumulativeStackSize;
	}

	public ItemStack insert(TransportedItemStack heldItem, boolean simulate) {
		if (!(Boolean) this.canAcceptItems.get()) {
			return heldItem.stack;
		}
		else if (!this.acceptedItems.test(heldItem.stack)) {
			return heldItem.stack;
		}
		else if (this.canMergeItems()) {
			int remainingSpace = this.getRemainingSpace();
			ItemStack inserted = heldItem.stack;
			if (remainingSpace <= 0) {
				return inserted;
			}
			else if (this.heldStack != null && !ItemHelper.canItemStackAmountsStack(this.heldStack, inserted)) {
				return inserted;
			}
			else {
				ItemStack returned = ItemStack.EMPTY;
				if (remainingSpace < inserted.getCount()) {
					returned =
							ItemHandlerHelper.copyStackWithSize(heldItem.stack, inserted.getCount() - remainingSpace);
					if (!simulate) {
						TransportedItemStack copy = heldItem.copy();
						copy.stack.setCount(remainingSpace);
						if (this.heldStack != null) {
							this.incoming.add(copy);
						}
						else {
							this.heldStack = copy.stack;
						}
					}
				}
				else if (!simulate) {
					if (this.heldStack != null) {
						this.incoming.add(heldItem);
					}
					else {
						this.heldStack = heldItem.stack;
					}
				}

				return returned;
			}
		}
		else {
			ItemStack returned = ItemStack.EMPTY;
			int maxCount = 1;
			boolean stackTooLarge = maxCount < heldItem.stack.getCount();
			if (stackTooLarge) {
				returned = ItemHandlerHelper.copyStackWithSize(heldItem.stack, heldItem.stack.getCount() - maxCount);
			}

			if (simulate) {
				return returned;
			}
			else {
				if (this.isEmpty()) {
					if (heldItem.insertedFrom.getAxis().isHorizontal()) {
						AllSoundEvents.DEPOT_SLIDE.playOnServer(this.getWorld(), this.getPos());
					}
					else {
						AllSoundEvents.DEPOT_PLOP.playOnServer(this.getWorld(), this.getPos());
					}
				}

				if (stackTooLarge) {
					heldItem = heldItem.copy();
					heldItem.stack.setCount(maxCount);
				}

				this.heldStack = heldItem.stack;
				this.onHeldInserted.accept(heldItem.stack);
				return returned;
			}
		}
	}

	public void setHeldStack(ItemStack heldStack) {
		this.heldStack = heldStack;
	}

	public void removeHeldStack() {
		this.heldStack = null;
	}

	public <T> LazyOptional<T> getItemCapability() {
		return this.lazyItemHandler.cast();
	}

	private boolean isOccupied(Direction side) {
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
			ItemStack remainder = this.insert(transportedStack, simulate);
			if (remainder.getCount() != size) {
				this.blockEntity.notifyUpdate();
			}

			return remainder;
		}
	}

	public boolean isEmpty() {
		return this.heldStack == null && this.isOutputEmpty();
	}

	public boolean isOutputEmpty() {
		for (int i = 0; i < this.processingOutputBuffer.getSlots(); ++i) {
			if (!this.processingOutputBuffer.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private Vec3 getWorldPositionOf(TransportedItemStack transported) {
		return VecHelper.getCenterOf(this.blockEntity.getBlockPos());
	}

	public BehaviourType<?> getType() {
		return TYPE;
	}

	public boolean isItemValid(ItemStack stack) {
		return this.acceptedItems.test(stack);
	}


}
