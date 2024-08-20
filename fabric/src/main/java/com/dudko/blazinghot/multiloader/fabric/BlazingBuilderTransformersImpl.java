package com.dudko.blazinghot.multiloader.fabric;

import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;

public class BlazingBuilderTransformersImpl {
    public static <B extends ModernLampBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampBlock(DyeColor color) {
        return a -> a
                .blockstate((c, p) -> p
                        .getVariantBuilder(c.get())
                        .forAllStates(state -> ConfiguredModel
                                .builder()
                                .modelFile(p
                                        .models()
                                        .cubeAll(color.getName()
                                                        + "_modern_lamp"
                                                        + (state.getValue(ModernLampBlock.LIT) ? "_powered" : ""),
                                                p.modLoc("block/modern_lamp/" + color.getName() + (state.getValue(
                                                        ModernLampBlock.LIT) ? "_powered" : ""))))
                                .build()

                        ))
                .item()
                .tag(BlazingTags.Items.MODERN_LAMPS.tag)
                .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
                .model((c, b) -> b.blockItem(c).texture("#all", b.modLoc("block/modern_lamp/" + color.getName())))
                .build();
    }

    public static <B extends ModernLampPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampPanel(DyeColor color) {
        return b -> b
                .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> {
                            Direction facing = state.getValue(ModernLampPanelBlock.FACING);
                            int xRotation = facing == Direction.DOWN ? 180 : 0;
                            int yRotation = facing.getAxis().isVertical() ? 0 : (int) facing.toYRot();

                            String variant = color.getName();
                            if (state.getValue(ModernLampPanelBlock.FACING).getAxis().isHorizontal()) variant += "_vertical";
                            if (state.getValue(ModernLampPanelBlock.LIT)) variant += "_powered";

                            return ConfiguredModel
                                    .builder()
                                    .modelFile(p.models().getExistingFile(p.modLoc("block/modern_lamp_panel/" + variant)))
                                    .rotationX((xRotation + 360) % 360)
                                    .rotationY((yRotation + 360) % 360)
                                    .build();
                        }

                ))
                .item()
                .tag(BlazingTags.Items.MODERN_LAMP_PANELS.tag)
                .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
                .transform(ModelGen.customItemModel("modern_lamp_panel", color.getName()));
    }

}
