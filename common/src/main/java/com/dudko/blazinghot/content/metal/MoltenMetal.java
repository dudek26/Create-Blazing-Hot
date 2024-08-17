package com.dudko.blazinghot.content.metal;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.multiloader.MultiFluids.Constants;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.util.ListUtil;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.dudko.blazinghot.compat.Mods.*;
import static com.dudko.blazinghot.registry.CommonTags.Namespace.INTERNAL;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;
import static com.dudko.blazinghot.util.LangUtil.titleCaseConversion;

@SuppressWarnings("unused")
public class MoltenMetal {

    public static final List<MoltenMetal> ALL_METALS = new ArrayList<>();

    public static MoltenMetal
            IRON = builder("iron").createForms().register(),
            GOLD = builder("gold").createForms().register(),
            COPPER = builder("copper").createForms().register(),
            ANCIENT_DEBRIS = builder("ancient_debris")
                    .customForm(VANILLA.asResource("netherite_scrap"),
                            Constants.INGOT.droplets,
                            Form.INGOT.processingTime * 2,
                            Form.INGOT.fuelCost * 2,
                            false)
                    .customForm(VANILLA.asResource("ancient_debris"),
                            Constants.NUGGET.droplets * 12,
                            Form.INGOT.processingTime * 2,
                            Form.INGOT.fuelCost * 2,
                            false)
                    .compactingOverride(VANILLA.asResource("netherite_scrap"), Constants.INGOT.droplets)
                    .addFluidInteraction(Fluids.WATER,
                            () -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
                    .disableMechanicalMixing()
                    .ignoreTagDatagen()
                    .register(),
            NETHERITE =
                    builder("netherite").supportedForms(Form.INGOT)
                            .disableMechanicalMixing()
                            .addFluidInteraction(Fluids.WATER,
                                    () -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
                            .register(),
            BLAZE_GOLD =
                    builder("blaze_gold").mod(BLAZINGHOT)
                            .basicAndPlateForms()
                            .supportedForms(Form.ROD)
                            .addFluidInteraction(Fluids.WATER, () -> Blocks.NETHERRACK)
                            .register(),
            ZINC = builder("zinc").mod(CREATE).basicForms().optionalForms(Form.PLATE).register(),
            BRASS = builder("brass").mod(CREATE).createForms().register(),
            ANDESITE = builder("andesite").mod(CREATE)
                    .customForm(AllItems.ANDESITE_ALLOY.getId(),
                            Form.INGOT.amount,
                            Form.INGOT.processingTime,
                            Form.INGOT.fuelCost,
                            true)
                    .compactingOverride(AllItems.ANDESITE_ALLOY.getId(), Constants.INGOT.droplets)
                    .ignoreTagDatagen()
                    .register();

    public static void init() {

    }

    public final String name;
    public final Mods mod;
    public final Form[] supportedForms;
    public final Form[] optionalForms;
    public final Map<Form, Mods> compatForms;
    public final boolean ignoreTagGen;
    public final boolean mechanicalMixerMeltable;

    public final Map<Fluid, NonNullSupplier<Block>> fluidInteractions;

    public final Form[] customForms;
    public final Pair<ResourceLocation, Long> compactingOverride;

    MoltenMetal(String name, Mods mod, Form[] supportedForms, Form[] optionalForms, Map<Form, Mods> compatForms, boolean ignoreTagGen, boolean mechanicalMixerMeltable, Map<Fluid, NonNullSupplier<Block>> fluidInteractions, Form[] customForms, Pair<ResourceLocation, Long> compactingOverride) {
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
     * Automatically registers fluid and adds this metal to the data generator
     */
    public MoltenMetal register() {
        ALL_METALS.add(this);
        return this;
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public ResourceLocation ingotLocation() {
        if (compactingOverride != null) return compactingOverride.getFirst();
        return Form.INGOT.resourceLocation(this);
    }

    public Pair<ItemLike, Long> compactingResult() {
        return Pair.of(MultiRegistries.getItemFromRegistry(ingotLocation()).get(),
                (compactingOverride != null ? compactingOverride.getSecond() : Form.INGOT.amount));
    }

    public ResourceLocation fluidLocation() {
        return BlazingHot.asResource(moltenName());
    }

    public Supplier<Fluid> fluid() {
        return MultiRegistries.getFluidFromRegistry(fluidLocation());
    }

    public TagKey<Fluid> fluidTag() {
        return CommonTags.fluidTagOf(moltenName(), INTERNAL);
    }

    public String moltenName() {
        return "molten_" + name;
    }

    public List<Form> nonCustomForms() {
        List<Form> all = new ArrayList<>();
        ListUtil.addIfAbsent(all, supportedForms);
        ListUtil.addIfAbsent(all, compatForms.keySet());
        return all;
    }

    public List<Form> supportedForms() {
        return new ArrayList<>(List.of(supportedForms));
    }

    public List<Form> customForms() {
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
        private final List<Form> supportedForms = new ArrayList<>();
        private final List<Form> optionalForms = new ArrayList<>();
        private final HashMap<Form, Mods> compatForms = new HashMap<>();
        private boolean ignoreTagDatagen;
        private final List<Form> customForms = new ArrayList<>();
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
         * Defines the fluid interactions. If no interaction with water is specified, it will default to Cobblestone.
         * <ul>
         *      <li>Never add AllPaletteStoneTypes directly by {@code [...].getBaseBlock()}! You should always add them by <code>() -> [...].getBaseBlock().get()</code></li>
         *      <li>Fabric: remember to update {@link com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl#fluidTags}</li>
         * </ul>
         */
        public Builder addFluidInteraction(Fluid fluid, NonNullSupplier<Block> block) {
            this.fluidInteractions.put(fluid, block);
            return this;
        }


        /**
         * For forms added by the main mod of the metal
         */
        public Builder supportedForms(Form... supportedForms) {
            this.supportedForms.addAll(List.of(supportedForms));
            return this;
        }

        /**
         * For tag-based forms that are not present in Vanilla or create.
         */
        public Builder optionalForms(Form... optionalForms) {
            this.optionalForms.addAll(List.of(optionalForms));
            return this;
        }

        /**
         * For item-based forms that are added by a different mod than the main one.
         */
        public Builder compatForm(Form form, Mods mod) {
            this.compatForms.put(form, mod);
            return this;
        }

        public Builder customForm(ResourceLocation item, long amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable) {
            this.customForms.add(Form.custom(item, amount, processingTime, fuelCost, mechanicalMixerMeltable));
            return this;
        }

        // Shortcuts

        /**
         * For Vanilla and Create metals except Zinc: Ingot, Nugget, Plate + Create Crafts & Additions Rod compat <br>
         */
        public Builder createForms() {
            return basicAndPlateForms().compatForm(Form.ROD, CREATE_ADDITIONS);
        }

        /**
         * For metals that also have a plate form
         */
        public Builder basicAndPlateForms() {
            return basicForms().supportedForms(Form.PLATE);
        }

        /**
         * For metals that only have ingot and nugget forms
         */
        public Builder basicForms() {
            return supportedForms(Form.INGOT, Form.NUGGET);
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
         * Data generator will not automatically create tags for all supported forms (including the internal tag). Use when the metal doesn't have common tags (like Andesite Alloy)
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
                    supportedForms.toArray(Form[]::new),
                    optionalForms.toArray(Form[]::new),
                    compatForms,
                    ignoreTagDatagen,
                    mechanicalMixerMeltable,
                    fluidInteractions,
                    customForms.toArray(Form[]::new), compactingOverride);
        }

        /**
         * Shortcut for building and registering the Molten Metal
         */
        public MoltenMetal register() {
            return build().register();
        }
    }

    public static void provideLangEntries(BiConsumer<String, String> consumer) {

        for (MoltenMetal metal : ALL_METALS) {
            for (CommonTags.Namespace namespace : CommonTags.Namespace.values()) {
                ResourceLocation fluidLoc = metal.fluidTag().location();
                consumer.accept(
                        "tag.fluid." + namespace.namespace + "." + fluidLoc.getPath().replace('/', '.'),
                        titleCaseConversion(metal.fluidLocation().getPath().replace('_', ' ')));

                for (Form form : metal.nonCustomForms()) {
                    TagKey<Item> tag = itemTagOf(namespace.tagPath(form.tagFolder, metal.name), namespace);
                    ResourceLocation loc = tag.location();
                    consumer.accept("tag.item." + namespace.namespace + "." + loc.getPath().replace('/', '.'),
                            titleCaseConversion((metal.name + ' ' + form.tagFolder).replace('_', ' ')));
                }
            }
        }

    }

}
