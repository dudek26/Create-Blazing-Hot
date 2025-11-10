package com.dudko.blazinghot.multiloader.fluid;

import com.google.gson.JsonObject;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class MultiFluidStack {

	public static final MultiFluidStack EMPTY = new MultiFluidStack(Fluids.EMPTY, MultiAmount.EMPTY);

	private final Fluid fluid;
	private final MultiAmount amount;
	private final boolean legacy;
	private CompoundTag tag;

	public MultiFluidStack(Fluid fluid, MultiAmount amount) {
		this.fluid = fluid;
		this.amount = amount;
		this.legacy = false;
	}

	public MultiFluidStack(Fluid fluid, MultiAmount amount, boolean legacy) {
		this.fluid = fluid;
		this.amount = amount;
		this.legacy = legacy;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public MultiAmount getAmount() {
		return amount;
	}

	public boolean hasTag() {
		return this.tag != null;
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

	public CompoundTag getOrCreateTag() {
		if (this.tag == null) {
			this.setTag(new CompoundTag());
		}

		return this.tag;
	}

	public CompoundTag getChildTag(String childName) {
		return this.tag == null ? null : this.tag.getCompound(childName);
	}

	public CompoundTag getOrCreateChildTag(String childName) {
		this.getOrCreateTag();
		CompoundTag child = this.tag.getCompound(childName);
		if (!this.tag.contains(childName, 10)) {
			this.tag.put(childName, child);
		}

		return child;
	}

	public void removeChildTag(String childName) {
		if (this.tag != null) {
			this.tag.remove(childName);
		}

	}

	@ExpectPlatform
	public static MultiFluidStack deserializeFluidStack(JsonObject json) {
		throw new AssertionError();
	}

	public boolean isLegacy() {
		return legacy;
	}
}
