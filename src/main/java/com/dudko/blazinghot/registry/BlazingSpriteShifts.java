package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

public class BlazingSpriteShifts {

    public static final CTSpriteShiftEntry BLAZE_CASING = omni("blaze_casing");

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return CTSpriteShifter.getCT(type, BlazingHot.asResource("block/" + blockTextureName),
                                     BlazingHot.asResource("block/" + blockTextureName + "_connected"));
    }

}
