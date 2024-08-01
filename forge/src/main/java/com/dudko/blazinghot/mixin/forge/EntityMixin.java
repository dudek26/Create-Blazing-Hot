package com.dudko.blazinghot.mixin.forge;

import com.dudko.blazinghot.registry.BlazingFluids;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;


@Mixin(Entity.class)
public class EntityMixin {

    @Shadow
    protected boolean firstTick;
    @Shadow
    protected Object2DoubleMap<FluidType> forgeFluidTypeHeight;

    @Unique
    private boolean blazinghot$isInBurningFluid() {
        return !firstTick && blazinghot$BURNING_FLUIDS.stream().anyMatch(f -> forgeFluidTypeHeight.getDouble(f) > 0.0);
    }

    @Unique
    private final List<FluidType> blazinghot$BURNING_FLUIDS =
            List.of(BlazingFluids.MOLTEN_BLAZE_GOLD.getType(),
                    BlazingFluids.MOLTEN_GOLD.getType(),
                    BlazingFluids.MOLTEN_IRON.getType(),
                    BlazingFluids.NETHER_LAVA.getType());

    @ModifyReturnValue(method = "isInLava", at = @At("RETURN"))
    public boolean blazinghot$isInLavaInjector(boolean original) {
        return original || blazinghot$isInBurningFluid();
    }

}
