package com.dudko.blazinghot.content.block.modern_lamp;

import static net.minecraft.ChatFormatting.GRAY;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.content.block.modern_lamp.AbstractModernLamp.ModernLampValueBox;
import com.dudko.blazinghot.content.block.modern_lamp.AbstractModernLampPanel.ModernLampPanelValueBox;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModernLampBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

	public boolean powered;
	public boolean locked;

	public ModernLampBehaviour behaviour;
	public ScrollValueBehaviour scrollValue;

	public ModernLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(behaviour = new ModernLampBehaviour(this));
		behaviours.add(scrollValue = new ScrollValueBehaviour(BlazingLang.LAMP_LIGHT.get(), this, getValueBoxTransform()));
	}

	protected ValueBoxTransform getValueBoxTransform() {
		if (getBlockState().getBlock() instanceof AbstractModernLampPanel) {
			return new ModernLampPanelValueBox();
		}
		return new ModernLampValueBox();
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (BlazingConfigs.client().modernLampGoggleTooltip.get().shouldRender(isPlayerSneaking)) {
			BlazingLang.LAMP_GOGGLE_TITLE.translate().forGoggles(tooltip);
			BlazingLang.LAMP_GOGGLE_STATE.translate().style(GRAY).forGoggles(tooltip);

			LangBuilder
				lockTranslation =
				locked ? BlazingLang.LAMP_GOGGLE_LOCKED.translate() : BlazingLang.LAMP_GOGGLE_UNLOCKED.translate();
			ChatFormatting lockStyle = locked ? ChatFormatting.RED : ChatFormatting.GREEN;

			lockTranslation.style(lockStyle).forGoggles(tooltip, 1);
		}

		return false;
	}
}
