package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancementBehaviour;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = SmartBlockEntity.class, remap = false)
public abstract class SmartBlockEntityMixin implements IAdvancementBehaviour {

    @Shadow
    public <T extends BlockEntityBehaviour> T getBehaviour(BehaviourType<T> type) {
        return null;
    }

    @Override
    public void blazinghot$award(BlazingAdvancement advancement) {
        BlazingAdvancementBehaviour behaviour = getBehaviour(BlazingAdvancementBehaviour.TYPE);
        if (behaviour != null)
            behaviour.awardPlayer(advancement);
    }

    @Override
    public void blazinghot$awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance) {
        BlazingAdvancementBehaviour behaviour = getBehaviour(BlazingAdvancementBehaviour.TYPE);
        if (behaviour != null)
            behaviour.awardPlayerIfNear(advancement, maxDistance);
    }

    @Override
    public void blazinghot$registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements) {
        for (BlockEntityBehaviour behaviour : behaviours) {
            if (behaviour instanceof BlazingAdvancementBehaviour ab) {
                ab.add(advancements);
                return;
            }
        }
        behaviours.add(new BlazingAdvancementBehaviour((SmartBlockEntity) (Object) this, advancements));
    }
}
