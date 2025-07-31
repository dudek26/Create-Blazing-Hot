package com.dudko.blazinghot.content.casting.casting_depot.forge;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

public class CastingDepotBlockEntityImpl extends CastingDepotBlockEntity {

	protected CastingDepotBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private CastingDepotBehaviourImpl getInputBehaviour() {
		return (CastingDepotBehaviourImpl) depotBehaviour;
	}

	private CastingDepotBehaviourImpl castBehaviour() {
		return (CastingDepotBehaviourImpl) depotBehaviour;
	}

	@Override
	public ItemStack getHeldItem() {
		return depotBehaviour.heldStack == null ? ItemStack.EMPTY : depotBehaviour.heldStack;
	}

	@Override
	public ItemStack getOutputItem() {
		return castBehaviour().processingOutputBuffer.getStackInSlot(0);
	}

	@Override
	public void setOutputItem(ItemStack stack) {
		castBehaviour().processingOutputBuffer.setStackInSlot(0, stack);
	}

	@Override
	public float getCoolingSpeed() {
		return 1 + depotBehaviour.coolingSpeedModifier;
	}

	@Override
	public void setFluid(Fluid fluid, long amount) {
		tank.getPrimaryHandler().setFluid(new FluidStack(fluid, (int) amount));
	}

	public FluidStack getFluid() {
		return tank.getPrimaryHandler().getFluid();
	}

	@Override
	public void resetFluid() {
		tank.getPrimaryHandler().setFluid(FluidStack.EMPTY);
		visualFluid = Fluids.EMPTY;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);

		tank = SmartFluidTankBehaviour.single(this, (int) MultiAmount.BLOCK.multiply(4).get());
		behaviours.add(tank);
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (isItemHandlerCap(cap)) {
			return this.getInputBehaviour().getItemCapability();
		}
		return super.getCapability(cap, side);
	}

	protected void spawnProcessingParticles(FluidStack fluid) {
		if (isVirtual()) return;
		if (fluid.isEmpty()) return;
		Vec3 vec = VecHelper.getCenterOf(worldPosition);
		vec = vec.add(0, 24 / 16f, 0);
		ParticleOptions particle = FluidFX.getFluidParticle(fluid);
		level.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, 0, -.1f, 0);
	}

	protected static int SPLASH_PARTICLE_COUNT = 1;

	protected void spawnSplash(FluidStack fluid) {
		if (isVirtual()) return;
		Vec3 vec = VecHelper.getCenterOf(worldPosition);
		vec = vec.add(0, 5 / 16f, 0);
		ParticleOptions particle = FluidFX.getFluidParticle(fluid);
		for (int i = 0; i < SPLASH_PARTICLE_COUNT; i++) {
			Vec3 m = VecHelper.offsetRandomly(Vec3.ZERO, level.random, 0.125f);
			m = new Vec3(m.x, Math.abs(m.y), m.z);
			level.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, m.x, m.y, m.z);
		}
	}

	protected void spawnCoolingParticles() {
		if (isVirtual()) return;
		Vec3 vec = VecHelper.getCenterOf(worldPosition);
		vec = vec.add(0, 5 / 16f, 0);
		for (int i = 0; i < 2; i++) {
			Vec3 m = VecHelper.offsetRandomly(Vec3.ZERO, level.random, 0.02f);
			m = new Vec3(0, Math.abs(m.y), 0);
			level.addAlwaysVisibleParticle(ParticleTypes.SMOKE,
					vec.x + level.random.nextFloat() - 0.5f,
					vec.y,
					vec.z + level.random.nextFloat() - 0.5f,
					m.x,
					m.y,
					m.z);
		}
	}

	public static CastingDepotBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CastingDepotBlockEntityImpl(type, pos, state);
	}

}
