package com.dudko.blazinghot.foundation.multiloader.fluid.neoforge;

import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;

public class MultiAmountImpl {
	public static long get(MultiAmount amount) {
		return amount.millibuckets();
	}

	public static MultiAmount from(long amount) {
		return MultiAmount.standardMb((int) amount);
	}
}
