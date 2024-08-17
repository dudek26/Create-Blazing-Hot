package com.dudko.blazinghot.content.metal;

import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.multiloader.MultiFluids;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.defaultDurationToFuelCost;
import static com.dudko.blazinghot.registry.CommonTags.Namespace.INTERNAL;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;

public class Form {

    public static final Form
            INGOT = Form.of("ingots", MultiFluids.Constants.INGOT, 400, true),
            NUGGET = Form.of("nuggets", MultiFluids.Constants.NUGGET, 65, true),
            PLATE = Form.of("plates", MultiFluids.Constants.PLATE, 400, true),
            ROD = Form.of("rods", MultiFluids.Constants.ROD, 250, true);

    public String tagFolder = null;
    public final long amount;
    public final int processingTime;
    public final long fuelCost;
    public final boolean mechanicalMixerMeltable;
    public ResourceLocation customLocation = null;

    Form(ResourceLocation customLocation, long amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable) {
        this.customLocation = customLocation;
        this.amount = amount;
        this.processingTime = processingTime;
        this.mechanicalMixerMeltable = mechanicalMixerMeltable;
        this.fuelCost = fuelCost;
    }

    Form(String tagFolder, long amount, int processingTime, boolean mechanicalMixerMeltable) {
        this.tagFolder = tagFolder;
        this.amount = amount;
        this.processingTime = processingTime;
        this.mechanicalMixerMeltable = mechanicalMixerMeltable;
        this.fuelCost = defaultDurationToFuelCost(processingTime);
    }

    Form(String tagFolder, MultiFluids.Constants fluidConstant, int processingTime, boolean mechanicalMixerMeltable) {
        this(tagFolder, fluidConstant.platformed(), processingTime, mechanicalMixerMeltable);
    }

    public TagKey<Item> internalTag(String material) {
        return itemTagOf(INTERNAL.tagPath(tagFolder, material), INTERNAL);
    }

    public TagKey<Item> internalTag(MoltenMetal metal) {
        return internalTag(metal.name);
    }

    public String simpleItemName(MoltenMetal metal) {
        return metal.name + "_" + (this.tagFolder.endsWith("s") ? this.tagFolder.substring(0,
                this.tagFolder.length() - 1) : this.tagFolder);
    }

    public ResourceLocation resourceLocation(MoltenMetal metal) {
        return resourceLocation(metal, metal.mod);
    }

    public ResourceLocation resourceLocation(MoltenMetal metal, Mods mod) {
        return mod.asResource(simpleItemName(metal));
    }

    public static Form of(String tagFolder, MultiFluids.Constants fluidConstant, int processingTime, boolean mechanicalMixerMeltable) {
        return new Form(tagFolder, fluidConstant, processingTime, mechanicalMixerMeltable);
    }

    public static Form custom(ResourceLocation location, long amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable) {
        return new Form(location, amount, processingTime, fuelCost, mechanicalMixerMeltable);
    }
}
