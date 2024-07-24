package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.content.block.IRemoteLamp;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnreachableCode")
@Mixin(RedstoneLampBlock.class)
public abstract class RedstoneLampMixin implements IRemoteLamp, BlockAccessor {

    @Shadow
    @Final
    public static BooleanProperty LIT;

    @Override
    public boolean blazinghot$isRemotePowered(BlockState state) {
        return state.getValue(REMOTE_POWERED);
    }

    @Override
    public void blazinghot$setRemotePowered(BlockState state, boolean bool) {
        state.setValue(REMOTE_POWERED, bool);
    }

    @Inject(method = "neighborChanged", at = @At("HEAD"))
    private void blazinghot$remoteControlledBehavior(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston, CallbackInfo ci) {
        if (level.isClientSide) return;
        if (blazinghot$isRemotePowered(state)) {
            level.setBlock(pos, state.setValue(LIT, true), 2);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void blazinghot$onInit(BlockBehaviour.Properties properties, CallbackInfo ci) {
        registerDefaultState(defaultBlockState().setValue(REMOTE_POWERED, false));
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void blazinghot$onCreateBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(REMOTE_POWERED);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void blazinghot$remoteTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.getValue(REMOTE_POWERED)) ci.cancel();
    }
}