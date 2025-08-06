package com.dudko.blazinghot.multiloader.fluid.forge;

import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.multiloader.fluid.MultiFluidStack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.simibubi.create.Create;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class MultiFluidStackImpl {

	public static MultiFluidStack deserializeFluidStack(JsonObject json) {
		ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
		Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
		if (fluid == null) throw new JsonSyntaxException("Unknown fluid '" + id + "'");
		if (fluid == Fluids.EMPTY) throw new JsonSyntaxException("Invalid empty fluid '" + id + "'");
		int amount = GsonHelper.getAsInt(json, "amount");

		MultiFluidStack stack = new MultiFluidStack(fluid, MultiAmount.standardMb(amount));
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
