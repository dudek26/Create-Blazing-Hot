package com.dudko.blazinghot.content.block.modern_lamp;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModernLampGenerator extends SpecialBlockStateGen {

    private final DyeColor color;

    public ModernLampGenerator(DyeColor color) {
        this.color = color;
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String variant = color.getName();
        String name = variant + "_modern_lamp";
        if (state.getValue(ModernLampBlock.LIT)) {
            variant += "_powered";
            name += "_powered";
        }
        return prov.models().cubeAll(name, prov.modLoc("block/modern_lamp/" + variant));
    }
}
