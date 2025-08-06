package com.dudko.blazinghot.mixin.forge;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.platform.ForgeCatnipServices;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

@Mixin(value = SpoutRenderer.class, remap = false)
public abstract class SpoutRendererMixin {

	@Shadow
	@Final
	static final PartialModel[]
			BITS =
			{AllPartialModels.SPOUT_TOP, AllPartialModels.SPOUT_MIDDLE, AllPartialModels.SPOUT_BOTTOM};

	@Inject(method = "renderSafe(Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
			at = @At(value = "FIELD",
					ordinal = 0,
					target = "Lcom/simibubi/create/content/fluids/spout/SpoutBlockEntity;processingTicks:I"),
			cancellable = true)
	public void modifyProcessingProgress(SpoutBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci, @Local(
			name = "fluidStack") FluidStack fluidStack) {
		CastingDepotBlockEntity depot = blazinghot$getCastingDepot(be);
		if (depot == null) return;

		SpoutCastingBehaviour behaviour = depot.getBehaviour(SpoutCastingBehaviour.TYPE);
		int processingTicks = behaviour.getProcessingTicks();
		if (behaviour.getState() == SpoutCastingBehaviour.State.NONE) return;
		if (behaviour.getState() != SpoutCastingBehaviour.State.FILLING) processingTicks = -1;
		int duration = behaviour.getRecipeProcessingDuration();

		boolean isQuick = duration < 20;

		if (processingTicks != -1) processingTicks = duration - processingTicks;

		float processingPT = processingTicks - partialTicks;
		float radius = 0;

		if (!fluidStack.isEmpty() && processingTicks != -1) {
			float start = (float) ((1 / 25f) * Math.pow((processingPT - duration + 10), 2)) - 1;
			float end = (float) ((1 / 25f) * Math.pow((processingPT - 10), 2)) - 1;
			if (isQuick) {
				radius = Math.max(start, end);
			}
			else if (processingPT > duration - 10) radius = start;
			else if (processingPT < 10) radius = end;
			else radius = -1;
			radius = Mth.clamp(radius, -1, 0);
			AABB bb = new AABB(0.5, 0.0, 0.5, 0.5, -1.4, 0.5).inflate(radius / 32f);
			ForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack,
					(float) bb.minX,
					(float) bb.minY,
					(float) bb.minZ,
					(float) bb.maxX,
					(float) bb.maxY,
					(float) bb.maxZ,
					buffer,
					ms,
					light,
					true,
					true);
		}

		float squeeze = radius;
		if (processingPT < 0) squeeze = 0;
		else if (processingPT < 2 && !isQuick) squeeze = Mth.lerp(processingPT / 2f, 0, -1);
		else if (processingPT < duration - 10 && !isQuick) squeeze = -1;

		ms.pushPose();
		for (PartialModel bit : BITS) {
			CachedBuffers
					.partial(bit, be.getBlockState())
					.light(light)
					.renderInto(ms, buffer.getBuffer(RenderType.solid()));
			ms.translate(0, -3 * squeeze / 32f, 0);
		}
		ms.popPose();

		ci.cancel();
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
