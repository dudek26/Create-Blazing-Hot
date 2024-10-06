package com.dudko.blazinghot.content.block.modern_lamp;

import static net.minecraft.ChatFormatting.GRAY;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.data.lang.BlazingLang;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@MethodsReturnNonnullByDefault
public class ModernLampBlockEntity extends BlockEntity implements IHaveGoggleInformation {

	public boolean powered;
	public boolean locked;

	public ModernLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.putBoolean("powered", powered);
		tag.putBoolean("locked", locked);
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		powered = tag.getBoolean("powered");
		locked = tag.getBoolean("locked");
		super.load(tag);
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithFullMetadata();
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
