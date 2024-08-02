package com.dudko.blazinghot.registry.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.fabricators_of_create.porting_lib.event.common.FluidPlaceBlockCallback;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

import static net.minecraft.world.item.Items.BUCKET;

@SuppressWarnings("UnstableApiUsage")
public class BlazingFluidsImpl {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    private static final FluidEntry<SimpleFlowableFluid.Flowing>

            NETHER_LAVA = createFromLava("nether_lava", 10, 1),

    MOLTEN_IRON = createFromLava("molten_iron"),

    MOLTEN_GOLD = createFromLava("molten_gold"),

    MOLTEN_BLAZE_GOLD = createFromLava("molten_blaze_gold");

    private static FluidEntry<SimpleFlowableFluid.Flowing> createFromLava(String name) {
        return createFromLava(name, 30);
    }

    private static FluidEntry<SimpleFlowableFluid.Flowing> createFromLava(String name, int tickRate) {
        return createFromLava(name, tickRate, 2);
    }

    private static FluidEntry<SimpleFlowableFluid.Flowing> createFromLava(String name, int tickRate, int decreaseRate) {
        return REGISTRATE
                .standardFluid(name)
                .tag(AllTags.forgeFluidTag(name), FluidTags.LAVA) // fabric: lava tag controls physics
                .fluidProperties(p -> p
                        .levelDecreasePerBlock(decreaseRate)
                        .tickRate(tickRate)
                        .flowSpeed(3)
                        .blastResistance(100f))
                .fluidAttributes(() -> new CreateAttributeHandler("block.blazinghot." + name, 6000, 3000))
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

    public static void registerFluidInteractions() {
        FluidPlaceBlockCallback.EVENT.register(BlazingFluidsImpl::whenFluidsMeet);
    }

    public static BlockState whenFluidsMeet(LevelAccessor world, BlockPos pos, BlockState blockState) {
        FluidState fluidState = blockState.getFluidState();

        if (fluidState.isSource() && FluidHelper.isWater(fluidState.getType())) return null;

        for (Direction direction : Iterate.directions) {
            FluidState
                    metFluidState =
                    fluidState.isSource() ? fluidState : world.getFluidState(pos.relative(direction));
            if (!metFluidState.is(FluidTags.LAVA)) continue;
            BlockState waterInteraction = getWaterInteraction(metFluidState);
            if (waterInteraction == null) continue;
            return waterInteraction;
        }
        return null;
    }

    @Nullable
    public static BlockState getWaterInteraction(FluidState fluidState) {
        Fluid fluid = fluidState.getType();
        if (fluid.isSame(MOLTEN_BLAZE_GOLD.get())) {
            return Blocks.NETHERRACK.defaultBlockState();
        }
        return null;
    }

    public static FluidEntry<?> getEntry(BlazingFluids.MultiloaderFluids fluid) {
        return switch (fluid) {
            case NETHER_LAVA -> NETHER_LAVA;
            case MOLTEN_IRON -> MOLTEN_IRON;
            case MOLTEN_GOLD -> MOLTEN_GOLD;
            case MOLTEN_BLAZE_GOLD -> MOLTEN_BLAZE_GOLD;
        };
    }

    public static void platformRegister() {

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
