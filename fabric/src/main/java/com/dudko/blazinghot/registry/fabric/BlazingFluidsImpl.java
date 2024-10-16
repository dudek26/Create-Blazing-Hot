package com.dudko.blazinghot.registry.fabric;

import static com.dudko.blazinghot.registry.CommonTags.Namespace.COMMON;
import static com.dudko.blazinghot.registry.CommonTags.Namespace.FORGE;
import static com.dudko.blazinghot.registry.CommonTags.fluidTagOf;
import static net.minecraft.world.item.Items.BUCKET;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.jetbrains.annotations.ApiStatus;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags.AllFluidTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("UnstableApiUsage")
public class BlazingFluidsImpl {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static final MoltenMetalsList<SimpleFlowableFluid.Flowing>
			MOLTEN_METALS =
			new MoltenMetalsList<>(metal -> createFromLava(metal.moltenName()));

	public static final FluidEntry<SimpleFlowableFluid.Flowing> NETHER_LAVA = createFromLava("nether_lava", 10, 1);

	private static FluidEntry<SimpleFlowableFluid.Flowing> createFromLava(String name) {
		return createFromLava(name, 30);
	}

	private static FluidEntry<SimpleFlowableFluid.Flowing> createFromLava(String name, int tickRate) {
		return createFromLava(name, tickRate, 2);
	}

	private static FluidEntry<SimpleFlowableFluid.Flowing> createFromLava(String name, int tickRate, int decreaseRate) {
		return REGISTRATE
				.standardFluid(name)
				.tag(fluidTagOf(name, COMMON),
						fluidTagOf(name,
								FORGE)) // replace this with something else if the datagen fails to generate these tags
				.tag(FluidTags.LAVA) // fabric: lava tag controls physics
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

		if (fluidState.isSource() && FluidHelper.isLava(fluidState.getType())) return null;

		for (Direction direction : Iterate.directions) {
			FluidState
					metFluidState =
					fluidState.isSource() ? fluidState : world.getFluidState(pos.relative(direction));
			if (!metFluidState.is(FluidTags.WATER)) continue;
			BlockState lavaInteraction = getLavaInteraction(fluidState, metFluidState);
			if (lavaInteraction == null) continue;
			return lavaInteraction;
		}
		return null;
	}

	/**
	 * Fabric: use tags for comparing fluids
	 */
	@ApiStatus.Experimental
	private static final Map<Fluid, TagKey<Fluid>>
			fluidTags =
			Map.of(Fluids.WATER, FluidTags.WATER, AllFluids.HONEY.get(), AllFluidTags.HONEY.tag);

	@Nullable
	public static BlockState getLavaInteraction(FluidState fluidState, FluidState metFluidState) {
		Fluid fluid = fluidState.getType();
		Fluid metFluid = metFluidState.getType();

		for (MoltenMetal metal : MoltenMetals.ALL) {
			for (Map.Entry<Fluid, NonNullSupplier<Block>> entry : metal.getFluidInteractions().entrySet()) {

				TagKey<Fluid> fluidTag = fluidTags.get(entry.getKey());

				if (entry.getValue() == null) {
					BlazingHot.LOGGER.debug("Null fluid interaction for {}, {}",
							metal.moltenName(),
							BuiltInRegistries.FLUID.getKey(entry.getKey()));
					continue;
				}
				if (fluid.isSame(MOLTEN_METALS.getFluid(metal)) && metFluidState.is(fluidTag)) {
					return entry.getValue().get().defaultBlockState();
				}
			}
		}

		return null;
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

	public static class MoltenMetalsList<T extends SimpleFlowableFluid> implements Iterable<FluidEntry<T>> {

		private static final int METAL_AMOUNT = MoltenMetals.ALL.size();

		private final FluidEntry<?>[] values = new FluidEntry<?>[METAL_AMOUNT];

		private static int metalOrdinal(MoltenMetal metal) {
			return MoltenMetals.ALL.indexOf(metal);
		}

		public MoltenMetalsList(Function<MoltenMetal, FluidEntry<? extends T>> filler) {
			for (MoltenMetal metal : MoltenMetals.ALL) {
				values[metalOrdinal(metal)] = filler.apply(metal);
			}
		}

		@SuppressWarnings("unchecked")
		public FluidEntry<T> get(MoltenMetal metal) {
			return (FluidEntry<T>) values[metalOrdinal(metal)];
		}

		public T getFluid(MoltenMetal metal) {
			return get(metal).getSource();
		}

		public boolean contains(Fluid fluid) {
			for (FluidEntry<?> entry : values) {
				if (entry.is(fluid)) {
					return true;
				}
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		public FluidEntry<T>[] toArray() {
			return (FluidEntry<T>[]) Arrays.copyOf(values, values.length);
		}

		@Override
		public Iterator<FluidEntry<T>> iterator() {
			return new Iterator<>() {
				private int index = 0;

				@Override
				public boolean hasNext() {
					return index < values.length;
				}

				@SuppressWarnings("unchecked")
				@Override
				public FluidEntry<T> next() {
					if (!hasNext()) throw new NoSuchElementException();
					return (FluidEntry<T>) values[index++];
				}
			};
		}

	}
}
