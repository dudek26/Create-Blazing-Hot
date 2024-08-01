package com.dudko.blazinghot.registry.forge;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class BlazingFluidsImpl {

    public static FluidEntry<ForgeFlowingFluid.Flowing> createFromLava(CreateRegistrate registrate, String name, int tickRate, int decreaseRate) {
        return registrate
                .standardFluid(name)
                .tag(AllTags.forgeFluidTag(name), FluidTags.LAVA)
                .properties(p -> p
                        .viscosity(10000)
                        .density(2000)
                        .temperature(1000)
                        .canExtinguish(false)
                        .canHydrate(false)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                        // from forge lava type
                        .motionScale(0.0023333333333333335D)
                        .canSwim(false).canDrown(false)
                        .pathType(BlockPathTypes.LAVA).adjacentPathType(null))
                .fluidProperties(p -> p
                        .tickRate(tickRate)
                        .levelDecreasePerBlock(decreaseRate)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .source(ForgeFlowingFluid.Source::new)
                .block()
                .properties(p -> p.lightLevel(b -> 15))
                .build()
                .bucket()
                .tag(AllTags.forgeItemTag("buckets/" + name))
                .build()
                .register();
    }

    public static FluidEntry<ForgeFlowingFluid.Flowing> createFromLava(CreateRegistrate registrate, String name, int tickRate) {
        return createFromLava(registrate, name, tickRate, 2);
    }

    public static FluidEntry<ForgeFlowingFluid.Flowing> createFromLava(CreateRegistrate registrate, String name) {
        return createFromLava(registrate, name, 30);
    }


}
