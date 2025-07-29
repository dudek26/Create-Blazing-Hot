package com.dudko.blazinghot.mixin.forge;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutRenderer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(value = SpoutRenderer.class, remap = false)
public abstract class SpoutRendererMixin {

	@ModifyVariable(method = "renderSafe(Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
			at = @At(value = "STORE"),
			name = "processingTicks")
	public int modifyProcessingTicks(int value, @Local(argsOnly = true) SpoutBlockEntity be) {
		CastingDepotBlockEntity depot = blazinghot$getCastingDepot(be);
		if (depot == null) return value;

		SpoutCastingBehaviour behaviour = depot.getBehaviour(SpoutCastingBehaviour.TYPE);
		int processingTicks = behaviour.getProcessingTicks();
		if (processingTicks == -1) return processingTicks;
		int duration = behaviour.getRecipeProcessingDuration();

		return duration - processingTicks;
	}

	@ModifyVariable(method = "renderSafe(Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
			at = @At(value = "STORE", ordinal = 0),
			name = "processingProgress")
	public float modifyProcessingProgress(float value, @Local(argsOnly = true) SpoutBlockEntity be, @Local(name = "processingTicks") int processingTicks) {
		CastingDepotBlockEntity depot = blazinghot$getCastingDepot(be);
		if (depot == null) return value;

		SpoutCastingBehaviour behaviour = depot.getBehaviour(SpoutCastingBehaviour.TYPE);
		int duration = behaviour.getRecipeProcessingDuration();

		return 1 - (float) (processingTicks - 5) / (10f / SpoutBlockEntity.FILLING_TIME * duration);
	}

	@Unique
	@Nullable
	private CastingDepotBlockEntity blazinghot$getCastingDepot(SpoutBlockEntity spout) {
		Level level = spout.getLevel();
		if (level == null) return null;

		BlockPos pos = spout.getBlockPos();
		BlockEntity beBelow = level.getBlockEntity(pos.below(2));

		if (!(beBelow instanceof CastingDepotBlockEntity depot)) return null;
		return depot;
	}

}
