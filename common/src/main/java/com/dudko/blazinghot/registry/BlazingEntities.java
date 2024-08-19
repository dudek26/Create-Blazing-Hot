package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import com.dudko.blazinghot.content.entity.renderer.BlazeArrowRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Consumer;

public class BlazingEntities {

    private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

    public static final EntityEntry<BlazeArrowEntity>
            BLAZE_ARROW =
            REGISTRATE
                    .<BlazeArrowEntity>entity("blaze_arrow", BlazeArrowEntity::create, MobCategory.MISC)
                    .renderer(() -> BlazeArrowRenderer::new)
                    .properties(configure(c -> c.size(0.25f, 0.25f)))
                    .tag(EntityTypeTags.ARROWS)
                    .register();


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
    }
}
