package com.dudko.blazinghot.foundation.codec;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.Codec;

public class BlazingCodecs {

	public static <S> Codec<S> stringEnum(Class<S> clazz) {
		if (!clazz.isEnum()) {
			throw new IllegalArgumentException("Class is not an enum: " + clazz);
		}
		List<S> values = Arrays.asList(clazz.getEnumConstants());

		return Codec.stringResolver(e -> e == null ? null : e.toString().toLowerCase(), s -> values.stream()
			.filter(i -> i.toString().equals(s.toUpperCase()))
			.findFirst()
			.orElse(null));
	}

}
