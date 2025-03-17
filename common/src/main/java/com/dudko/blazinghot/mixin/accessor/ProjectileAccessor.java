package com.dudko.blazinghot.mixin.accessor;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.projectile.Projectile;

@Mixin(Projectile.class)
public interface ProjectileAccessor {

	@Accessor
	UUID getOwnerUUID();

}
