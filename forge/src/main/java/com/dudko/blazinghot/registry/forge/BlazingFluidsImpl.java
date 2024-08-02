package com.dudko.blazinghot.registry.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingFluids.MultiloaderFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

public class BlazingFluidsImpl {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_GOLD, MOLTEN_IRON, MOLTEN_BLAZE_GOLD, NETHER_LAVA;

    public static void platformRegister() {
        MOLTEN_GOLD = createFromLava("molten_gold");
        MOLTEN_IRON = createFromLava("molten_iron");
        MOLTEN_BLAZE_GOLD = createFromLava("molten_blaze_gold");
        NETHER_LAVA = createFromLava("nether_lava", 10, 1);
    }

    public static FluidEntry<ForgeFlowingFluid.Flowing> createFromLava(String name, int tickRate) {
        return createFromLava(name, tickRate, 2);
    }

    public static FluidEntry<ForgeFlowingFluid.Flowing> createFromLava(String name) {
        return createFromLava(name, 30);
    }

    public static FluidEntry<ForgeFlowingFluid.Flowing> createFromLava(String name, int tickRate, int decreaseRate) {
        return REGISTRATE
                .standardFluid(name)
                .tag(AllTags.forgeFluidTag(name))
                .properties(p -> p
                        .density(3000)
                        .viscosity(6000)
                        .temperature(1300)
                        .canExtinguish(false)
                        .canHydrate(false)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                        // from forge lava type
                        .motionScale(0.0023333333333333335D)
                        .canSwim(false)
                        .canDrown(false)
                        .pathType(BlockPathTypes.LAVA)
                        .adjacentPathType(null))
                .fluidProperties(p -> p
                        .tickRate(tickRate)
                        .levelDecreasePerBlock(decreaseRate)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .source(ForgeFlowingFluid.Source::new)
                .block()
                .initialProperties(() -> Blocks.LAVA)
                .properties(p -> p.lightLevel(b -> 15))
                .build()
                .bucket()
                .tag(AllTags.forgeItemTag("buckets/" + name))
                .build()
                .register();
    }

    public static void registerFluidInteractions() {

        waterInteraction(NETHER_LAVA, Blocks.COBBLESTONE.defaultBlockState());
        waterInteraction(MOLTEN_GOLD, Blocks.COBBLESTONE.defaultBlockState());
        waterInteraction(MOLTEN_IRON, Blocks.COBBLESTONE.defaultBlockState());
        waterInteraction(MOLTEN_BLAZE_GOLD, Blocks.NETHERRACK.defaultBlockState());

    }

    private static void waterInteraction(FluidEntry<ForgeFlowingFluid.Flowing> entry, BlockState result) {
        FluidInteractionRegistry.addInteraction(entry.get().getFluidType(),
                new FluidInteractionRegistry.InteractionInformation(ForgeMod.WATER_TYPE.get(), fluidState -> {
                    if (fluidState.isSource()) {
                        return Blocks.OBSIDIAN.defaultBlockState();
                    }
                    else {
                        return result;
                    }
                }));
    }

    public static FluidEntry<ForgeFlowingFluid.Flowing> getEntry(MultiloaderFluids fluid) {

        return switch (fluid) {
            case MOLTEN_GOLD -> MOLTEN_GOLD;
            case MOLTEN_IRON -> MOLTEN_IRON;
            case MOLTEN_BLAZE_GOLD -> MOLTEN_BLAZE_GOLD;
            case NETHER_LAVA -> NETHER_LAVA;
            default -> throw new NullPointerException("Fluid " + fluid + " not found");
        };

    }

}
