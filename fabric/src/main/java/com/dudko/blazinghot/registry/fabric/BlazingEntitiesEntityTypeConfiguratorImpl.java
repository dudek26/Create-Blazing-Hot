package com.dudko.blazinghot.registry.fabric;

import com.dudko.blazinghot.registry.BlazingEntities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.EntityDimensions;

public class BlazingEntitiesEntityTypeConfiguratorImpl extends BlazingEntities.EntityTypeConfigurator {

    private final FabricEntityTypeBuilder<?> builder;

    protected BlazingEntitiesEntityTypeConfiguratorImpl(FabricEntityTypeBuilder<?> builder) {
        this.builder = builder;
    }

    public static BlazingEntities.EntityTypeConfigurator of(Object builder) {
        if (builder instanceof FabricEntityTypeBuilder<?> fabricBuilder)
            return new BlazingEntitiesEntityTypeConfiguratorImpl(fabricBuilder);
        throw new IllegalArgumentException("builder must be a FabricEntityTypeBuilder");
    }

    @Override
    public BlazingEntities.EntityTypeConfigurator size(float width, float height) {
        builder.dimensions(EntityDimensions.fixed(width, height));
        return this;
    }
}
