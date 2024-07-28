package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class BlazeMixerBlockEntity extends BasinOperatingBlockEntity implements IHaveGoggleInformation, SidedStorageBlockEntity {

    private static final Object shapelessOrMixingRecipesKey = new Object();

    public int runningTicks;
    public int processingTicks;
    public int dripTicks = 0;
    public boolean running;
    public boolean fueled;
    public long minFuel = FluidConstants.fromBucketFraction(1, 100);
    public boolean blazeMixing;
    public long fuelCost;

    SmartFluidTankBehaviour tank;

    public BlazeMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public float getRenderedHeadOffset(float partialTicks) {
        int localTick;
        float offset = 0;
        if (running) {
            if (runningTicks < 20) {
                localTick = runningTicks;
                float num = (localTick + partialTicks) / 20f;
                num = ((2 - Mth.cos((float) (num * Math.PI))) / 2);
                offset = num - .5f;
            }
            else if (runningTicks == 20) {
                offset = 1;
            }
            else {
                localTick = 40 - runningTicks;
                float num = (localTick - partialTicks) / 20f;
                num = ((2 - Mth.cos((float) (num * Math.PI))) / 2);
                offset = num - .5f;
            }
        }
        return offset + 7 / 16f;
    }

    public float getRenderedHeadRotationSpeed(float partialTicks) {
        float speed = getSpeed();
        if (running) {
            if (runningTicks < 15) {
                return speed;
            }
            if (runningTicks <= 20) {
                return speed * 2;
            }
            return speed;
        }
        return speed / 2;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, FluidConstants.BUCKET);
        behaviours.add(tank);
        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.MIXER);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, -1.5, 0);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        fueled = compound.getBoolean("Fueled");
        super.read(compound, clientPacket);

        if (clientPacket && hasLevel()) getBasin().ifPresent(bte -> bte.setAreFluidsMoving(running
                && runningTicks <= 20));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
        compound.putBoolean("Fueled", fueled);
        super.write(compound, clientPacket);
    }

    public void updateFueled() {
        FluidStack fluidStack = tank.getPrimaryTank().getTank().getFluid();
        FluidState fluidState = fluidStack.getFluid().defaultFluidState();

        fueled = fluidState.is(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag) && fluidStack.getAmount() >= minFuel;
    }

    private long fuelAmount() {
        return tank.getPrimaryTank().getTank().getFluidAmount();
    }

    @Override
    public void tick() {
        super.tick();

        dripTicks++;
        if (dripTicks >= 10) {
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
                    fuelCost = minFuel;
                    if (currentRecipe instanceof ProcessingRecipe) {
                        int t = ((ProcessingRecipe<?>) currentRecipe).getProcessingDuration();
                        if (t != 0) {
                            recipeSpeed = t / 100f;
                            if (currentRecipe instanceof BlazeMixingRecipe blazeMixingRecipe
                                    && blazeMixingRecipe.hasFuel(tank.getPrimaryTank().getTank().getFluid())) {
                                fuelCost = blazeMixingRecipe.getFuelFluid().getRequiredAmount();
                                blazeMixing = true;
                            }
                            else {
                                fuelCost = t * 27L;
                                if (fuelAmount() >= fuelCost) {
                                    recipeSpeed *= 2;
                                    fuelCost = t * 27L;
                                    blazeMixing = true;
                                }
                                else blazeMixing = false;
                            }
                        }
                    }

                    processingTicks = Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1,
                            1,
                            512);

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
                        if (blazeMixing) tank.getPrimaryTank().getTank().getFluid().setAmount(fuelAmount() - fuelCost);
                        applyBasinRecipe();
                        sendData();
                    }
                }
            }

            if (runningTicks != 20) runningTicks++;
        }
    }

    public void renderFuelParticles() {
        FluidStack fluidStack = tank.getPrimaryTank().getTank().getFluid();
        if (fluidStack.isEmpty()) return;

        Vec3 offset = new Vec3(-0.3 + Math.random() * 0.55, -0.3 + Math.random() * 0.55, -0.3 + Math.random() * 0.55);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));

        double runningOffset = running && runningTicks != 0 ? 1 - (double) 1 / Math.min(runningTicks, 40) : 0;

        assert level != null;

        level.addParticle(FluidFX.getDrippingParticle(fluidStack),
                center.x,
                center.y - 1 - runningOffset,
                center.z,
                0,
                0,
                0);

        if (fluidStack.getFluid().defaultFluidState().is(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag)) level.addParticle(
                ParticleTypes.SMALL_FLAME,
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
            for (int slot = 0; slot < inv.getSlotCount(); slot++) {
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

    protected void spillParticle(ParticleOptions data) {
        assert level != null;
        float angle = level.random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.25f);
        offset = VecHelper.rotate(offset, angle, Axis.Y);
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Axis.Y).add(0, .25f, 0);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z);
        if (blazeMixing && Math.random() > 0.75f) level.addParticle(ParticleTypes.SMALL_FLAME,
                center.x,
                center.y - 1.75f,
                center.z,
                target.x / 4,
                target.y / 4,
                target.z / 4);
    }

    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        List<Recipe<?>> matchingRecipes = super.getMatchingRecipes();

        if (!AllConfigs.server().recipes.allowBrewingInMixer.get()) return matchingRecipes;

        Optional<BasinBlockEntity> basin = getBasin();
        if (basin.isEmpty()) return matchingRecipes;

        BasinBlockEntity basinBlockEntity = basin.get();
        if (basin.isEmpty()) return matchingRecipes;

        Storage<ItemVariant> availableItems = basinBlockEntity.getItemStorage(null);
        if (availableItems == null) return matchingRecipes;

        try (Transaction t = TransferUtil.getTransaction()) {
            for (StorageView<ItemVariant> view : availableItems.nonEmptyViews()) {
                List<MixingRecipe> list = PotionMixingRecipes.BY_ITEM.get(view.getResource().getItem());
                if (list == null) continue;
                for (MixingRecipe mixingRecipe : list)
                    if (matchBasinRecipe(mixingRecipe)) matchingRecipes.add(mixingRecipe);
            }
        }

        return matchingRecipes;
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return ((r instanceof CraftingRecipe
                && !(r instanceof ShapedRecipe)
                && AllConfigs.server().recipes.allowShapelessInMixer.get()
                && r.getIngredients().size() > 1
                && !MechanicalPressBlockEntity.canCompress(r)) && !AllRecipeTypes.shouldIgnoreInAutomation(r)
                || r.getType() == AllRecipeTypes.MIXING.getType()
                || r.getType() == BlazingRecipeTypes.BLAZE_MIXING.getType());
    }

    @Override
    protected <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe) {
        if (recipe == null) return false;
        Optional<BasinBlockEntity> basin = getBasin();
        if (basin.isEmpty()) return false;

        if (recipe instanceof BlazeMixingRecipe blazeMixingRecipe) {
            return BasinRecipe.match(basin.get(), blazeMixingRecipe)
                    && fuelAmount() >= blazeMixingRecipe.getFuelFluid().getRequiredAmount();
        }

        return BasinRecipe.match(basin.get(), recipe);
    }

    @Override
    public void startProcessingBasin() {
        if (running && runningTicks <= 20) return;
        super.startProcessingBasin();
        running = true;
        runningTicks = 0;
    }

    @Override
    public boolean continueWithPreviousRecipe() {
        runningTicks = 20;
        return true;
    }

    @Override
    protected void onBasinRemoved() {
        if (!running) return;
        runningTicks = 40;
        running = false;
    }

    @Override
    protected Object getRecipeCacheKey() {
        return shapelessOrMixingRecipesKey;
    }

    @Override
    protected boolean isRunning() {
        return running;
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger() {
        return Optional.of(AllAdvancements.MIXER);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        // SoundEvents.BLOCK_STONE_BREAK
        boolean slow = Math.abs(getSpeed()) < 65;
        if (slow && AnimationTickHolder.getTicks() % 2 == 0) return;
        if (runningTicks == 20) AllSoundEvents.MIXING.playAt(level, worldPosition, .75f, 1, true);
    }

    @Override
    public @Nullable Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
        if (face != Direction.DOWN) {
            return tank.getCapability();
        }
        return null;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        boolean kinetics = kineticStatsTooltip(tooltip, isPlayerSneaking);
        if (kinetics) tooltip.add(Component.empty());
        boolean fluids = containedFluidTooltip(tooltip, isPlayerSneaking, getFluidStorage(null));

        return kinetics || fluids;
    }

    private boolean kineticStatsTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = false;

        if (!IRotate.StressImpact.isEnabled())
            return added;
        float stressAtBase = calculateStressApplied();
        if (Mth.equal(stressAtBase, 0))
            return added;

        Lang.translate("gui.goggles.kinetic_stats")
            .forGoggles(tooltip);

        addStressImpactStats(tooltip, stressAtBase);

        return true;
    }
}
