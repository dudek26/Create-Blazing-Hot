package com.dudko.blazinghot.content.casting.casting_depot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
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

	@NotNull
	protected Fluid visualFluid;
	protected ResourceLocation currentRecipeId;

	public final List<FanProcessingType> fanModifiers;

	public SpoutCastingBehaviour(CastingDepotBlockEntity depot) {
		super(depot);
		state = State.NONE;
		processingTicks = -1;
		coolingTicks = -1;
		coolingDuration = -1;
		castItem = ItemStack.EMPTY;
		keepMold = false;
		visualFluid = Fluids.EMPTY;
		currentRecipeId = null;

		fanModifiers = new ArrayList<>();
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

	public boolean canSpout() {
		return !((CastingDepotBlockEntity) blockEntity).isPowered();
	}

	public abstract int getRecipeCoolingDuration();

	public abstract int getRecipeProcessingDuration();

	public abstract void resetProcessing();

	@Override
	public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(nbt, registries, clientPacket);
		nbt.putFloat("CoolingTicks", coolingTicks);
		nbt.putInt("ProcessingTicks", processingTicks);
		nbt.putString("State", state.toString());
		nbt.putString("VisualFluid", MultiRegistries.getFluidId(visualFluid).toString());
		if (currentRecipeId != null) nbt.putString("ProcessedRecipe", currentRecipeId.toString());
		if (!castItem.isEmpty()) {
			CompoundTag castItemTag = new CompoundTag();
			castItem.save(registries, castItemTag);
			nbt.put("CastItem", castItemTag);
		}
		nbt.putInt("CoolingDuration", coolingDuration);
		nbt.putBoolean("KeepMold", keepMold);
	}

	@Override
	public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(nbt, registries, clientPacket);
		coolingTicks = nbt.getFloat("CoolingTicks");
		processingTicks = nbt.getInt("ProcessingTicks");
		state = State.valueOf(nbt.getString("State").toUpperCase());
		ResourceLocation fluidId = ResourceLocation.tryParse(nbt.getString("VisualFluid"));
		if (fluidId == null) visualFluid = Fluids.EMPTY;
		else visualFluid = MultiRegistries.getFluidFromRegistry(fluidId).get();

		if (nbt.contains("ProcessedRecipe")) {
			currentRecipeId = ResourceLocation.tryParse(nbt.getString("ProcessedRecipe"));
		}
		castItem = ItemStack.EMPTY;
		if (nbt.contains("CastItem")) {
			castItem = ItemStack.parseOptional(registries, nbt.getCompound("CastItem"));
		}
		coolingDuration = nbt.getInt("CoolingDuration");
		keepMold = nbt.getBoolean("KeepMold");
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

	public static float getCoolingModifier(FanProcessingType type) {
		if (type.equals(AllFanProcessingTypes.BLASTING)) {
			return BlazingConfigs.server().casting.blastingCoolingModifier.getF();
		}
		if (type.equals(AllFanProcessingTypes.SPLASHING)) {
			return BlazingConfigs.server().casting.splashingCoolingModifier.getF();
		}
		return 0;
	}
}
