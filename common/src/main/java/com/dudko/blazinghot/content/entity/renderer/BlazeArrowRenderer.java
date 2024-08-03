package com.dudko.blazinghot.content.entity.renderer;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.entity.BlazeArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlazeArrowRenderer extends ArrowRenderer<BlazeArrowEntity> {

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BlazeArrowEntity entity) {
        return new ResourceLocation(BlazingHot.ID, "textures/entity/projectiles/blaze_arrow.png");
    }

    public BlazeArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
