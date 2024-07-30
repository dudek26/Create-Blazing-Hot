package com.dudko.blazinghot.compat.jei;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedBlazeMixer extends AnimatedKinetics {

    protected PartialModel crimsonCogwheel() {
        return BlazingPartialModels.SHAFTLESS_CRIMSON_COGWHEEL;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(crimsonCogwheel()).rotateBlock(0, getCurrentAngle() * 2, 0).atLocal(0, 0, 0).scale(scale).render(
                graphics);

        blockElement(BlazingBlocks.BLAZE_MIXER.getDefaultState()).atLocal(0, 0, 0).scale(scale).render(graphics);

        float animation = ((Mth.sin(AnimationTickHolder.getRenderTime() / 32f) + 1) / 5) + .5f;

        blockElement(BlazingPartialModels.BLAZE_MIXER_POLE).atLocal(0, animation, 0).scale(scale).render(graphics);

        blockElement(BlazingPartialModels.BLAZE_MIXER_HEAD).rotateBlock(0, getCurrentAngle() * 4, 0).atLocal(0,
                animation,
                0).scale(scale).render(graphics);

        blockElement(AllBlocks.BASIN.getDefaultState()).atLocal(0, 1.65, 0).scale(scale).render(graphics);

        matrixStack.popPose();
    }

}
