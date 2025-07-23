package com.dudko.blazinghot.content.metal;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.defaultDurationToFuelCost;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.CommonTags.Namespace;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Forms {

	public static final Forms INGOT = Forms.of("ingot", "ingots", MultiAmount.INGOT, 400, true, Molds.INGOT),
			NUGGET =
					Forms.of("nugget", "nuggets", MultiAmount.NUGGET, 65, true, Molds.NUGGET),
			PLATE =
					Forms.of("sheet", "plates", MultiAmount.INGOT, 400, true, Molds.SHEET),
			ROD =
					Forms.of("rod", "rods", MultiAmount.ROD, 250, true, Molds.ROD),
			WIRE =
					Forms.of("wire", "wires", MultiAmount.ROD, 250, true, null);

	public final String name;
	public final @Nullable String tagFolder;
	public final MultiAmount amount;
	public final int processingTime;
	public final long fuelCost;
	public final boolean mechanicalMixerMeltable;
	public final @Nullable ResourceLocation customLocation;
	public final @Nullable Molds.Mold mold;

	Forms(String name, @NotNull ResourceLocation customLocation, MultiAmount amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable, @Nullable Molds.Mold mold) {
		this.name = name;
		this.customLocation = customLocation;
		this.amount = amount;
		this.processingTime = processingTime;
		this.mechanicalMixerMeltable = mechanicalMixerMeltable;
		this.fuelCost = fuelCost;
		this.mold = mold;
		tagFolder = null;
	}

	Forms(String name, @NotNull String tagFolder, MultiAmount amount, int processingTime, boolean mechanicalMixerMeltable, @Nullable Molds.Mold mold) {
		this.tagFolder = tagFolder;
		this.amount = amount;
		this.processingTime = processingTime;
		this.mechanicalMixerMeltable = mechanicalMixerMeltable;
		this.fuelCost = defaultDurationToFuelCost(processingTime);
		this.name = name;
		this.mold = mold;
		this.customLocation = null;
	}

	public TagKey<Item> tag(String material) {
		return itemTagOf(Namespace.platform().tagPath(tagFolder, material), Namespace.platform());
	}

	public TagKey<Item> tag(MoltenMetal metal) {
		return tag(metal.name);
	}

	public String simpleItemName(MoltenMetal metal) {
		return metal.name + "_" + this.name;
	}

	public ResourceLocation resourceLocation(MoltenMetal metal) {
		return resourceLocation(metal, metal.mod);
	}

	public ResourceLocation resourceLocation(MoltenMetal metal, Mods mod) {
		if (customLocation != null) return customLocation;
		return mod.asResource(simpleItemName(metal));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Forms form) {
			return Objects.equals(form.tagFolder, this.tagFolder) && Objects.equals(form.customLocation,
					customLocation);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (tagFolder + "&" + customLocation).hashCode();
	}

	public static Forms of(String name, String tagFolder, MultiAmount amount, int processingTime, boolean mechanicalMixerMeltable, @Nullable Molds.Mold mold) {
		return new Forms(name, tagFolder, amount, processingTime, mechanicalMixerMeltable, mold);
	}

	public static Forms custom(String name, ResourceLocation location, MultiAmount amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable, @Nullable Molds.Mold mold) {
		return new Forms(name, location, amount, processingTime, fuelCost, mechanicalMixerMeltable, mold);
	}
}
