package com.dudko.blazinghot.content.item.food;

public record BlazingFoodProperty<T>(Type type, T value) {

	public enum Type {
		EXTINGUISHING,
		OXYGEN,
		FOIL,
		REMOVE_SLOWNESS
	}

}
