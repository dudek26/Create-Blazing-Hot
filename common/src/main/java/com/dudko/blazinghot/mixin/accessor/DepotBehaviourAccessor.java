package com.dudko.blazinghot.mixin.accessor;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;

@Mixin(DepotBehaviour.class)
public interface DepotBehaviourAccessor {

	@Accessor
	TransportedItemStack getHeldItem();

	@Accessor
	List<TransportedItemStack> getIncoming();

	@Accessor
	ItemStackHandler getProcessingOutputBuffer();
}
