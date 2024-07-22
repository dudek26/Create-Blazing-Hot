package com.dudko.blazinghot.content.block.modern_lamp;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModernLampPanelGenerator extends SpecialBlockStateGen {

    private final DyeColor color;

    public ModernLampPanelGenerator(DyeColor color) {
        this.color = color;
    }

    @Override
    protected int getXRotation(BlockState state) {
        Direction facing = state.getValue(ModernLampPanelBlock.FACING);
        return facing == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction facing = state.getValue(ModernLampPanelBlock.FACING);
        return facing.getAxis().isVertical() ? 180 : horizontalAngle(facing);
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String variant = color.getName();
        if (state.getValue(ModernLampPanelBlock.FACING).getAxis().isHorizontal()) variant += "_vertical";
        if (state.getValue(ModernLampPanelBlock.LIT)) variant += "_powered";

        return prov.models().getExistingFile(prov.modLoc("block/modern_lamp_panel/" + variant));
    }
}
