package com.dudko.blazinghot.content.metal;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.util.ListUtil;
import com.simibubi.create.foundation.utility.Pair;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.dudko.blazinghot.compat.Mods.CREATE_ADDITIONS;
import static com.dudko.blazinghot.compat.Mods.VANILLA;
import static com.dudko.blazinghot.registry.CommonTags.Namespace.INTERNAL;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;
import static com.dudko.blazinghot.util.LangUtil.titleCaseConversion;

@SuppressWarnings("unused")
public class MoltenMetal {

    public final String name;
    public final Mods mod;
    public final Forms[] supportedForms;
    public final Forms[] optionalForms;
    public final Map<Forms, Mods> compatForms;
    public final boolean ignoreTagGen;
    public final boolean mechanicalMixerMeltable;

    public final Map<Fluid, NonNullSupplier<Block>> fluidInteractions;

    public final Forms[] customForms;
    public final Pair<ResourceLocation, Long> compactingOverride;

    MoltenMetal(String name, Mods mod, Forms[] supportedForms, Forms[] optionalForms, Map<Forms, Mods> compatForms, boolean ignoreTagGen, boolean mechanicalMixerMeltable, Map<Fluid, NonNullSupplier<Block>> fluidInteractions, Forms[] customForms, Pair<ResourceLocation, Long> compactingOverride) {
        this.name = name;
        this.mod = mod;
        this.optionalForms = optionalForms;
        this.compatForms = compatForms;
        this.supportedForms = supportedForms;
        this.mechanicalMixerMeltable = mechanicalMixerMeltable;
        this.fluidInteractions = fluidInteractions;
        this.customForms = customForms;
        this.compactingOverride = compactingOverride;
        this.ignoreTagGen = ignoreTagGen;
    }

    /**
     * Automatically registers fluid, fluid interactions and adds this metal to the data generator
     */
    public MoltenMetal register() {
        MoltenMetals.ALL.add(this);
        return this;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public ResourceLocation ingotLocation() {
        if (compactingOverride != null) return compactingOverride.getFirst();
        return Forms.INGOT.resourceLocation(this);
    }

    public Pair<ItemLike, Long> compactingResult() {
        return Pair.of(MultiRegistries.getItemFromRegistry(ingotLocation()).get(),
                (compactingOverride != null ? compactingOverride.getSecond() : Forms.INGOT.amount));
    }

    public ResourceLocation fluidLocation() {
        return BlazingHot.asResource(moltenName());
    }

    public Supplier<Fluid> fluid() {
        return MultiRegistries.getFluidFromRegistry(fluidLocation());
    }

    public Supplier<ItemLike> bucket() {
        return () -> fluid().get().getBucket();
    }

    /**
     * @see BlazingAdvancements#ALL_MOLTEN_METALS
     */
    public static Set<ItemLike> allBuckets(boolean includeCompat) {
        return MoltenMetals.ALL.stream()
                .filter(m -> m.mod.alwaysIncluded || includeCompat)
                .map(m -> m.bucket().get())
                .collect(Collectors.toSet());
    }

    public TagKey<Fluid> fluidTag() {
        return CommonTags.fluidTagOf(moltenName(), INTERNAL);
    }

    public String moltenName() {
        return "molten_" + name;
    }

    public List<Forms> nonCustomForms() {
        List<Forms> all = new ArrayList<>();
        ListUtil.addIfAbsent(all, supportedForms);
        ListUtil.addIfAbsent(all, compatForms.keySet());
        return all;
    }

    public List<Forms> supportedForms() {
        return new ArrayList<>(List.of(supportedForms));
    }

    public List<Forms> customForms() {
        return new ArrayList<>(List.of(customForms));
    }

    public Map<Fluid, NonNullSupplier<Block>> getFluidInteractions() {
        HashMap<Fluid, NonNullSupplier<Block>> interactions = new HashMap<>(fluidInteractions);
        if (!interactions.containsKey(Fluids.WATER))
            interactions.put(Fluids.WATER, () -> Blocks.COBBLESTONE);
        return interactions;
    }

    public static class Builder {

        private final String name;
        private Mods mod;
        private final List<Forms> supportedForms = new ArrayList<>();
        private final List<Forms> optionalForms = new ArrayList<>();
        private final HashMap<Forms, Mods> compatForms = new HashMap<>();
        private boolean ignoreTagDatagen;
        private final List<Forms> customForms = new ArrayList<>();
        private boolean mechanicalMixerMeltable = true;
        private final HashMap<Fluid, NonNullSupplier<Block>> fluidInteractions = new HashMap<>();

        private Pair<ResourceLocation, Long> compactingOverride;

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
         * <p>Defines the fluid interactions. If no interaction with water is specified, it will default to Cobblestone.</p>
         * <p>Never add AllPaletteStoneTypes directly by {@code [...].getBaseBlock()}! You should always add them by <code>() -> [...].getBaseBlock().get()</code></p>
         * <p>Fabric: remember to update {@link com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl#fluidTags}</p>
         */
        public Builder addFluidInteraction(Fluid fluid, NonNullSupplier<Block> block) {
            this.fluidInteractions.put(fluid, block);
            return this;
        }


        /**
         * For forms added by the main mod of the metal
         */
        public Builder supportedForms(Forms... supportedForms) {
            this.supportedForms.addAll(List.of(supportedForms));
            return this;
        }

        /**
         * For tag-based forms that are not present in Vanilla or create.
         */
        public Builder optionalForms(Forms... optionalForms) {
            this.optionalForms.addAll(List.of(optionalForms));
            return this;
        }

        /**
         * For item-based forms that are added by a different mod than the main one.
         */
        public Builder compatForm(Forms form, Mods mod) {
            this.compatForms.put(form, mod);
            return this;
        }

        public Builder customForm(ResourceLocation item, long amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable) {
            this.customForms.add(Forms.custom(item, amount, processingTime, fuelCost, mechanicalMixerMeltable));
            return this;
        }

        // Shortcuts

        /**
         * For Vanilla and Create metals except Zinc: Ingot, Nugget, Plate + Create Crafts & Additions Rod compat <br>
         */
        public Builder createForms() {
            return basicAndPlateForms().compatForm(Forms.ROD, CREATE_ADDITIONS);
        }

        /**
         * For metals that also have a plate form
         */
        public Builder basicAndPlateForms() {
            return basicForms().supportedForms(Forms.PLATE);
        }

        /**
         * For metals that only have ingot and nugget forms
         */
        public Builder basicForms() {
            return supportedForms(Forms.INGOT, Forms.NUGGET);
        }

        /**
         * If compacting the molten metal should return something else than an item that starts with the metal's name and ends with <code>_ingot</code>
         *
         * @param item     Resource location of the item to return
         * @param droplets Amount of droplets required to compact one item
         */
        public Builder compactingOverride(ResourceLocation item, long droplets) {
            this.compactingOverride = Pair.of(item, droplets);
            return this;
        }

        /**
         * If called, the data generator will not automatically create tags for all supported forms (including the internal tag). Use when the metal doesn't have common tags (like Andesite Alloy)
         */
        public Builder ignoreTagDatagen() {
            this.ignoreTagDatagen = true;
            return this;
        }

        /**
         * Disables melting in Mechanical Mixer for ALL forms of this metal.
         */
        public Builder disableMechanicalMixing() {
            this.mechanicalMixerMeltable = false;
            return this;
        }

        public MoltenMetal build() {
            return new MoltenMetal(name,
                    mod == null ? VANILLA : mod,
                    supportedForms.toArray(Forms[]::new),
                    optionalForms.toArray(Forms[]::new),
                    compatForms,
                    ignoreTagDatagen,
                    mechanicalMixerMeltable,
                    fluidInteractions,
                    customForms.toArray(Forms[]::new), compactingOverride);
        }

        /**
         * Shortcut for building and registering the Molten Metal
         */
        public MoltenMetal register() {
            return build().register();
        }
    }

    public static void provideLangEntries(BiConsumer<String, String> consumer) {

        for (MoltenMetal metal : MoltenMetals.ALL) {
            for (CommonTags.Namespace namespace : CommonTags.Namespace.values()) {
                ResourceLocation fluidLoc = metal.fluidTag().location();
                consumer.accept(
                        "tag.fluid." + namespace.namespace + "." + fluidLoc.getPath().replace('/', '.'),
                        titleCaseConversion(metal.fluidLocation().getPath().replace('_', ' ')));

                for (Forms form : metal.nonCustomForms()) {
                    TagKey<Item> tag = itemTagOf(namespace.tagPath(form.tagFolder, metal.name), namespace);
                    ResourceLocation loc = tag.location();
                    consumer.accept("tag.item." + namespace.namespace + "." + loc.getPath().replace('/', '.'),
                            titleCaseConversion((metal.name + ' ' + form.tagFolder).replace('_', ' ')));
                }
            }
        }

    }

}
