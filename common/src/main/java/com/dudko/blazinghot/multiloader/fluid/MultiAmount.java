package com.dudko.blazinghot.multiloader.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class MultiAmount {

	public static final MultiAmount EMPTY = standard(0);
	public static final MultiAmount INGOT = metal(9000);
	public static final MultiAmount NUGGET = metal(1000);
	public static final MultiAmount BLOCK = metal(81000);
	public static final MultiAmount ROD = metal(4500);

	public final long droplets;
	public final int millibuckets;

	public MultiAmount(long droplets, int millibuckets) {
		this.droplets = droplets;
		this.millibuckets = millibuckets;
	}

	/**
	 * Gets the platformed amount.
	 */
	public long get() {
		return get(this);
	}

	/**
	 * Uses the default conversion rate of 81 droplets per millibucket
	 */
	public static MultiAmount standard(long droplets) {
		return new MultiAmount(droplets, (int) (droplets / 81));
	}

	/**
	 * Uses the default conversion rate of 81 droplets per millibucket
	 */
	public static MultiAmount standardMb(int millibuckets) {
		return new MultiAmount(millibuckets * 81L, millibuckets);
	}

	public static MultiAmount metal(long droplets) {
		return new MultiAmount(droplets, (int) (droplets / MultiFluids.MELTABLE_CONVERSION));
	}

	public static MultiAmount metalMb(int millibuckets) {
		return new MultiAmount((long) (millibuckets * MultiFluids.MELTABLE_CONVERSION), millibuckets);
	}

	@ExpectPlatform
	public static long get(MultiAmount amount) {
		throw new AssertionError();
	}

}
