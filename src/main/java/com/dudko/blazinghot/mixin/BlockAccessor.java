package com.dudko.blazinghot.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor {
    @Accessor("defaultBlockState")
    void registerDefaultState(BlockState state);

    @Accessor("defaultBlockState")
    BlockState defaultBlockState();
}
