package com.dudko.blazinghot.mixin;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.mixin_interfaces.ISmartBlockEntity;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@SuppressWarnings("DataFlowIssue")
@Mixin(value = SmartBlockEntity.class, remap = false)
public abstract class SmartBlockEntityMixin implements ISmartBlockEntity {

    @Shadow
    public <T extends BlockEntityBehaviour> T getBehaviour(BehaviourType<T> type) {
        return null;
    }

    @Override
    public void blazinghot$registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements) {
        SmartBlockEntity self = (SmartBlockEntity) (Object) this;
        for (BlockEntityBehaviour behaviour : behaviours) {
            if (behaviour instanceof IAdvancementBehaviour ab) {
                ab.blazinghot$add(advancements);
                return;
            }
        }
        behaviours.add(((IAdvancementBehaviour) new AdvancementBehaviour(self)).blazinghot$add(advancements));
    }


    @Override
    public void blazinghot$award(BlazingAdvancement advancement) {
        IAdvancementBehaviour behaviour = (IAdvancementBehaviour) getBehaviour(AdvancementBehaviour.TYPE);
        if (behaviour != null)
            behaviour.blazinghot$awardPlayer(advancement);
    }

    @Override
    public void blazinghot$awardIfNear(BlazingAdvancement advancement, int range) {
        IAdvancementBehaviour behaviour = (IAdvancementBehaviour) getBehaviour(AdvancementBehaviour.TYPE);
        if (behaviour != null)
            behaviour.blazinghot$awardPlayerIfNear(advancement, range);
    }
}
