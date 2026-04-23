package com.dudko.blazinghot.util;

import java.util.ArrayList;
import java.util.List;

import net.createmod.catnip.data.Pair;

public class RandomUtil {

	private static <T> List<Pair<T, Double>> convertList(List<Pair<T, Double>> list) {
		double d = 0;
		List<Pair<T, Double>> result = new ArrayList<>();
		for (Pair<T, Double> pair : list) {
			d += pair.getSecond();
			result.add(Pair.of(pair.getFirst(), d));
		}
		return result;
	}

	public static <T> T rollFromPairList(List<Pair<T, Double>> list) {
		List<Pair<T, Double>> converted = convertList(list);
		double randomDouble = Math.random();

		for (Pair<T, Double> pair : converted) {
			if (randomDouble <= pair.getSecond()) {
				return pair.getFirst();
			}
		}

		return list.getFirst().getFirst();
	}

}
