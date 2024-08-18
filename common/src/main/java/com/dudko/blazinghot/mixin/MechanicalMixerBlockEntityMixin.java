package com.dudko.blazinghot.mixin;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MechanicalMixerBlockEntity.class)
public abstract class MechanicalMixerBlockEntityMixin extends BasinOperatingBlockEntity {

    public MechanicalMixerBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @ModifyArg(method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(III)I"),
            index = 2)
    protected int blazinghot$extendDuration(int max) {
        ResourceLocation blazinghot$id = currentRecipe.getId();
        if (blazinghot$id.getPath().startsWith("mixing/melting")) {
            return max * 16;
        }
        else return max;
    }

}
