package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import java.util.function.Consumer;

import com.dudko.blazinghot.registry.BlazingPartialModels;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;

public class BlazeMixerVisual extends SingleAxisRotatingVisual<BlazeMixerBlockEntity> implements SimpleDynamicVisual {
	private final RotatingInstance mixerHead;
	private final OrientedInstance mixerPole;
	private final BlazeMixerBlockEntity mixer;

	public BlazeMixerVisual(VisualizationContext context, BlazeMixerBlockEntity blockEntity, float partialTick) {
		super(context, blockEntity, partialTick, Models.partial(BlazingPartialModels.SHAFTLESS_CRIMSON_COGWHEEL));
		this.mixer = blockEntity;
		this.mixerHead =
				this
						.instancerProvider()
						.instancer(AllInstanceTypes.ROTATING, Models.partial(BlazingPartialModels.BLAZE_MIXER_HEAD))
						.createInstance();
		this.mixerHead.setRotationAxis(Direction.Axis.Y);
		this.mixerPole =
				this
						.instancerProvider()
						.instancer(InstanceTypes.ORIENTED, Models.partial(BlazingPartialModels.BLAZE_MIXER_POLE))
						.createInstance();
		this.animate(partialTick);
	}

	public void beginFrame(DynamicVisual.Context ctx) {
		this.animate(ctx.partialTick());
	}

	private void animate(float pt) {
		float renderedHeadOffset = this.mixer.getRenderedHeadOffset(pt);
		this.transformPole(renderedHeadOffset);
		this.transformHead(renderedHeadOffset, pt);
	}

	private void transformHead(float renderedHeadOffset, float pt) {
		float speed = this.mixer.getRenderedHeadRotationSpeed(pt);
		this.mixerHead
				.setPosition(this.getVisualPosition())
				.nudge(0.0F, -renderedHeadOffset, 0.0F)
				.setRotationalSpeed(speed * 2.0F * 6.0F)
				.setChanged();
	}

	private void transformPole(float renderedHeadOffset) {
		this.mixerPole
				.position(this.getVisualPosition())
				.translatePosition(0.0F, -renderedHeadOffset, 0.0F)
				.setChanged();
	}

	public void updateLight(float partialTick) {
		super.updateLight(partialTick);
		this.relight(this.pos.below(), this.mixerHead);
		this.relight(this.mixerPole);
	}

	protected void _delete() {
		super._delete();
		this.mixerHead.delete();
		this.mixerPole.delete();
	}

	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		super.collectCrumblingInstances(consumer);
		consumer.accept(this.mixerHead);
		consumer.accept(this.mixerPole);
	}
}
