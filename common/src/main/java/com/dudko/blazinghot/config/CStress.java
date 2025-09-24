package com.dudko.blazinghot.config;

import com.dudko.blazinghot.BlazingHot;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * From {@link com.simibubi.create.infrastructure.config.CStress}
 */
@MethodsReturnNonnullByDefault
public abstract class CStress extends ConfigBase {
	// bump this version to reset configured values.
	private static final int VERSION = 1;

	// IDs need to be used since configs load before registration

	@ExpectPlatform
	public static CStress create() {
		throw new AssertionError();
	}

	protected static final Object2DoubleMap<ResourceLocation> DEFAULT_IMPACTS = new Object2DoubleOpenHashMap<>();
	protected static final Object2DoubleMap<ResourceLocation> DEFAULT_CAPACITIES = new Object2DoubleOpenHashMap<>();

	public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
		return setImpact(0);
	}

	public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
		return builder -> {
			assertFromBlazinghot(builder);
			ResourceLocation id = BlazingHot.asResource(builder.getName());
			DEFAULT_IMPACTS.put(id, value);
			return builder;
		};
	}

	public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
		return builder -> {
			assertFromBlazinghot(builder);
			ResourceLocation id = BlazingHot.asResource(builder.getName());
			DEFAULT_CAPACITIES.put(id, value);
			return builder;
		};
	}

	private static void assertFromBlazinghot(BlockBuilder<?, ?> builder) {
		if (!builder.getOwner().getModid().equals(BlazingHot.ID)) {
			throw new IllegalStateException("Non-Blazing Hot blocks cannot be added to Blazing Hot's config.");
		}
	}

	@Override
	public String getName() {
		return "stressValues.v" + VERSION;
	}

	protected static class Comments {
		public static String su = "[in Stress Units]";
		public static String
				impact =
				"Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives.";
		public static String capacity = "Configure how much stress a source can accommodate for.";
	}

}
