package com.dudko.blazinghot.content.casting.casting_depot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class CastingDepotBehaviour extends BlockEntityBehaviour {

	public static final BehaviourType<CastingDepotBehaviour> TYPE = new BehaviourType<>();
	public TransportedItemStackHandlerBehaviour transportedHandler;
	public Supplier<Integer> maxStackSize;
	public Supplier<Boolean> canAcceptItems;
	public Predicate<Direction> canFunnelsPullFrom;
	public Consumer<ItemStack> onHeldInserted;
	public Predicate<ItemStack> acceptedItems;
	public boolean allowMerge;

	protected BehaviourType<CastingDepotBehaviour> behaviourType;

	public @NotNull ItemStack heldStack;
	protected List<TransportedItemStack> incoming;

	public CastingDepotBehaviour(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		super(be);
		maxStackSize = () -> 1;
		canAcceptItems = () -> !be.isPowered();
		canFunnelsPullFrom = $ -> true;
		acceptedItems = $ -> true;
		onHeldInserted = $ -> {
		};
		behaviourType = type;
		allowMerge = false;
		heldStack = ItemStack.EMPTY;
		incoming = new ArrayList<>();
	}

	@ExpectPlatform
	public static CastingDepotBehaviour of(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		throw new AssertionError();
	}

	@Override
	public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(compound, registries, clientPacket);
		if (!this.heldStack.isEmpty()) {
			compound.put("HeldStack", heldStack.saveOptional(registries));
		}

		if (this.canMergeItems() && !this.incoming.isEmpty()) {
			compound.put("Incoming",
					NBTHelper.writeCompoundList(this.incoming, stack -> stack.serializeNBT(registries)));
		}
	}

	@Override
	public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(compound, registries, clientPacket);
		this.heldStack = ItemStack.EMPTY;
		if (compound.contains("HeldStack")) {
			this.heldStack = ItemStack.parseOptional(registries, compound.getCompound("HeldStack"));
		}
		if (this.canMergeItems()) {
			ListTag list = compound.getList("Incoming", 10);
			incoming = NBTHelper.readCompoundList(list, c -> TransportedItemStack.read(c, registries));
		}
	}

	public abstract void addSubBehaviours(List<BlockEntityBehaviour> behaviours);

	public ItemStack getHeldItemStack() {
		return this.heldStack;
	}

	public boolean canMergeItems() {
		return this.allowMerge;
	}

	public abstract int getPresentStackSize();

	public abstract int getRemainingSpace();

	public abstract ItemStack insert(ItemStack heldItem, Direction insertedFrom, boolean simulate);

	public abstract ItemStack extract(int slot, int amount, boolean simulate);

	public void setHeldStack(ItemStack heldStack) {
		this.heldStack = heldStack;
	}

	public void removeHeldStack() {
		this.heldStack = ItemStack.EMPTY;
	}

	public boolean isEmpty() {
		return this.heldStack.isEmpty() && this.isOutputEmpty();
	}

	protected abstract boolean isOccupied(Direction side);

	public abstract boolean isOutputEmpty();

	@Override
	public BehaviourType<?> getType() {
		return behaviourType;
	}

	protected abstract void handleBeltFunnelOutput();

}
