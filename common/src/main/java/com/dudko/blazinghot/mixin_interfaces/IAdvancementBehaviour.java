package com.dudko.blazinghot.mixin_interfaces;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;

public interface IAdvancementBehaviour {

    AdvancementBehaviour blazinghot$add(BlazingAdvancement... advancements);

    void blazinghot$awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance);

    void blazinghot$awardPlayer(BlazingAdvancement advancement);

    void blazinghot$award(BlazingAdvancement advancement, Player player);

    static void blazinghot$tryAward(BlockGetter reader, BlockPos pos, BlazingAdvancement advancement) {

    }

    void blazinghot$removeAwarded();
}
