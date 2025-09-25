package com.dudko.blazinghot.config.neoforge;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.config.CStress;

import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CStressImpl extends CStress {

	protected final Map<ResourceLocation, ConfigValue<Double>> capacities = new HashMap<>();
	protected final Map<ResourceLocation, ConfigValue<Double>> impacts = new HashMap<>();

	public static CStress create() {
		return new CStressImpl();
	}

	@Override
	public void registerAll(Builder builder) {
		builder.comment(".", Comments.su, Comments.impact).push("impact");
		DEFAULT_IMPACTS.forEach((id, value) -> this.impacts.put(id, builder.define(id.getPath(), value)));
		builder.pop();

		builder.comment(".", Comments.su, Comments.capacity).push("capacity");
		DEFAULT_CAPACITIES.forEach((id, value) -> this.capacities.put(id, builder.define(id.getPath(), value)));
		builder.pop();
	}

	@Override
	public @Nullable DoubleSupplier getImpact(Block block) {
		ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
		ConfigValue<Double> value = this.impacts.get(id);
		return value == null ? null : value::get;
	}

	@Override
	public @Nullable DoubleSupplier getCapacity(Block block) {
		ResourceLocation id = RegisteredObjectsHelper.getKeyOrThrow(block);
		ConfigValue<Double> value = this.capacities.get(id);
		return value == null ? null : value::get;
	}

}
