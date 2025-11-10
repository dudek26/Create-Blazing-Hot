package com.dudko.blazinghot.multiloader.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;

public record MultiAmount(long droplets, int millibuckets, int millibucketsLegacy) {

	public static final MultiAmount EMPTY = standard(0);
	public static final MultiAmount BOTTLE = standard(20250);
	public static final MultiAmount BUCKET = standard(81000);
	public static final MultiAmount INGOT = metal(9000);
	public static final MultiAmount NUGGET = metal(1000);
	public static final MultiAmount BLOCK = metal(81000);
	public static final MultiAmount ROD = metal(4500);
	public static final MultiAmount INGOT_COVER = INGOT.multiply(6);
	public static final MultiAmount NUGGET_COVER = NUGGET.multiply(6);
	public static final MultiAmount RAW_ORE = NUGGET.multiply(12);

	/**
	 * Gets the platformed amount.
	 */
	public long get() {
		return get(this);
	}

	/**
	 * Gets the platformed amount.
	 */
	public long get(boolean legacy) {
		return get(this, legacy);
	}

	public MultiAmount multiply(float multiplier) {
		if (multiplier == 0) {
			return EMPTY;
		}
		return new MultiAmount((long) (droplets * multiplier),
				(int) (millibuckets * multiplier),
				(int) (millibucketsLegacy * multiplier));
	}

	public MultiAmount divide(float divider) {
		if (divider == 0) {
			return EMPTY;
		}
		return new MultiAmount((long) (droplets / divider),
				(int) (millibuckets / divider),
				(int) (millibucketsLegacy / divider));
	}

	/**
	 * Uses the default conversion rate of 81 droplets per millibucket
	 */
	public static MultiAmount standard(long droplets) {
		return new MultiAmount(droplets, (int) (droplets / 81), (int) (droplets / 81));
	}

	/**
	 * Uses the default conversion rate of 81 droplets per millibucket
	 */
	public static MultiAmount standardMb(int millibuckets) {
		return new MultiAmount(millibuckets * 81L, millibuckets, millibuckets);
	}

	public static MultiAmount metal(long droplets) {
		return new MultiAmount(droplets,
				(int) (droplets / MultiFluids.MELTABLE_CONVERSION),
				(int) (droplets / MultiFluids.MELTABLE_CONVERSION_LEGACY));
	}

	public static MultiAmount metalMb(int millibuckets) {
		return new MultiAmount((long) (millibuckets * MultiFluids.MELTABLE_CONVERSION),
				millibuckets,
				(int) (millibuckets * (MultiFluids.MELTABLE_CONVERSION_LEGACY / MultiFluids.MELTABLE_CONVERSION)));
	}

	public static MultiAmount fromBucketFraction(long numerator, long denominator) {
		long total = numerator * MultiAmount.BUCKET.droplets();

		if (total % denominator != 0) {
			throw new IllegalArgumentException("Not a valid number of droplets!");
		}

		return standard(total / denominator);
	}

	public static long get(MultiAmount amount) {
		return get(amount, false);
	}

	@ExpectPlatform
	public static long get(MultiAmount amount, boolean legacy) {
		throw new AssertionError();
	}

}
