package com.dudko.blazinghot.compat.emi;

import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.emi.CreateEmiAnimations;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Mth;

public class BlazingEmiAnimations extends CreateEmiAnimations {

    public static void addBlazeMixer(WidgetHolder widgets, int x, int y) {
        widgets.addDrawable(x, y, 0, 0, (graphics, mouseX, mouseY, delta) -> {
            PoseStack matrices = graphics.pose();
            matrices.translate(0, 0, 200);
            matrices.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-15.5f));
            matrices.mulPose(com.mojang.math.Axis.YP.rotationDegrees(22.5f));
            int scale = 23;

            blockElement(crimsonCogwheel())
                    .rotateBlock(0, getCurrentAngle() * 2, 0)
                    .atLocal(0, 0, 0)
                    .scale(scale)
                    .render(graphics);

            blockElement(BlazingBlocks.BLAZE_MIXER.getDefaultState()).atLocal(0, 0, 0).scale(scale).render(graphics);

            float animation = ((Mth.sin(AnimationTickHolder.getRenderTime() / 32f) + 1) / 5) + .5f;

            blockElement(BlazingPartialModels.BLAZE_MIXER_POLE).atLocal(0, animation, 0).scale(scale).render(graphics);

            blockElement(BlazingPartialModels.BLAZE_MIXER_HEAD).rotateBlock(0, getCurrentAngle() * 4, 0).atLocal(0,
                    animation,
                    0).scale(scale).render(graphics);

            blockElement(AllBlocks.BASIN.getDefaultState()).atLocal(0, 1.65, 0).scale(scale).render(graphics);
        });
    }

    public static PartialModel crimsonCogwheel() {
        return BlazingPartialModels.SHAFTLESS_CRIMSON_COGWHEEL;
    }

}
