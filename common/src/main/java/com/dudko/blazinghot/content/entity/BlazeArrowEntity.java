package com.dudko.blazinghot.content.entity;

import static com.dudko.blazinghot.util.WorldUtil.dimensionFromString;
import static com.dudko.blazinghot.util.WorldUtil.dimensionToString;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.mixin.accessor.ProjectileAccessor;
import com.dudko.blazinghot.registry.BlazingItems;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeArrowEntity extends AbstractArrow {

	@Nullable
	private ResourceKey<Level> dimensionOrigin;

	public BlazeArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
		super(entityType, level);
		this.dimensionOrigin = level.dimension();
	}

	public BlazeArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
		super(entityType, owner, level, pickupItemStack, firedFromWeapon);
		this.dimensionOrigin = level.dimension();
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(BlazingItems.BLAZE_ARROW.asItem());
	}

	public void tick() {

		if (this.level().isClientSide && !this.inGround && this.level().dimension() == Level.NETHER) {
			this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
		}
		super.tick();

	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (this.level().dimension().equals(Level.NETHER)) setBaseDamage(getBaseDamage() * 1.5);

		super.onHitEntity(result);

		if (getOwner() != null
				&& getOwner() instanceof Player player
				&& result.getEntity() instanceof LivingEntity entity
				&& entity.level().dimension().equals(Level.NETHER)) {
			BlazingAdvancements.BLAZE_ARROW.awardTo(player);
			if (dimensionOrigin != null && dimensionOrigin.equals(Level.OVERWORLD) && (entity.getHealth() <= 0
					|| !entity.isAlive())) {
				BlazingAdvancements.BLAZE_ARROW_INTERDIMENSIONAL.awardTo(player);
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("DimensionOrigin", dimensionToString(dimensionOrigin));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		dimensionOrigin = dimensionFromString(compound.getString("DimensionOrigin"));
	}

	// TODO: check if it's still necessary
	@Override
	public @Nullable Entity getOwner() {
		Entity entity = super.getOwner();
		if (entity != null) return entity;
		if (getServer() != null) {
			Level overworld = getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				return overworld.getPlayerByUUID(((ProjectileAccessor) this).getOwnerUUID());
			}
		}
		return null;
	}
}
