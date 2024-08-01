package com.dudko.blazinghot.registry.fabric;

import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

import static net.minecraft.world.item.Items.BUCKET;

@SuppressWarnings("UnstableApiUsage")
public class BlazingFluidsImpl {

    public static FluidEntry<?> createFromLava(CreateRegistrate registrate, String name) {
        return createFromLava(registrate, name,30, 2);
    }

    public static FluidEntry<?> createFromLava(CreateRegistrate registrate, String name, int tickRate, int decreaseRate) {
        return registrate
                .standardFluid(name)
                .tag(AllTags.forgeFluidTag(name), FluidTags.LAVA) // fabric: lava tag controls physics
                .fluidProperties(p -> p
                        .levelDecreasePerBlock(decreaseRate)
                        .tickRate(tickRate)
                        .flowSpeed(3)
                        .blastResistance(100f))
                .fluidAttributes(() -> new CreateAttributeHandler("block.blazinghot." + name, 1500, 1400))
                .block()
                .properties(p -> p.lightLevel(s -> 15))
                .build()
                .onRegisterAfter(Registries.ITEM, f -> {
                    Fluid source = f.getSource();
                    // transfer values
                    FluidStorage
                            .combinedItemApiProvider(source.getBucket())
                            .register(context -> new FullItemFluidStorage(context,
                                                                          bucket -> ItemVariant.of(BUCKET),
                                                                          FluidVariant.of(source),
                                                                          FluidConstants.BUCKET));
                    FluidStorage
                            .combinedItemApiProvider(BUCKET)
                            .register(context -> new EmptyItemFluidStorage(context,
                                                                           bucket -> ItemVariant.of(source.getBucket()),
                                                                           source,
                                                                           FluidConstants.BUCKET));
                })
                .register();
    }

    public static FluidEntry<?> createFromLava(CreateRegistrate registrate, String name, int tickRate) {
        return createFromLava(registrate, name, tickRate, 2);
    }

    private record CreateAttributeHandler(Component name, int viscosity, boolean lighterThanAir) implements FluidVariantAttributeHandler {
        private CreateAttributeHandler(String key, int viscosity, int density) {
            this(Component.translatable(key), viscosity, density <= 0);
        }

        public CreateAttributeHandler(String key) {
            this(key, FluidConstants.WATER_VISCOSITY, 1000);
        }

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return name.copy();
        }

        @Override
        public int getViscosity(FluidVariant variant, @Nullable Level world) {
            return viscosity;
        }

        @Override
        public boolean isLighterThanAir(FluidVariant variant) {
            return lighterThanAir;
        }
    }
}
