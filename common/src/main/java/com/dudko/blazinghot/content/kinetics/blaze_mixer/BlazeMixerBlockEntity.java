package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.util.FluidUtil;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public abstract class BlazeMixerBlockEntity extends BasinOperatingBlockEntity implements IHaveGoggleInformation {

    protected static final Object shapelessOrMixingRecipesKey = new Object();

    public int runningTicks;
    public int processingTicks;
    public int dripTicks = 0;
    public boolean running;
    public boolean fueled;
    public boolean blazeMixing;
    public long fuelCost;

    protected SmartFluidTankBehaviour tank;

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
        tank = SmartFluidTankBehaviour.single(this, FluidUtil.platformedAmount(FluidUtil.BUCKET));
        tank.whenFluidUpdates(() -> {
            if (getBasin().isPresent()) getBasin().get().notifyChangeOfContents();
        });
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
        blazeMixing = compound.getBoolean("BlazeMixing");
        super.read(compound, clientPacket);

        if (clientPacket && hasLevel())
            getBasin().ifPresent(bte -> bte.setAreFluidsMoving(running && runningTicks <= 20));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
        compound.putBoolean("Fueled", fueled);
        compound.putBoolean("BlazeMixing", blazeMixing);
        super.write(compound, clientPacket);
    }

    public abstract void updateFueled();

    public abstract boolean hasFuel(double amount);

    public abstract boolean hasFuel(TagKey<Fluid> tag, double amount);

    public abstract boolean hasFuel(FluidIngredient fluidIngredient);

    public abstract long fuelAmount();

    public abstract void renderFuelParticles();

    public abstract void renderParticles();

    protected void spillParticle(ParticleOptions data) {
        assert level != null;
        float angle = level.random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.25f);
        offset = VecHelper.rotate(offset, angle, Axis.Y);
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Axis.Y).add(0, .25f, 0);
        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y - 1.75f, center.z, target.x, target.y, target.z);
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
                || r.getType() == AllRecipeTypes.MIXING.getType())
                || r.getType() == BlazingRecipeTypes.BLAZE_MIXING.getType();
    }

    @Override
    protected abstract <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe);

    public static boolean doInputsMatch(Recipe<?> a, Recipe<?> b) {
        if (!a.getIngredients().isEmpty() && !b.getIngredients().isEmpty()) {
            ItemStack[] matchingStacks = a.getIngredients().get(0).getItems();
            if (matchingStacks.length != 0) {
                if (b.getIngredients().get(0).test(matchingStacks[0])) {
                    return true;
                }
            }
        }
        return false;
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
    public abstract boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking);

    public boolean kineticStatsTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = false;

        if (!IRotate.StressImpact.isEnabled()) return added;
        float stressAtBase = calculateStressApplied();
        if (Mth.equal(stressAtBase, 0)) return added;

        Lang.translate("gui.goggles.kinetic_stats").forGoggles(tooltip);

        addStressImpactStats(tooltip, stressAtBase);

        return true;
    }
}
