package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import com.dudko.blazinghot.content.entity.renderer.BlazeArrowRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.MobCategory;

public class BlazingEntities {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static final EntityEntry<BlazeArrowEntity>
            BLAZE_ARROW =
            REGISTRATE.<BlazeArrowEntity>entity("blaze_arrow", BlazeArrowEntity::create, MobCategory.MISC).renderer(
                    () -> BlazeArrowRenderer::new).properties(c -> BlazeArrowEntity.build(c.));


    private static <T> NonNullConsumer<T> configure(Consumer<EntityTypeConfigurator> consumer) {
        return builder -> consumer.accept(EntityTypeConfigurator.of(builder));
    }

    public static void register() {
    }

    public abstract static class EntityTypeConfigurator {
        @ExpectPlatform
        public static EntityTypeConfigurator of(Object builder) {
            throw new AssertionError();
        }

        public abstract EntityTypeConfigurator size(float width, float height);
        public abstract EntityTypeConfigurator fireImmune();
    }
}
