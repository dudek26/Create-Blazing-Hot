package com.dudko.blazinghot.data.conditions.forge;

import com.dudko.blazinghot.BlazingHot;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public record LegacyFluidCondition(boolean useLegacyFluidAmounts) implements ICondition {

	public static final ResourceLocation NAME = BlazingHot.asResource("legacy_fluid_amounts");

	@Override
	public ResourceLocation getID() {
		return NAME;
	}

	@Override
	public boolean test(IContext context) {
		return BlazingHot.USE_LEGACY_FLUID_AMOUNTS == useLegacyFluidAmounts;
	}

	public static class Serializer implements IConditionSerializer<LegacyFluidCondition> {

		public static final Serializer INSTANCE = new Serializer();

		@Override
		public void write(JsonObject json, LegacyFluidCondition value) {
			json.addProperty("value", value.useLegacyFluidAmounts);
		}

		@Override
		public LegacyFluidCondition read(JsonObject json) {
			return new LegacyFluidCondition(GsonHelper.getAsBoolean(json, "value", false));
		}

		@Override
		public ResourceLocation getID() {
			return NAME;
		}
	}
}
