package com.dudko.blazinghot.content.casting.casting_depot.fabric;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// TODO: clean this mess
@SuppressWarnings("UnstableApiUsage")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CastingDepotBehaviourImpl extends CastingDepotBehaviour {

	TransportedItemStack heldItem;
	ItemStackHandler processingOutputBuffer;
	public CastingDepotItemHandler itemHandler;

	SnapshotParticipant<Data> snapshotParticipant = new SnapshotParticipant<>() {
		@Override
		protected Data createSnapshot() {
			// incoming stacks are not mutated during transfer, no need to deep copy
			return new Data(new ArrayList<>(incoming), heldItem == null ? null : heldItem.fullCopy());
		}

		@Override
		protected void readSnapshot(Data snapshot) {
			incoming = snapshot.incoming;
			heldItem = snapshot.held;
		}

		@Override
		protected void onFinalCommit() {
			blockEntity.notifyUpdate();
		}
	};

	record Data(List<TransportedItemStack> incoming, TransportedItemStack held) {
	}

	public CastingDepotBehaviourImpl(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		super(be, type);

		incoming = new ArrayList<>();
		itemHandler = new CastingDepotItemHandler(this);
		processingOutputBuffer = new ItemStackHandler(8) {
			protected void onContentsChanged(int slot) {
				be.notifyUpdate();
			}
		};

	}

	public CastingDepotBehaviourImpl withCallback(Consumer<ItemStack> changeListener) {
		onHeldInserted = changeListener;
		return this;
	}

	public CastingDepotBehaviourImpl onlyAccepts(Predicate<ItemStack> filter) {
		acceptedItems = filter;
		return this;
	}

	@Override
	public void tick() {
		super.tick();

		Level world = blockEntity.getLevel();

		for (Iterator<TransportedItemStack> iterator = incoming.iterator(); iterator.hasNext(); ) {
			TransportedItemStack ts = iterator.next();
			if (!tick(ts)) continue;
			if (world == null) continue;
			if (world.isClientSide && !blockEntity.isVirtual()) continue;
			if (heldItem == null) {
				heldItem = ts;
			}
			else {
				if (!ItemHelper.canItemStackAmountsStack(heldItem.stack, ts.stack)) {
					Vec3 vec = VecHelper.getCenterOf(blockEntity.getBlockPos());
					Containers.dropItemStack(blockEntity.getLevel(), vec.x, vec.y + .5f, vec.z, ts.stack);
				}
				else {
					heldItem.stack.grow(ts.stack.getCount());
				}
			}
			iterator.remove();
			blockEntity.notifyUpdate();
		}

		if (heldItem == null) return;
		if (!tick(heldItem)) return;

		BlockPos pos = blockEntity.getBlockPos();

		if (world.isClientSide) return;

		BeltProcessingBehaviour
				processingBehaviour =
				BlockEntityBehaviour.get(world, pos.above(2), BeltProcessingBehaviour.TYPE);
		if (processingBehaviour == null) return;
		if (!heldItem.locked && BeltProcessingBehaviour.isBlocked(world, pos)) return;

		ItemStack previousItem = heldItem.stack;
		boolean wasLocked = heldItem.locked;
		BeltProcessingBehaviour.ProcessingResult
				result =
				wasLocked ?
				processingBehaviour.handleHeldItem(heldItem, transportedHandler) :
				processingBehaviour.handleReceivedItem(heldItem, transportedHandler);
		if (result == BeltProcessingBehaviour.ProcessingResult.REMOVE) {
			heldItem = null;
			blockEntity.sendData();
			return;
		}

		// fabric: might be set to null in processing
		if (heldItem == null) {
			blockEntity.sendData();
			return;
		}

		heldItem.locked = result == BeltProcessingBehaviour.ProcessingResult.HOLD;
		if (heldItem.locked != wasLocked || !ItemStack.matches(previousItem, heldItem.stack)) blockEntity.sendData();
	}

	protected boolean tick(TransportedItemStack heldItem) {
		heldItem.prevBeltPosition = heldItem.beltPosition;
		heldItem.prevSideOffset = heldItem.sideOffset;
		float diff = .5f - heldItem.beltPosition;
		if (diff > 1 / 512f) {
			if (diff > 1 / 32f && !BeltHelper.isItemUpright(heldItem.stack)) heldItem.angle += 1;
			heldItem.beltPosition += diff / 4f;
		}
		return diff < 1 / 16f;
	}

	@Override
	protected void handleBeltFunnelOutput() {
		BlockState funnel = getWorld().getBlockState(getPos().above());
		Direction funnelFacing = AbstractFunnelBlock.getFunnelFacing(funnel);
		if (funnelFacing == null || !canFunnelsPullFrom.test(funnelFacing.getOpposite())) return;

		for (int slot = 0; slot < processingOutputBuffer.getSlotCount(); slot++) {
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

		ItemStack previousItem = heldItem.stack;
		if (previousItem.isEmpty()) { // fabric: this is not allowed
			return;
		}
		ItemStack
				afterInsert =
				blockEntity
						.getBehaviour(DirectBeltInputBehaviour.TYPE)
						.tryExportingToBeltFunnel(previousItem, null, false);
		if (afterInsert == null) return;
		if (previousItem.getCount() != afterInsert.getCount()) {
			if (afterInsert.isEmpty()) heldItem = null;
			else heldItem.stack = afterInsert;
			blockEntity.notifyUpdate();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		Level level = getWorld();
		BlockPos pos = getPos();
		ItemHelper.dropContents(level, pos, processingOutputBuffer);
		for (TransportedItemStack transportedItemStack : incoming)
			Block.popResource(level, pos, transportedItemStack.stack);
		if (!getHeldItemStack().isEmpty()) Block.popResource(level, pos, getHeldItemStack());
	}

	@Override
	public void unload() {
		itemHandler = null;
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		if (heldItem != null) compound.put("HeldItem", heldItem.serializeNBT());
		compound.put("OutputBuffer", processingOutputBuffer.serializeNBT());
		if (canMergeItems() && !incoming.isEmpty())
			compound.put("Incoming", NBTHelper.writeCompoundList(incoming, TransportedItemStack::serializeNBT));
	}

	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		heldItem = null;
		if (compound.contains("HeldItem")) heldItem = TransportedItemStack.read(compound.getCompound("HeldItem"));
		processingOutputBuffer.deserializeNBT(compound.getCompound("OutputBuffer"));
		if (canMergeItems()) {
			ListTag list = compound.getList("Incoming", Tag.TAG_COMPOUND);
			incoming = NBTHelper.readCompoundList(list, TransportedItemStack::read);
		}
	}

	@Override
	public void addSubBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(new DirectBeltInputBehaviour(blockEntity)
				.allowingBeltFunnels()
				.setInsertionHandler(this::tryInsertingFromSide)
				.considerOccupiedWhen(this::isOccupied));
		transportedHandler =
				new TransportedItemStackHandlerBehaviour(blockEntity,
						this::applyToAllItems).withStackPlacement(this::getWorldPositionOf);
		behaviours.add(transportedHandler);
	}

	@Override
	public ItemStack insert(ItemStack heldItem, Direction insertedFrom, boolean simulate) {
		return ItemStack.EMPTY;
	}

	public ItemStack getHeldItemStack() {
		return heldItem == null ? ItemStack.EMPTY : heldItem.stack;
	}

	public boolean canMergeItems() {
		return super.canMergeItems();
	}

	public int getPresentStackSize() {
		int cumulativeStackSize = 0;
		cumulativeStackSize += getHeldItemStack().getCount();
		for (int slot = 0; slot < processingOutputBuffer.getSlotCount(); slot++)
			cumulativeStackSize += processingOutputBuffer.getStackInSlot(slot).getCount();
		return cumulativeStackSize;
	}

	public int getRemainingSpace() {
		int cumulativeStackSize = getPresentStackSize();
		for (TransportedItemStack transportedItemStack : incoming)
			cumulativeStackSize += transportedItemStack.stack.getCount();
		int fromGetter = maxStackSize.get();
		return (fromGetter == 0 ? 1 : fromGetter) - cumulativeStackSize;
	}

	public ItemStack insert(TransportedItemStack heldItem, TransactionContext ctx) {
		if (!canAcceptItems.get()) return heldItem.stack;
		if (!acceptedItems.test(heldItem.stack)) return heldItem.stack;

		if (canMergeItems()) {
			int remainingSpace = getRemainingSpace();
			ItemStack inserted = heldItem.stack;
			if (remainingSpace <= 0) return inserted;
			if (this.heldItem != null && !ItemHelper.canItemStackAmountsStack(this.heldItem.stack, inserted))
				return inserted;

			ItemStack returned = ItemStack.EMPTY;
			snapshotParticipant.updateSnapshots(ctx);
			if (remainingSpace < inserted.getCount()) {
				returned = ItemHandlerHelper.copyStackWithSize(heldItem.stack, inserted.getCount() - remainingSpace);
				TransportedItemStack copy = heldItem.copy();
				copy.stack.setCount(remainingSpace);
				if (this.heldItem != null) incoming.add(copy);
				else this.heldItem = copy;
			}
			else {
				if (this.heldItem != null) incoming.add(heldItem);
				else this.heldItem = heldItem;
			}
			return returned;
		}

		if (this.isEmpty()) {
			if (heldItem.insertedFrom.getAxis().isHorizontal())
				TransactionCallback.onSuccess(ctx, () -> AllSoundEvents.DEPOT_SLIDE.playOnServer(getWorld(), getPos()));
			else TransactionCallback.onSuccess(ctx, () -> AllSoundEvents.DEPOT_PLOP.playOnServer(getWorld(), getPos()));
		}
		snapshotParticipant.updateSnapshots(ctx);
		ItemStack inserted = heldItem.stack.copyWithCount(1);
		ItemStack held = heldItem.stack.copyWithCount(heldItem.stack.getCount() - 1);
		TransportedItemStack transported = heldItem.copy();
		TransportedItemStack returned = transported.copy();
		transported.stack = inserted;
		returned.stack = held;
		this.heldItem = transported;
		TransactionCallback.onSuccess(ctx, () -> onHeldInserted.accept(heldItem.stack));
		return held.isEmpty() ? ItemStack.EMPTY : held;
	}

	public void setHeldItem(TransportedItemStack heldItem) {
		this.heldItem = heldItem;
	}

	public void setHeldStack(ItemStack heldItem) {
		throw new UnsupportedOperationException();
	}

	public void removeHeldItem() {
		this.heldItem = null;
	}

	public void setCenteredHeldItem(TransportedItemStack heldItem) {
		this.heldItem = heldItem;
		this.heldItem.beltPosition = 0.5f;
		this.heldItem.prevBeltPosition = 0.5f;
	}

//	public <T> LazyOptional<T> getItemCapability(Capability<T> cap, Direction side) {
//		return lazyItemHandler.cast();
//	}

	@Override
	protected boolean isOccupied(Direction side) {
		if (!getHeldItemStack().isEmpty() && !canMergeItems()) return true;
		if (!isOutputEmpty() && !canMergeItems()) return true;
		return !canAcceptItems.get();
	}

	private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
		ItemStack inserted = transportedStack.stack;

		if (isOccupied(side)) return inserted;

		int size = transportedStack.stack.getCount();
		transportedStack = transportedStack.copy();
		transportedStack.beltPosition = side.getAxis().isVertical() ? .5f : 0;
		transportedStack.insertedFrom = side;
		transportedStack.prevSideOffset = transportedStack.sideOffset;
		transportedStack.prevBeltPosition = transportedStack.beltPosition;
		try (Transaction t = TransferUtil.getTransaction()) {
			snapshotParticipant.updateSnapshots(t);
			ItemStack remainder = insert(transportedStack, t);
			if (remainder.getCount() != size) blockEntity.notifyUpdate();
			if (!simulate) t.commit();

			return remainder;
		}
	}

	private void applyToAllItems(float maxDistanceFromCentre, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
		if (heldItem == null) return;
		if (.5f - heldItem.beltPosition > maxDistanceFromCentre) return;

		boolean dirty = false;
		TransportedItemStack transportedItemStack = heldItem;
		ItemStack stackBefore = transportedItemStack.stack.copy();
		TransportedItemStackHandlerBehaviour.TransportedResult result = processFunction.apply(transportedItemStack);
		if (result == null || result.didntChangeFrom(stackBefore)) return;

		dirty = true;
		heldItem = null;
		if (result.hasHeldOutput()) setCenteredHeldItem(result.getHeldOutput());

		for (TransportedItemStack added : result.getOutputs()) {
			if (getHeldItemStack().isEmpty()) {
				setCenteredHeldItem(added);
				continue;
			}
			try (Transaction t = TransferUtil.getTransaction()) {
				long inserted = processingOutputBuffer.insert(ItemVariant.of(added.stack), added.stack.getCount(), t);
				t.commit();
				ItemStack remainder = added.stack.copy();
				remainder.setCount(ItemHelper.truncateLong(added.stack.getCount() - inserted));
				Vec3 vec = VecHelper.getCenterOf(blockEntity.getBlockPos());
				Containers.dropItemStack(blockEntity.getLevel(), vec.x, vec.y + .5f, vec.z, remainder);
			}
		}

		if (dirty) blockEntity.notifyUpdate();
	}

	@Override
	public boolean isOutputEmpty() {
		for (int i = 0; i < processingOutputBuffer.getSlotCount(); i++)
			if (!processingOutputBuffer.getStackInSlot(i).isEmpty()) return false;
		return true;
	}

	private Vec3 getWorldPositionOf(TransportedItemStack transported) {
		return VecHelper.getCenterOf(blockEntity.getBlockPos());
	}

	public boolean isItemValid(ItemStack stack) {
		return acceptedItems.test(stack);
	}

	public static CastingDepotBehaviour of(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		return new CastingDepotBehaviourImpl(be, type);
	}


}
