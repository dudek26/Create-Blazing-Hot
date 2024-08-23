package com.dudko.blazinghot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListUtil {

	public static <T> List<T> compactLists(List<List<T>> lists) {
		List<T> list = new ArrayList<>();
		for (List<T> l : lists) {
			list.addAll(l);
		}
		return list;
	}

	@SafeVarargs
	public static <T> List<T> addIfAbsent(List<T> list, T... elements) {
		for (T element : elements) {
			if (!list.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}

	public static <T> List<T> addIfAbsent(List<T> list, Collection<T> elements) {
		for (T element : elements) {
			addIfAbsent(list, element);
		}
		return list;
	}

	public static <T> boolean containsAny(List<T> list, Collection<T> elements) {
		return elements.stream().anyMatch(list::contains);
	}

	@SafeVarargs
	public static <T> boolean containsAny(List<T> list, T... elements) {
		return Arrays.stream(elements).anyMatch(list::contains);
	}

}
