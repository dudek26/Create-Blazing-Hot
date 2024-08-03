package com.dudko.blazinghot.content.kinetics.blaze_mixer.forge;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe.durationToFuelCost;
import static com.dudko.blazinghot.util.FluidUtil.platformedAmount;

@SuppressWarnings("UnstableApiUsage")
public class BlazeMixerBlockEntityImpl extends BlazeMixerBlockEntity {

    public int fuelCost;

    public BlazeMixerBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, (int) platformedAmount(FluidUtil.BUCKET));
        tank.whenFluidUpdates(() -> {
            if (getBasin().isPresent()) getBasin().get().notifyChangeOfContents();
        });
        behaviours.add(tank);

        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.MIXER);
    }

    @NotNull
    public FluidStack getFluidStack() {
        return tank.getPrimaryHandler().getFluid();
    }

    public void updateFueled() {
        FluidState fluidState = getFluidStack().getFluid().defaultFluidState();

        fueled = fluidState.is(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag) && fuelAmount() > 0;
    }

    public boolean hasFuel(int amount) {
        return hasFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag, amount);
    }

    public boolean hasFuel(TagKey<Fluid> tag, int amount) {
        return hasFuel(FluidIngredient.fromTag(tag, amount));
    }

    public boolean hasFuel(FluidIngredient fluidIngredient) {
        return (fluidIngredient.test(getFluidStack()) && fluidIngredient.getRequiredAmount() <= fuelAmount())
                || fluidIngredient.getRequiredAmount() == 0;
    }

    public int fuelAmount() {
        return getFluidStack().getAmount();
    }

    @Override
    public void tick() {
        super.tick();

        dripTicks++;
        if (level != null && level.isClientSide && dripTicks >= 10) {
            dripTicks = 0;
            renderFuelParticles();
        }

        updateFueled();

        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
            basinChecker.scheduleUpdate();
            return;
        }

        float speed = Math.abs(getSpeed());
        if (running && level != null) {
            if (level.isClientSide && runningTicks == 20) renderParticles();


            if ((!level.isClientSide || isVirtual()) && runningTicks == 20) {
                if (processingTicks < 0) {
                    float recipeSpeed = 1;
                    fuelCost = 0;
                    blazeMixing = false;
                    int t = 0;
                    if (currentRecipe instanceof ProcessingRecipe) {
                        t = ((ProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                        if (t != 0) {
                            recipeSpeed = t / 100f;
                        }
                        if (currentRecipe instanceof BlazeMixingRecipe blazeMixingRecipe && blazeMixingRecipe
                                .getFuelFluid()
                                .test(getFluidStack())) {
                            fuelCost = blazeMixingRecipe.getFuelFluid().getRequiredAmount();
                            blazeMixing = true;
                        }
                    }
                    int calculatedCost = (int) platformedAmount(durationToFuelCost(t));
                    if (hasFuel(calculatedCost) && !(currentRecipe instanceof BlazeMixingRecipe)) {
                        recipeSpeed /= 2;
                        blazeMixing = true;
                        fuelCost = calculatedCost;
                    }

                    processingTicks =
                            Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512);

                    Optional<BasinBlockEntity> basin = getBasin();
                    if (basin.isPresent()) {
                        Couple<SmartFluidTankBehaviour> tanks = basin.get().getTanks();
                        if (!tanks.getFirst().isEmpty() || !tanks.getSecond().isEmpty()) level.playSound(null,
                                worldPosition,
                                SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,
                                SoundSource.BLOCKS,
                                .75f,
                                speed < 65 ? .75f : 1.5f);
                    }

                }
                else {
                    processingTicks--;
                    if (processingTicks == 0) {
                        runningTicks++;
                        processingTicks = -1;
                        blazeMixing = false;
                        FluidStack newFuel = getFluidStack().copy();
                        newFuel.setAmount(getFluidStack().getAmount() - fuelCost);
                        tank.getPrimaryHandler().setFluid(newFuel);
                        applyBasinRecipe();
                        sendData();
                    }
                }
            }

            if (runningTicks != 20) runningTicks++;
        }
    }

    public void renderFuelParticles() {
        if (getFluidStack().isEmpty()) return;

        Vec3 offset = new Vec3(-0.3 + Math.random() * 0.55, -0.3 + Math.random() * 0.55, -0.3 + Math.random() * 0.55);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));

        double runningOffset = running && runningTicks != 0 ? 1 - (double) 1 / Math.min(runningTicks, 40) : 0;

        assert level != null;

        level.addParticle(FluidFX.getDrippingParticle(getFluidStack()),
                center.x,
                center.y - 1 - runningOffset,
                center.z,
                0,
                0,
                0);

        if (getFluidStack().getFluid().defaultFluidState().is(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag))
            level.addParticle(ParticleTypes.SMALL_FLAME,
                    center.x,
                    center.y - 1 - runningOffset,
                    center.z,
                    offset.x * 0.05,
                    offset.y * 0.05,
                    offset.z * 0.05);
    }

    public void renderParticles() {
        Optional<BasinBlockEntity> basin = getBasin();
        if (basin.isEmpty() || level == null) return;

        for (SmartInventory inv : basin.get().getInvs()) {
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getItem(slot);
                if (stackInSlot.isEmpty()) continue;
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                spillParticle(data);
            }
        }

        for (SmartFluidTankBehaviour behaviour : basin.get().getTanks()) {
            if (behaviour == null) continue;
            for (TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0)) continue;
                spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
            }
        }
    }

    @Override
    protected <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe) {
        if (recipe == null) return false;
        Optional<BasinBlockEntity> basin = getBasin();
        if (basin.isEmpty()) return false;

        if (recipe instanceof BlazeMixingRecipe bmxRecipe) {
            return BasinRecipe.match(basin.get(), bmxRecipe) && hasFuel(bmxRecipe.getFuelFluid());
        }
        else if (recipe instanceof MixingRecipe) {
            assert level != null;
            RecipeManager manager = level.getRecipeManager();
            List<BlazeMixingRecipe> bmRecipes = manager.getAllRecipesFor(BlazingRecipeTypes.BLAZE_MIXING.getType());
            for (BlazeMixingRecipe bmRecipe : bmRecipes) {
                if (doInputsMatch(bmRecipe, recipe)) return false;
            }
        }

        return BasinRecipe.match(basin.get(), recipe);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER && side != Direction.DOWN) return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean kinetics = kineticStatsTooltip(tooltip, isPlayerSneaking);
        if (kinetics) tooltip.add(Component.empty());
        boolean
                fluids =
                containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));

        return kinetics || fluids;
    }
}
