package com.dudko.blazinghot.content.metal;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.defaultDurationToFuelCost;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.CommonTags.Namespace;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Forms {

	public static final Forms INGOT = Forms.of("ingots", MultiAmount.INGOT, 400, true),
			NUGGET =
					Forms.of("nuggets", MultiAmount.NUGGET, 65, true),
			PLATE =
					Forms.of("plates", MultiAmount.INGOT, 400, true),
			ROD =
					Forms.of("rods", MultiAmount.ROD, 250, true), WIRE = Forms.of("wires", MultiAmount.ROD, 250, true);

	public String tagFolder = null;
	public final MultiAmount amount;
	public final int processingTime;
	public final long fuelCost;
	public final boolean mechanicalMixerMeltable;
	public ResourceLocation customLocation = null;

	Forms(ResourceLocation customLocation, MultiAmount amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable) {
		this.customLocation = customLocation;
		this.amount = amount;
		this.processingTime = processingTime;
		this.mechanicalMixerMeltable = mechanicalMixerMeltable;
		this.fuelCost = fuelCost;
	}

	Forms(String tagFolder, MultiAmount amount, int processingTime, boolean mechanicalMixerMeltable) {
		this.tagFolder = tagFolder;
		this.amount = amount;
		this.processingTime = processingTime;
		this.mechanicalMixerMeltable = mechanicalMixerMeltable;
		this.fuelCost = defaultDurationToFuelCost(processingTime);
	}

	public TagKey<Item> tag(String material) {
		return itemTagOf(Namespace.platform().tagPath(tagFolder, material), Namespace.platform());
	}

	public TagKey<Item> tag(MoltenMetal metal) {
		return tag(metal.name);
	}

	public String simpleItemName(MoltenMetal metal) {
		return metal.name + "_" + (this.tagFolder.endsWith("s") ?
								   this.tagFolder.substring(0, this.tagFolder.length() - 1) :
								   this.tagFolder);
	}

	public ResourceLocation resourceLocation(MoltenMetal metal) {
		return resourceLocation(metal, metal.mod);
	}

	public ResourceLocation resourceLocation(MoltenMetal metal, Mods mod) {
		return mod.asResource(simpleItemName(metal));
	}

	public static Forms of(String tagFolder, MultiAmount amount, int processingTime, boolean mechanicalMixerMeltable) {
		return new Forms(tagFolder, amount, processingTime, mechanicalMixerMeltable);
	}

	public static Forms custom(ResourceLocation location, MultiAmount amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable) {
		return new Forms(location, amount, processingTime, fuelCost, mechanicalMixerMeltable);
	}
}
