package com.dudko.blazinghot.data.advancement.forge;

import com.dudko.blazinghot.data.advancement.BlazingAdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;

public class BlazingAdvancementBehaviourImpl {
    public static void setPlacedBy(Level worldIn, BlockPos pos, LivingEntity placer) {
        BlazingAdvancementBehaviour
                behaviour = BlockEntityBehaviour.get(worldIn, pos, BlazingAdvancementBehaviour.TYPE);
        if (behaviour == null)
            return;
        if (placer instanceof ServerPlayer player && !(player instanceof FakePlayer))
            behaviour.setPlayer(placer.getUUID());
    }
}
