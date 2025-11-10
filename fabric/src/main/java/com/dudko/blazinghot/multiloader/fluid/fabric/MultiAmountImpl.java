package com.dudko.blazinghot.multiloader.fluid.fabric;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;

public class MultiAmountImpl {
	public static long get(MultiAmount amount, boolean legacy) {
		return amount.droplets();
	}
}
