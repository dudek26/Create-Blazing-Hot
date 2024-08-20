package com.dudko.blazinghot.content.metal;

import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

import static com.dudko.blazinghot.compat.Mods.*;

public class MoltenMetals {
    public static final List<MoltenMetal> ALL = new ArrayList<>();

    public static MoltenMetal
            IRON = MoltenMetal.builder("iron").createForms().register(),

    GOLD = MoltenMetal.builder("gold").createForms().register(),

    COPPER = MoltenMetal.builder("copper").createForms().register(),

    ANCIENT_DEBRIS = MoltenMetal.builder("ancient_debris")
            .customForm(VANILLA.asResource("netherite_scrap"),
                    MultiFluids.Constants.INGOT.droplets,
                    Forms.INGOT.processingTime * 2,
                    Forms.INGOT.fuelCost * 2,
                    false)
            .customForm(VANILLA.asResource("ancient_debris"),
                    MultiFluids.Constants.NUGGET.droplets * 12,
                    Forms.INGOT.processingTime * 2,
                    Forms.INGOT.fuelCost * 2,
                    false)
            .compactingOverride(VANILLA.asResource("netherite_scrap"), MultiFluids.Constants.INGOT.droplets)
            .addFluidInteraction(Fluids.WATER,
                    () -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
            .disableMechanicalMixing()
            .ignoreTagDatagen()
            .register(),

    NETHERITE = MoltenMetal.builder("netherite")
            .supportedForms(Forms.INGOT)
            .disableMechanicalMixing()
            .addFluidInteraction(Fluids.WATER,
                    () -> AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get())
            .register(),

    BLAZE_GOLD = MoltenMetal.builder("blaze_gold")
            .mod(BLAZINGHOT)
            .basicAndPlateForms()
            .supportedForms(Forms.ROD)
            .addFluidInteraction(Fluids.WATER, () -> Blocks.NETHERRACK)
            .register(),

    ZINC = MoltenMetal.builder("zinc").mod(CREATE).basicForms().optionalForms(Forms.PLATE).register(),

    BRASS = MoltenMetal.builder("brass").mod(CREATE).createForms().register(),

    ANDESITE = MoltenMetal.builder("andesite")
            .mod(CREATE)
            .customForm(AllItems.ANDESITE_ALLOY.getId(),
                    Forms.INGOT.amount,
                    Forms.INGOT.processingTime,
                    Forms.INGOT.fuelCost,
                    true)
            .compactingOverride(AllItems.ANDESITE_ALLOY.getId(), MultiFluids.Constants.INGOT.droplets)
            .ignoreTagDatagen()
            .register();

    public static void init() {

    }
}
