package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.registry.BlazingTags;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = SpoutBlockEntity.class, remap = false)
public abstract class SpoutBlockEntityMixin implements IAdvancementBehaviour {

    @Unique
    private int blazinghot$goldenApplesCreated;

    @Inject(method = "addBehaviours", at = @At("TAIL"))
    private void blazinghot$addBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        blazinghot$registerAwardables(behaviours, BlazingAdvancements.METAL_APPLE_SPOUT,
                BlazingAdvancements.GOLDEN_APPLE_FACTORY);
    }

    @Inject(method = "whenItemHeld", at = @At("TAIL"))
    private void blazinghot$giveAdvancements(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, CallbackInfoReturnable<BeltProcessingBehaviour.ProcessingResult> cir,
                                             @Local ItemStack out) {
        if (out.is(BlazingTags.Items.METAL_APPLES.tag)) blazinghot$award(BlazingAdvancements.METAL_APPLE_SPOUT);
        if (out.is(Items.GOLDEN_APPLE)) {
            blazinghot$goldenApplesCreated += out.getCount();
            if (blazinghot$goldenApplesCreated >= 64) {
                blazinghot$award(BlazingAdvancements.GOLDEN_APPLE_FACTORY);
                blazinghot$goldenApplesCreated = 0;
            }
        }
    }

}
