package com.dudko.blazinghot.data.recipe.fabric;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

public class BlazingSequencedRecipe<T extends ProcessingRecipe<?>> extends SequencedRecipe<T> {

	private final boolean convertMeltable;

	public BlazingSequencedRecipe(T wrapped, boolean convertMeltable) {
		super(wrapped);
		this.convertMeltable = convertMeltable;
	}

	@Override
	public JsonObject toJson() {
		JsonObject json = super.toJson();
		if (convertMeltable) json.addProperty("blazinghot:convertMeltable", true);
		return json;
	}
}
