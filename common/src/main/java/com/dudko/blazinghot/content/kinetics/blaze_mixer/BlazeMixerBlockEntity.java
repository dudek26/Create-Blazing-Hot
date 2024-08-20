package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.architectury.injectables.annotations.ExpectPlatform;
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
import net.minecraft.world.item.crafting.Ingredient;
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

    private int ancientDebrisMelted;

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

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.MIXER);
        registerAwardables(behaviours,
                BlazingAdvancements.BLAZE_MIXER,
                BlazingAdvancements.MOLTEN_GOLD,
                BlazingAdvancements.MOLTEN_BLAZE_GOLD,
                BlazingAdvancements.BLAZE_MIXER_MAX,
                BlazingAdvancements.ANCIENT_DEBRIS_MELTING);
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
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, -1.5, 0);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        running = compound.getBoolean("Running");
        runningTicks = compound.getInt("Ticks");
        fueled = compound.getBoolean("Fueled");
        blazeMixing = compound.getBoolean("BlazeMixing");
        ancientDebrisMelted = compound.getInt("AncientDebrisMelted");
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
        compound.putInt("AncientDebrisMelted", ancientDebrisMelted);
        super.write(compound, clientPacket);
    }

    public static float multipliedRecipeSpeed(float speed, Recipe<?> r) {
        if (r == null) return speed;

        if (r instanceof MixingRecipe && PotionMixingRecipes.ALL.contains(r)) {
            return speed / BlazingConfigs.server().blazeBrewingSpeedMultiplier.getF();
        }
        else if (r.getType() == AllRecipeTypes.MIXING.getType()) {
            return speed / BlazingConfigs.server().blazeMixingSpeedMultiplier.getF();
        }
        else if ((r instanceof CraftingRecipe
                && !(r instanceof ShapedRecipe)
                && AllConfigs.server().recipes.allowShapelessInMixer.get()
                && r.getIngredients().size() > 1
                && !MechanicalPressBlockEntity.canCompress(r)) && !AllRecipeTypes.shouldIgnoreInAutomation(r)) {
            return speed / BlazingConfigs.server().blazeShapelessSpeedMultiplier.getF();
        }
        return speed;
    }

    public void updateAdvancements(Recipe<?> r) {
        award(BlazingAdvancements.BLAZE_MIXER);
        if (r instanceof ProcessingRecipe<?> recipe) {
            if (recipe.getId().equals(BlazingHot.asResource("blaze_mixing/melting/ancient_debris"))) {
                ancientDebrisMelted++;
                if (ancientDebrisMelted >= 15) {
                    award(BlazingAdvancements.ANCIENT_DEBRIS_MELTING);
                    ancientDebrisMelted = 0;
                }
            }

            if (MultiFluids.recipeResultContains(recipe, MoltenMetals.GOLD.fluidTag())) {
                award(BlazingAdvancements.MOLTEN_GOLD);
            }

            if (MultiFluids.recipeResultContains(recipe, MoltenMetals.BLAZE_GOLD.fluidTag())) {
                award(BlazingAdvancements.MOLTEN_BLAZE_GOLD);
            }

            //noinspection ConstantValue
            if (Mth.abs(getSpeed()) >= AllConfigs.server().kinetics.maxRotationSpeed.get() &&
                    hasFuel(MultiFluids.Constants.BUCKET.platformed())) {
                award(BlazingAdvancements.BLAZE_MIXER_MAX);
            }
        }
    }

    public abstract long getFuelAmount();

    public abstract boolean hasFuel(long amount);

    public abstract boolean hasFuel(TagKey<Fluid> tag, long amount);

    public abstract boolean hasFuel(FluidIngredient fluidIngredient);

    public abstract void updateFueled();

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
    protected abstract <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe);

    public static boolean doInputsMatch(ProcessingRecipe<?> a, ProcessingRecipe<?> b) {
        return doItemInputsMatch(a, b) && doFluidInputsMatch(a, b);
    }

    public static boolean doItemInputsMatch(ProcessingRecipe<?> a, ProcessingRecipe<?> b) {
        if (!a.getIngredients().isEmpty() && !b.getIngredients().isEmpty()) {
            List<ItemStack[]> allItems = a.getIngredients().stream().map(Ingredient::getItems).toList();
            for (ItemStack[] matchingStacks : allItems) {
                boolean matched = false;
                if (matchingStacks.length != 0) {
                    matched = b.getIngredients().stream().anyMatch(i -> i.test(matchingStacks[0]));
                }
                if (matched) continue;
                return false;
            }
            return true;
        }
        else return !a.getFluidIngredients().isEmpty() && !b.getFluidIngredients().isEmpty();
    }

    @ExpectPlatform
    public static boolean doFluidInputsMatch(ProcessingRecipe<?> a, ProcessingRecipe<?> b) {
        return true; // no assertion error so IntelliJ doesn't cry
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


    public void registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements) {
        ((IAdvancementBehaviour) this).blazinghot$registerAwardables(behaviours, advancements);
    }

    public void award(BlazingAdvancement advancement) {
        ((IAdvancementBehaviour) this).blazinghot$award(advancement);
    }

    public void awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance) {
        ((IAdvancementBehaviour) this).blazinghot$award(advancement);
    }
}
