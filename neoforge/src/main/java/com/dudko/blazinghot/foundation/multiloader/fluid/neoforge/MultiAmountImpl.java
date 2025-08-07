package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;

public class MultiAmountImpl {
	public static long get(MultiAmount amount) {
		return amount.millibuckets();
	}
}
