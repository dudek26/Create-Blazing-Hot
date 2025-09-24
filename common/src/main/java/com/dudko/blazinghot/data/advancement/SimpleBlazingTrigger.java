package com.dudko.blazinghot.data.advancement;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;

/**
 * from {@link com.simibubi.create.foundation.advancement.SimpleCreateTrigger}
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleBlazingTrigger extends BlazingCriterionTriggerBase<SimpleBlazingTrigger.Instance> {
	public SimpleBlazingTrigger(String id) {
		super(id);
	}

	public void trigger(ServerPlayer player) {
		super.trigger(player, null);
	}

	public Instance instance() {
		return new Instance();
	}

	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public static class Instance extends BlazingCriterionTriggerBase.Instance {
		private static final Codec<Instance>
				CODEC =
				RecordCodecBuilder.create(instance -> instance
						.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player))
						.apply(instance, Instance::new));

		private final Optional<ContextAwarePredicate> player;

		public Instance() {
			player = Optional.empty();
		}

		public Instance(Optional<ContextAwarePredicate> player) {
			this.player = player;
		}

		@Override
		protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
			return true;
		}

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		}
	}
}
