package com.dudko.blazinghot.multiloader.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class MultiFluidStack {

	public static final MultiFluidStack EMPTY = new MultiFluidStack(Fluids.EMPTY, MultiAmount.EMPTY);

	private final Fluid fluid;
	private final MultiAmount amount;
	private CompoundTag tag;

	public MultiFluidStack(Fluid fluid, MultiAmount amount) {
		this.fluid = fluid;
		this.amount = amount;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public MultiAmount getAmount() {
		return amount;
	}

	public CompoundTag getTag() {
		return this.tag;
	}

	public void setTag(CompoundTag tag) {
		if (this.getFluid() == Fluids.EMPTY) {
			throw new IllegalStateException("Can't modify the empty stack.");
		}
		else {
			this.tag = tag;
		}
	}

}
