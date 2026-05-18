package com.dudko.blazinghot.content.block.modern_lamp;

import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;

public class ModernLampBehaviour extends BlockEntityBehaviour {

	public static final BehaviourType<ModernLampBehaviour> TYPE = new BehaviourType<>();

	public boolean powered;
	public boolean locked;
	public int lightLevel;

	public State state;

	public ModernLampBehaviour(ModernLampBlockEntity be) {
		super(be);
		powered = false;
		locked = false;
		lightLevel = 15;
		state = State.MANUAL;
	}

	@Override
	public void write(CompoundTag nbt, Provider registries, boolean clientPacket) {
		super.write(nbt, registries, clientPacket);
		nbt.putBoolean("powered", powered);
		nbt.putBoolean("locked", locked);
		nbt.putInt("lightLevel", lightLevel);
		nbt.putString("state", state.toString().toLowerCase());
	}

	@Override
	public void read(CompoundTag nbt, Provider registries, boolean clientPacket) {
		super.read(nbt, registries, clientPacket);
		powered = nbt.getBoolean("powered");
		locked = nbt.getBoolean("locked");
		lightLevel = nbt.getInt("lightLevel");
		String stateString = nbt.getString("state");
		try {
			state = State.valueOf(stateString.toUpperCase());
		} catch (IllegalArgumentException e) {
			state = State.MANUAL;
		}
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}


	public enum State {
		MANUAL, ALWAYS_OFF, REDSTONE, REDSTONE_INVERTED, ALWAYS_ON
	}

}
