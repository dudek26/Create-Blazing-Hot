package com.dudko.blazinghot.data.advancement.fabric;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.advancement.CriterionTriggerBase;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

/**
 * from {@link com.simibubi.create.foundation.advancement.SimpleCreateTrigger}
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleBlazingTrigger extends BlazingCriterionTriggerBase<SimpleBlazingTrigger.Instance>{
    public SimpleBlazingTrigger(String id) {
        super(id);
    }

    @Override
    public SimpleBlazingTrigger.Instance createInstance(JsonObject json, DeserializationContext context) {
        return new SimpleBlazingTrigger.Instance(getId());
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    public SimpleBlazingTrigger.Instance instance() {
        return new SimpleBlazingTrigger.Instance(getId());
    }

    public static class Instance extends BlazingCriterionTriggerBase.Instance {

        public Instance(ResourceLocation idIn) {
            super(idIn, ContextAwarePredicate.ANY);
        }

        @Override
        protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
            return true;
        }
    }
}
