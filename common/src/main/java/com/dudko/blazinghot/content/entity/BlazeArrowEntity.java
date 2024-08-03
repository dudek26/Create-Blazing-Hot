package com.dudko.blazinghot.content.entity;

import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlazeArrowEntity extends AbstractArrow {

    private final Item referenceItem;

    public BlazeArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.referenceItem = BlazingItems.BLAZE_ARROW.asItem();
    }

    public BlazeArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level, Item referenceItem) {
        super(entityType, shooter, level);
        this.referenceItem = referenceItem;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(this.referenceItem);
    }

    public void tick() {

        if (this.level().isClientSide && !this.inGround && this.level().dimension() == ContraptionWorld.NETHER) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        super.tick();

    }

    @ExpectPlatform
    public static BlazeArrowEntity create(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level, Item referenceItem) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static BlazeArrowEntity create(EntityType<? extends AbstractArrow> entityType, Level level) {
        throw new AssertionError();
    }

}
