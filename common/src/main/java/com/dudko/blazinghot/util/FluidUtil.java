package com.dudko.blazinghot.util;

import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.Create;

import dev.architectury.fluid.FluidStack;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidUtil {

	public static FluidStack deserializeFluidStack(JsonObject json) {
		ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
		Fluid fluid = MultiRegistries.getFluidFromRegistry(id).get();
		if (fluid == null) throw new JsonSyntaxException("Unknown fluid '" + id + "'");
		if (fluid == Fluids.EMPTY) throw new JsonSyntaxException("Invalid empty fluid '" + id + "'");
		int amount = GsonHelper.getAsInt(json, "amount");
		FluidStack stack = FluidStack.create(fluid, amount);

		if (!json.has("nbt")) return stack;

		try {
			JsonElement element = json.get("nbt");
			stack.setTag(TagParser.parseTag(element.isJsonObject() ?
											Create.GSON.toJson(element) :
											GsonHelper.convertToString(element, "nbt")));

		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException("Failed to read NBT", e);
		}

		return stack;
	}

}
