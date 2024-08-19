package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MechanicalMixerBlockEntity.class)
public abstract class MechanicalMixerBlockEntityMixin extends BasinOperatingBlockEntity implements IAdvancementBehaviour {


    public MechanicalMixerBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(method = "addBehaviours", at = @At("TAIL"), remap = false)
    private void blazinghot$addAdvancements(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        blazinghot$registerAwardables(behaviours,
                BlazingAdvancements.MOLTEN_GOLD,
                BlazingAdvancements.MOLTEN_BLAZE_GOLD);
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

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/mixer/MechanicalMixerBlockEntity;applyBasinRecipe()V"), remap = false)
    private void blazinghot$meltingAdvancements(CallbackInfo ci) {
        if (currentRecipe instanceof MixingRecipe recipe) {
            if (MultiFluids.recipeResultContains(recipe, MoltenMetal.GOLD.fluidTag())) {
                blazinghot$award(BlazingAdvancements.MOLTEN_GOLD);
            }
            if (MultiFluids.recipeResultContains(recipe, MoltenMetal.BLAZE_GOLD.fluidTag())) {
                blazinghot$award(BlazingAdvancements.MOLTEN_BLAZE_GOLD);
            }
        }
    }

}
