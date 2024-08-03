package com.dudko.blazinghot.registry.forge;

import com.dudko.blazinghot.registry.BlazingEntities;
import net.minecraft.world.entity.EntityType;

public class EntityTypeConfiguratorImpl extends BlazingEntities.EntityTypeConfigurator {

    private final EntityType.Builder<?> builder;

    public EntityTypeConfiguratorImpl(EntityType.Builder<?> builder) {
        this.builder = builder;
    }

    public static BlazingEntities.EntityTypeConfigurator of(Object builder) {
        if (builder instanceof EntityType.Builder<?> fabricBuilder) {
            return new EntityTypeConfiguratorImpl(fabricBuilder);
        }
        throw new IllegalArgumentException("builder must be an EntityType.Builder");
    }

    @Override
    public BlazingEntities.EntityTypeConfigurator size(float width, float height) {
        builder.sized(width, height);
        return this;
    }

    @Override
    public BlazingEntities.EntityTypeConfigurator fireImmune() {
        builder.fireImmune();
        return this;
    }

}
