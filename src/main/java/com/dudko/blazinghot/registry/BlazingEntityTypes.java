package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import com.dudko.blazinghot.content.entity.renderer.BlazeArrowRenderer;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class BlazingEntityTypes {

    private static final CreateRegistrate REGISTRATE = BlazingHot.REGISTRATE;

    public static final EntityEntry<BlazeArrowEntity>
            BLAZE_ARROW =
            register("blaze_arrow", BlazeArrowEntity::new, () -> BlazeArrowRenderer::new, MobCategory.MISC, 4, 20, true,
                     false, BlazeArrowEntity::build).tag(EntityTypeTags.ARROWS).register();

    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory, NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer, MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire, NonNullConsumer<FabricEntityTypeBuilder<T>> propertyBuilder) {
        String id = Lang.asId(name);
        return (CreateEntityBuilder<T, ?>) REGISTRATE
                .entity(id, factory, group)
                .properties(b -> b
                        .trackRangeChunks(range)
                        .trackedUpdateRate(updateFrequency)
                        .forceTrackedVelocityUpdates(sendVelocity))
                .properties(propertyBuilder)
                .properties(b -> {
                    if (immuneToFire) b.fireImmune();
                })
                .renderer(renderer);
    }

    public static void register() {
    }
}
