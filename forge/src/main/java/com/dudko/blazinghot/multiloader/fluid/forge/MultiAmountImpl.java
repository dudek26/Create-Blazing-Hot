package com.dudko.blazinghot.multiloader.fluid.forge;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;

public class MultiAmountImpl {
	public static long get(MultiAmount amount, boolean legacy) {
		return legacy ? amount.millibucketsLegacy() : amount.millibuckets();
	}
}
