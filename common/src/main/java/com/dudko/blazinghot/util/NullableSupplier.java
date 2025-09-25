package com.dudko.blazinghot.util;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface NullableSupplier<T> extends Supplier<T> {
	@Override
	@Nullable T get();
}
