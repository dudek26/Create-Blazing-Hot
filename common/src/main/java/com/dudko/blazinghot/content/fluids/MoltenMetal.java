package com.dudko.blazinghot.content.fluids;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.CommonTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Supplier;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.defaultDurationToFuelCost;

@SuppressWarnings("unused")
public class MoltenMetal {

    public static final List<MoltenMetal> ALL_METALS = new ArrayList<>();

    public static MoltenMetal
            IRON = builder("iron").createForms().register(),
            GOLD = builder("gold").createForms().register(),
            BLAZE_GOLD =
                    MoltenMetal.builder("blaze_gold")
                            .mod(Mods.BLAZINGHOT)
                            .basicAndPlateForms()
                            .supportedForms(Forms.ROD)
                            .register();

    public static void init() {

    }

    public final String name;
    public final Mods mod;
    public final Forms[] supportedForms;
    public final Map<Forms, Mods> compatForms;

    public final ResourceLocation ingotOverride;

    MoltenMetal(String name, Mods mod, Forms[] supportedForms, Map<Forms, Mods> compatForms, ResourceLocation ingotOverride) {
        this.name = name;
        this.mod = mod;
        this.compatForms = compatForms;
        this.supportedForms = supportedForms;
        this.ingotOverride = ingotOverride;
    }

    public MoltenMetal register() {
        ALL_METALS.add(this);
        return this;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public ResourceLocation ingotLocation() {
        if (ingotOverride != null) return ingotOverride;
        return Forms.INGOT.resourceLocation(this);
    }

    public Supplier<Item> ingot() {
        return MultiRegistries.getItemFromRegistry(ingotLocation());
    }

    public ResourceLocation fluidLocation() {
        return BlazingHot.asResource("molten_" + name);
    }

    public Supplier<Fluid> fluid() {
        return MultiRegistries.getFluidFromRegistry(fluidLocation());
    }

    public TagKey<Fluid> fluidTag() {
        return CommonTags.internalFluidTagOf("molten_" + name);
    }

    public enum Forms {
        BLOCK("blocks", Constants.BLOCK, 2400, false, true),
        INGOT("ingots", Constants.INGOT, 400, true, true),
        NUGGET("nuggets", Constants.NUGGET, 65, true, true),
        PLATE("plates", Constants.PLATE, 400, true, true),
        ROD("rods", Constants.ROD, 250, true, false);

        public final String tagFolder;
        public final long amount;
        public final int processingTime;
        public final long fuelCost;
        public final boolean mechanicalMixerMeltable;
        public final boolean alwaysGen;

        Forms(String tagFolder, long amount, int processingTime, boolean mechanicalMixerMeltable, boolean alwaysGen) {
            this.tagFolder = tagFolder;
            this.amount = amount;
            this.processingTime = processingTime;
            this.mechanicalMixerMeltable = mechanicalMixerMeltable;
            this.alwaysGen = alwaysGen;
            this.fuelCost = defaultDurationToFuelCost(processingTime);
        }

        Forms(String tagFolder, Constants fluidConstant, int processingTime, boolean mechanicalMixerMeltable, boolean alwaysGen) {
            this(tagFolder, fluidConstant.platformed(), processingTime, mechanicalMixerMeltable, alwaysGen);
        }

        public TagKey<Item> internalTag(String material) {
            return CommonTags.internalItemTagOf(material + "_" + tagFolder);
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
    }

    public static class Builder {

        public final String name;
        public Mods mod;
        public List<Forms> supportedForms = new ArrayList<>();
        public HashMap<Forms, Mods> compatForms = new HashMap<>();

        public ResourceLocation ingotOverride;

        protected Builder(String name) {
            this.name = name;
        }

        /**
         * Set the mod that adds the metal to the game. If unset, defaults to Vanilla
         */
        public Builder mod(Mods mod) {
            this.mod = mod;
            return this;
        }

        /**
         * For Vanilla and Create metals: Block, Ingot, Nugget, Plate (excluding zinc) + Create Crafts & Additions Rod compat <br>
         */
        public Builder createForms() {
            return basicAndPlateForms().compatForm(Forms.ROD, Mods.CREATE_ADDITIONS);
        }

        /**
         * For metals that also have a plate form
         */
        public Builder basicAndPlateForms() {
            Builder builder = basicForms();
            if (!name.equals("zinc")) builder.supportedForms(Forms.PLATE);
            return builder;
        }

        /**
         * For metals that only have a block, ingot and nugget form
         */
        public Builder basicForms() {
            return supportedForms(Arrays.stream(Forms.values())
                    .filter(form -> form.alwaysGen)
                    .toArray(Forms[]::new));
        }

        /**
         * For forms added by the main mod of the metal
         */
        public Builder supportedForms(Forms... supportedForms) {
            this.supportedForms.addAll(List.of(supportedForms));
            return this;
        }

        /**
         * For forms that are added by a different mod than the main one.
         */
        public Builder compatForm(Forms form, Mods mod) {
            this.compatForms.put(form, mod);
            return this;
        }

        /**
         * If compacting the molten metal should return something else than an item that starts with the metal's name and ends with <code>_ingot</code>
         */
        public Builder ingotOverride(ResourceLocation ingotOverride) {
            this.ingotOverride = ingotOverride;
            return this;
        }

        public MoltenMetal build() {
            return new MoltenMetal(name,
                    mod == null ? Mods.VANILLA : mod,
                    supportedForms.toArray(Forms[]::new),
                    compatForms,
                    ingotOverride);
        }

        /**
         * Shortcut for building and registering the Molten Metal
         */
        public MoltenMetal register() {
            return build().register();
        }
    }

}
