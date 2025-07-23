package com.dudko.blazinghot.content.casting.casting_depot;

import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public abstract class CastingDepotBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	protected SmartFluidTankBehaviour tank;
	protected CastingDepotBehaviour depotBehaviour;
	protected CastingDepotBehaviour outputBehaviour;

	protected CastingDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
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

		behaviours.add(SpoutCastingBehaviour.of(this));
	}

	@SuppressWarnings("SameReturnValue")
	private boolean moldTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

		BlazingLang.CASTING_GOGGLE_TITLE.translate().forGoggles(tooltip);

		if (getHeldItem().isEmpty()) {
			BlazingLang.CASTING_GOGGLE_NO_MOLD.translate().style(ChatFormatting.GRAY).forGoggles(tooltip);
		}
		else {
			Style style = Style.EMPTY.withColor(ChatFormatting.GRAY);
			Component name = getHeldItem().getHoverName().copy().setStyle(style);
			BlazingHot.lang().add(name).forGoggles(tooltip);
		}


		LangBuilder cooling = BlazingLang.CASTING_GOGGLE_COOLING.translate().style(ChatFormatting.GRAY);
		cooling.add(Component.literal(" "));

		ChatFormatting color = ChatFormatting.AQUA;

		if (getCoolingSpeed() < 0) {
			color = ChatFormatting.DARK_RED;
		}
		else if (getCoolingSpeed() == 0) {
			color = ChatFormatting.RED;
		}
		else if (getCoolingSpeed() < 1) {
			color = ChatFormatting.GOLD;
		}
		else if (getCoolingSpeed() == 1) {
			color = ChatFormatting.GREEN;
		}

		MutableComponent
				speedComponent =
				Component.literal("x" + getCoolingSpeed()).setStyle(Style.EMPTY.withColor(color));

		cooling.add(speedComponent).forGoggles(tooltip);
		return true;
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
}
