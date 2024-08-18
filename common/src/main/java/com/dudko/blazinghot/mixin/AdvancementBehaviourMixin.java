package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(value = AdvancementBehaviour.class, remap = false)
public abstract class AdvancementBehaviourMixin extends BlockEntityBehaviour implements IAdvancementBehaviour {

    @Unique
    private final Set<BlazingAdvancement> blazinghot$advancements = new HashSet<>();

    @Shadow
    private UUID playerId;

    public AdvancementBehaviourMixin(SmartBlockEntity be) {
        super(be);
    }

    @Shadow(remap = true)
    private Player getPlayer() {
        return null;
    }

    @Override
    @Unique
    public AdvancementBehaviour blazinghot$add(BlazingAdvancement... advancements) {
        blazinghot$advancements.addAll(Arrays.asList(advancements));
        return (AdvancementBehaviour) (Object) this;
    }

    @Override
    @Unique
    public void blazinghot$awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance) {
        Player player = getPlayer();
        if (player == null)
            return;
        if (player.distanceToSqr(Vec3.atCenterOf(getPos())) > maxDistance * maxDistance)
            return;
        blazinghot$award(advancement, player);
    }

    @Override
    @Unique
    public void blazinghot$awardPlayer(BlazingAdvancement advancement) {
        Player player = getPlayer();
        if (player == null)
            return;
        blazinghot$award(advancement, player);
    }

    @Override
    @Unique
    public void blazinghot$award(BlazingAdvancement advancement, Player player) {
        if (blazinghot$advancements.contains(advancement))
            advancement.awardTo(player);
        blazinghot$removeAwarded();
    }

    @Override
    public void blazinghot$removeAwarded() {
        Player player = getPlayer();
        if (player == null)
            return;
        blazinghot$advancements.removeIf(c -> c.isAlreadyAwardedTo(player));
        if (blazinghot$advancements.isEmpty()) {
            playerId = null;
            blockEntity.setChanged();
        }
    }

}
