package com.dudko.blazinghot.content.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IRemoteLamp {

    BooleanProperty REMOTE_POWERED = BooleanProperty.create("remote_powered");

    boolean blazinghot$isRemotePowered(BlockState state);

    void blazinghot$setRemotePowered(BlockState state, boolean bool);
}
