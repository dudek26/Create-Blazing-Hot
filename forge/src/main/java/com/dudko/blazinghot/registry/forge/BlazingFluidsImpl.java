package com.dudko.blazinghot.registry.forge;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

public class BlazingFluidsImpl {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	public static MoltenMetalsList<ForgeFlowingFluid.Flowing>
			MOLTEN_METALS =
			new MoltenMetalsList<>(metal -> createFromLava(metal.moltenName()));

	public static FluidEntry<ForgeFlowingFluid.Flowing> NETHER_LAVA = createFromLava("nether_lava", 10, 1);


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

		fluidInteraction(NETHER_LAVA, () -> Blocks.COBBLESTONE, Fluids.WATER, Fluids.WATER.getSource());
		fluidInteraction(NETHER_LAVA,
				() -> AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get(),
				AllFluids.HONEY.get(),
				AllFluids.HONEY.get().getSource());
		fluidInteraction(NETHER_LAVA,
				() -> AllPaletteStoneTypes.SCORIA.getBaseBlock().get(),
				AllFluids.CHOCOLATE.get(),
				AllFluids.CHOCOLATE.get().getSource());

		for (MoltenMetal metal : MoltenMetals.ALL) {
			for (Map.Entry<Fluid, NonNullSupplier<Block>> entry : metal.getFluidInteractions().entrySet()) {
				if (entry.getValue() == null) {
					BlazingHot.LOGGER.error("Null fluid interaction for {}, {}",
							metal.moltenName(),
							ForgeRegistries.FLUIDS.getKey(entry.getKey()));
					continue;
				}
				fluidInteraction(MOLTEN_METALS.get(metal), entry.getValue(), entry.getKey());
			}
		}

		FluidInteractionRegistry.addInteraction(NETHER_LAVA.getType(),
				new InteractionInformation((level, currentPos, relativePos, fluidState) -> level
						.getBlockState(relativePos)
						.getBlock() == Blocks.BLUE_ICE
						&& level.getBlockState(currentPos.below()).getBlock() == Blocks.SOUL_SOIL,
						Blocks.BASALT.defaultBlockState()));

	}

	private static void fluidInteraction(FluidEntry<ForgeFlowingFluid.Flowing> entry, NonNullSupplier<Block> result, Fluid... fluids) {
		Block block = result.get();
		for (Fluid fluid : fluids) {
			FluidInteractionRegistry.addInteraction(entry.getType(),
					new InteractionInformation(fluid.getFluidType(), fluidState -> {
						if (fluidState.isSource()) {
							return Blocks.OBSIDIAN.defaultBlockState();
						}
						else {
							return block.defaultBlockState();
						}
					}));
		}
	}

	@Nullable
	public static BlockState getFluidInteraction(FluidState fluidState, FluidState metFluidState) {
		for (MoltenMetal metal : MoltenMetals.ALL) {
			for (Map.Entry<Fluid, NonNullSupplier<Block>> entry : metal.getFluidInteractions().entrySet()) {
				if (entry.getValue() == null) {
					BlazingHot.LOGGER.debug("Null fluid interaction for {}, {}",
							metal.moltenName(),
							ForgeRegistries.FLUIDS.getKey(entry.getKey()));
					continue;
				}
				if (fluidState.getType().isSame(MOLTEN_METALS.getFluid(metal)) && metFluidState
						.getType()
						.isSame(entry.getKey())) {
					return entry.getValue().get().defaultBlockState();
				}
			}
		}

		return null;
	}

	public static void platformRegister() {
	}

	public static class MoltenMetalsList<T extends ForgeFlowingFluid> implements Iterable<FluidEntry<T>> {

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
		public @NotNull Iterator<FluidEntry<T>> iterator() {
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
