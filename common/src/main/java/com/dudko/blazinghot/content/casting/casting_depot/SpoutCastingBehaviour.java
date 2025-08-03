package com.dudko.blazinghot.content.casting.casting_depot;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class SpoutCastingBehaviour extends BlockEntityBehaviour {

	public static final BehaviourType<SpoutCastingBehaviour> TYPE = new BehaviourType<>();

	protected State state;
	protected int processingTicks;
	protected float coolingTicks;

	protected ItemStack castItem;
	protected boolean keepMold;
	protected int coolingDuration;

	protected Fluid visualFluid;
	protected ResourceLocation currentRecipeId;

	public SpoutCastingBehaviour(CastingDepotBlockEntity depot) {
		super(depot);
		state = State.NONE;
		processingTicks = -1;
		coolingTicks = -1;
		coolingDuration = -1;
		castItem = ItemStack.EMPTY;
		keepMold = false;
		visualFluid = null;
		currentRecipeId = null;
	}

	@ExpectPlatform
	public static SpoutCastingBehaviour of(CastingDepotBlockEntity depot) {
		throw new AssertionError();
	}

	@Nullable
	protected SpoutBlockEntity getSpout() {
		BlockEntity be = getWorld().getBlockEntity(getPos().above(2));
		if (be instanceof SpoutBlockEntity spout) {
			return spout;
		}
		return null;
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	public State getState() {
		return state;
	}

	public int getProcessingTicks() {
		return processingTicks;
	}

	public float getCoolingTicks() {
		return coolingTicks;
	}

	public Fluid getVisualFluid() {
		return visualFluid;
	}

	public abstract int getRecipeCoolingDuration();

	public abstract int getRecipeProcessingDuration();

	public abstract void resetProcessing();

	@Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
		nbt.putFloat("CoolingTicks", coolingTicks);
		nbt.putInt("ProcessingTicks", processingTicks);
		nbt.putString("State", state.toString());
		nbt.putString("VisualFluid", MultiRegistries.getFluidId(visualFluid).toString());
		if (currentRecipeId != null) nbt.putString("ProcessedRecipe", currentRecipeId.toString());
		CompoundTag castItemTag = new CompoundTag();
		castItem.save(castItemTag);
		nbt.put("CastItem", castItemTag);
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
		coolingTicks = nbt.getFloat("CoolingTicks");
		processingTicks = nbt.getInt("ProcessingTicks");
		state = State.valueOf(nbt.getString("State").toUpperCase());
		ResourceLocation fluidId = ResourceLocation.tryParse(nbt.getString("VisualFluid"));
		if (fluidId == null) visualFluid = Fluids.EMPTY;
		else visualFluid = MultiRegistries.getFluidFromRegistry(fluidId).get();

		if (nbt.contains("ProcessedRecipe")) {
			currentRecipeId = ResourceLocation.tryParse(nbt.getString("ProcessedRecipe"));
		}
		castItem = ItemStack.of(nbt.getCompound("CastItem"));
	}


	public enum State {
		NONE,
		FILLING,
		COOLING;

		public final boolean savable;

		State(boolean savable) {
			this.savable = savable;
		}

		State() {
			this.savable = true;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
}
