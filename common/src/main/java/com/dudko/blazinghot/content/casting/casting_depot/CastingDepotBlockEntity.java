package com.dudko.blazinghot.content.casting.casting_depot;

import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.util.TooltipUtil;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public abstract class CastingDepotBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	protected SmartFluidTankBehaviour tank;
	protected CastingDepotBehaviour depotBehaviour;
	protected SpoutCastingBehaviour castingBehaviour;
	protected FilteringBehaviour filtering;

	protected boolean contentsChanged;

	protected Fluid visualFluid;

	protected CastingDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		visualFluid = Fluids.EMPTY;
	}

	@ExpectPlatform
	public static CastingDepotBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@Override
	public void sendData() {
		super.sendData();
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return moldTooltip(tooltip, isPlayerSneaking);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(depotBehaviour = CastingDepotBehaviour.of(this, CastingDepotBehaviour.TYPE));
		depotBehaviour.addSubBehaviours(behaviours);

		filtering =
				new FilteringBehaviour(this, new CastingDepotValueBox())
						.withCallback(newFilter -> contentsChanged = true)
						.forRecipes();
		behaviours.add(filtering);

		behaviours.add(castingBehaviour = SpoutCastingBehaviour.of(this));

		registerAwardables(behaviours, BlazingAdvancements.CASTING, BlazingAdvancements.STURDY_MOLD);
	}

	public SpoutCastingBehaviour.State getState() {
		return castingBehaviour.getState();
	}

	@SuppressWarnings("SameReturnValue")
	private boolean moldTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

		BlazingLang.CASTING_GOGGLE_TITLE.translate().forGoggles(tooltip);

		if (getHeldItem().isEmpty()) {
			BlazingLang.CASTING_GOGGLE_NO_MOLD.translate().style(ChatFormatting.GRAY).forGoggles(tooltip);
		}
		else {
			Component name = getHeldItem().getHoverName().copy().withStyle(ChatFormatting.GRAY);
			BlazingHot.lang().add(name).forGoggles(tooltip);
		}

		ChatFormatting coolingSpeedColor = ChatFormatting.AQUA;

		if (getCoolingSpeed() < 0) {
			coolingSpeedColor = ChatFormatting.DARK_RED;
		}
		else if (getCoolingSpeed() == 0) {
			coolingSpeedColor = ChatFormatting.RED;
		}
		else if (getCoolingSpeed() < 1) {
			coolingSpeedColor = ChatFormatting.GOLD;
		}
		else if (getCoolingSpeed() == 1) {
			coolingSpeedColor = ChatFormatting.GREEN;
		}

		MutableComponent speedComponent = Component.literal("x" + getCoolingSpeed()).withStyle(coolingSpeedColor);

		LangBuilder cooling = BlazingLang.CASTING_GOGGLE_COOLING_SPEED.translate().style(ChatFormatting.GRAY);
		cooling.add(Component.literal(" "));
		cooling.add(speedComponent);


		switch (getState()) {
			case NONE -> {
				ItemStack outputStack = getOutputItem();
				if (!outputStack.isEmpty()) {
					LangBuilder output = BlazingHot.lang().text("← ").style(ChatFormatting.GREEN);
					output.add(outputStack.getHoverName().copy().withStyle(ChatFormatting.GRAY));
					output.forGoggles(tooltip);
				}
				cooling.forGoggles(tooltip);
			}
			case FILLING -> {
				LangBuilder filling = BlazingHot.lang().text("→ ").style(ChatFormatting.GOLD);
				filling.add(BlazingLang.fluidName(getVisualFluid()).style(ChatFormatting.GRAY));
				MutableComponent
						progress =
						TooltipUtil.asciiProgressBar(10,
								castingBehaviour.getProcessingTicks(),
								castingBehaviour.getRecipeProcessingDuration());

				filling.forGoggles(tooltip);
				BlazingHot.lang().add(progress).forGoggles(tooltip);

				cooling.forGoggles(tooltip);
			}
			case COOLING -> {
				Component castItem = castingBehaviour.castItem.getHoverName();

				LangBuilder
						filling =
						BlazingHot
								.lang()
								.text("❄ ")
								.style(ChatFormatting.AQUA)
								.add(castItem.copy().withStyle(ChatFormatting.GRAY));

				MutableComponent
						progress =
						TooltipUtil.asciiProgressBar(10,
								castingBehaviour.getCoolingTicks(),
								castingBehaviour.getRecipeCoolingDuration());

				filling.forGoggles(tooltip);
				BlazingHot
						.lang()
						.add(progress)
						.add(Component
								.literal(" (")
								.withStyle(ChatFormatting.GRAY)
								.append(speedComponent)
								.append(")")
								.withStyle(ChatFormatting.GRAY))
						.forGoggles(tooltip);
			}
		}

		return true;
	}

	public Fluid getVisualFluid() {
		return visualFluid;
	}

	public void setVisualFluid(Fluid visualFluid) {
		this.visualFluid = visualFluid;
		notifyUpdate();
	}

	public abstract ItemStack getHeldItem();

	public abstract ItemStack getOutputItem();

	public abstract void setOutputItem(ItemStack stack);

	public abstract float getCoolingSpeed();

	public abstract void setFluid(Fluid fluid, long amount);

	public abstract void resetFluid();

	public SmartFluidTankBehaviour getTank() {
		return tank;
	}

	public FilteringBehaviour getFilter() {
		return filtering;
	}

	public boolean isPowered() {
		return getBlockState().getValue(CastingDepotBlock.POWERED);
	}

	public void registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements) {
		((IAdvancementBehaviour) this).blazinghot$registerAwardables(behaviours, advancements);
	}

	public void award(BlazingAdvancement advancement) {
		((IAdvancementBehaviour) this).blazinghot$award(advancement);
	}

	public void awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance) {
		((IAdvancementBehaviour) this).blazinghot$award(advancement);
	}

	static class CastingDepotValueBox extends ValueBoxTransform.Sided {

		@Override
		protected Vec3 getSouthLocation() {
			return VecHelper.voxelSpace(8, 7.25, 15.5);
		}

		@Override
		protected boolean isSideActive(BlockState state, Direction direction) {
			return direction.getAxis().isHorizontal();
		}

	}
}
