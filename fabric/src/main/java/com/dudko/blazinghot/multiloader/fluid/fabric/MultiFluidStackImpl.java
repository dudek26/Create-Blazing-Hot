package com.dudko.blazinghot.multiloader.fluid.fabric;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.Create;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class MultiFluidStackImpl {
	@SuppressWarnings("ConstantValue")
	public static MultiFluidStack deserializeFluidStack(JsonObject json) {
		ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
		Fluid fluid = BuiltInRegistries.FLUID.get(id);
		if (fluid == null) throw new JsonSyntaxException("Unknown fluid '" + id + "'");
		if (fluid == Fluids.EMPTY) throw new JsonSyntaxException("Invalid empty fluid '" + id + "'");
		long amount = GsonHelper.getAsLong(json, "amount");
		MultiFluidStack stack = new MultiFluidStack(fluid, MultiAmount.standard(amount));
		if (!json.has("nbt")) return stack;

		try {
			JsonElement element = json.get("nbt");
			CompoundTag
					nbt =
					TagParser.parseTag(element.isJsonObject() ?
									   Create.GSON.toJson(element) :
									   GsonHelper.convertToString(element, "nbt"));
			stack.setTag(nbt);
		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException("Failed to read NBT", e);
		}

		return stack;
	}
}
