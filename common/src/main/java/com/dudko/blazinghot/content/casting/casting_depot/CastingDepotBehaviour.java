package com.dudko.blazinghot.content.casting.casting_depot;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CastingDepotBehaviour extends BlockEntityBehaviour {

	public static final BehaviourType<CastingDepotBehaviour> INPUT = new BehaviourType<>("Input");
	public static final BehaviourType<CastingDepotBehaviour> OUTPUT = new BehaviourType<>("Output");
	public TransportedItemStackHandlerBehaviour transportedHandler;
	public Supplier<Integer> maxStackSize;
	public Supplier<Boolean> canAcceptItems;
	public Predicate<Direction> canFunnelsPullFrom;
	public Consumer<ItemStack> onHeldInserted;
	public Predicate<ItemStack> acceptedItems;
	public boolean allowMerge;
	public float coolingSpeedModifier;

	protected BehaviourType<CastingDepotBehaviour> behaviourType;

	public CastingDepotBehaviour(CastingDepotBlockEntity be, BehaviourType<CastingDepotBehaviour> type) {
		super(be);
		maxStackSize = () -> 1;
		canAcceptItems = () -> true;
		canFunnelsPullFrom = $ -> be.getBlockState().getValue(BlockStateProperties.POWERED);
		acceptedItems = $ -> true;
		onHeldInserted = $ -> {
		};
		behaviourType = type;
	}

	@Override
	public BehaviourType<?> getType() {
		return behaviourType;
	}

	public static final Map<FanProcessingType, Float>
			COOLING_SPEEDS =
			Map.of(AllFanProcessingTypes.BLASTING, -0.5f, AllFanProcessingTypes.SPLASHING, 0.5f);
}
