package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.dudko.blazinghot.data.advancement.BlazingAdvancement;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.foundation.mixin_interfaces.IAdvancementBehaviour;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiFluids;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public abstract class BlazeMixerBlockEntity extends BasinOperatingBlockEntity implements IHaveGoggleInformation {

	protected static final Object shapelessOrMixingRecipesKey = new Object();

	public int runningTicks;
	public int processingTicks;
	public int dripTicks = 0;
	public boolean running;
	public boolean fueled;
	protected Mode mode = Mode.BLAZE;

	private int ancientDebrisMelted;

	public SmartFluidTankBehaviour tank;

	protected BlazeMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@ExpectPlatform
	public static BlazeMixerBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
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

	public float getRenderedHeadOffset(float partialTicks) {
		int localTick;
		float offset = 0;
		if (running) {
			if (runningTicks < 20) {
				localTick = runningTicks;
				float num = (localTick + partialTicks) / 20f;
				num = ((2 - Mth.cos((float) (num * Math.PI))) / 2);
				offset = num - .5f;
			} else if (runningTicks == 20) {
				offset = 1;
			} else {
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
	protected AABB createRenderBoundingBox() {
		return new AABB(worldPosition).expandTowards(0, -1.5, 0);
	}

	@Override
	protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		running = compound.getBoolean("Running");
		runningTicks = compound.getInt("Ticks");
		fueled = compound.getBoolean("Fueled");
		ancientDebrisMelted = compound.getInt("AncientDebrisMelted");

		String modeSerialized = compound.getString("Mode").toUpperCase();
		if (modeSerialized.isEmpty()) mode = Mode.BLAZE;
		else mode = Mode.valueOf(modeSerialized);

		super.read(compound, registries, clientPacket);
		if (clientPacket && hasLevel())
			getBasin().ifPresent(bte -> bte.setAreFluidsMoving(running && runningTicks <= 20));
	}

	@Override
	protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putInt("Ticks", runningTicks);
		compound.putBoolean("Fueled", fueled);
		compound.putInt("AncientDebrisMelted", ancientDebrisMelted);
		compound.putString("Mode", mode.toString());
		super.write(compound, registries, clientPacket);
	}

	public float recipeSpeedMultiplier(Recipe<?> recipe) {
		if (recipe == null) return 1;

		// brewing
		if (recipe instanceof MixingRecipe) {
			for (ItemStack stack : getAvailableItems()) {
				if (stack.isEmpty()) continue;

				List<MixingRecipe> list = PotionMixingRecipes.sortRecipesByItem(level).get(stack.getItem());
				if (list == null) continue;
				for (MixingRecipe mixingRecipe : list)
					if (matchBasinRecipe(mixingRecipe))
						return BlazingConfigs.server().recipes.fueledBrewingSpeedMultiplier.getF();
			}
		}

		// mixing
		if (recipe.getType() == AllRecipeTypes.MIXING.getType()) {
			return BlazingConfigs.server().recipes.fueledMixingSpeedMultiplier.getF();
		}

		// shapeless
		if (recipe instanceof CraftingRecipe) {
			return BlazingConfigs.server().recipes.fueledShapelessSpeedMultiplier.getF();
		}
		return 1;
	}

	public void updateAdvancements(Recipe<?> r) {
		award(BlazingAdvancements.BLAZE_MIXER);
		if (r instanceof StandardProcessingRecipe<?> recipe) {
			if (MultiFluids.recipeResultContains(recipe, BlazingMetals.ANCIENT_DEBRIS.getFluidTag()) && recipe
				.getIngredients()
				.stream()
				.anyMatch(i -> i.test(Items.ANCIENT_DEBRIS.getDefaultInstance()))) {
				ancientDebrisMelted++;
				if (ancientDebrisMelted >= 15) {
					award(BlazingAdvancements.ANCIENT_DEBRIS_MELTING);
					ancientDebrisMelted = 0;
				}
			}

			if (MultiFluids.recipeResultContains(recipe, BlazingMetals.GOLD.getFluidTag())) {
				award(BlazingAdvancements.MOLTEN_GOLD);
			}

			if (MultiFluids.recipeResultContains(recipe, BlazingMetals.BLAZE_GOLD.getFluidTag())) {
				award(BlazingAdvancements.MOLTEN_BLAZE_GOLD);
			}

			//noinspection ConstantValue
			if (Mth.abs(getSpeed()) >= AllConfigs.server().kinetics.maxRotationSpeed.get()
				&& hasFuel(MultiAmount.BUCKET.get())) {
				award(BlazingAdvancements.BLAZE_MIXER_MAX);
			}
		}
	}

	public abstract long getFuelAmount();

	public abstract boolean hasFuel(long amount);

	public abstract boolean hasFuel(TagKey<Fluid> tag, long amount);

	public abstract boolean hasFuel(SizedFluidIngredient fluidIngredient);

	public abstract void updateFueled();

	public abstract void renderFuelParticles();

	public abstract void renderParticles();

	public abstract List<ItemStack> getAvailableItems();

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

	public static boolean doInputsMatch(ProcessingRecipe<?, ?> a, ProcessingRecipe<?, ?> b) {
		return doItemInputsMatch(a, b) && doFluidInputsMatch(a, b);
	}

	public static boolean doItemInputsMatch(ProcessingRecipe<?, ?> a, ProcessingRecipe<?, ?> b) {
		if (a.getIngredients().isEmpty() && b.getIngredients().isEmpty()) return true;

		List<ItemStack[]> allItemsA = a.getIngredients().stream().map(Ingredient::getItems).toList();
		for (ItemStack[] matchingStacks : allItemsA) {
			boolean matched = false;
			if (matchingStacks.length == 0) return matched;

			matched = b.getIngredients().stream().anyMatch(i -> Arrays.stream(matchingStacks).allMatch(i));
			if (matched) continue;
			return false;
		}

		List<ItemStack[]> allItemsB = b.getIngredients().stream().map(Ingredient::getItems).toList();
		for (ItemStack[] matchingStacks : allItemsB) {
			boolean matched = false;
			if (matchingStacks.length == 0) return matched;

			matched = a.getIngredients().stream().anyMatch(i -> Arrays.stream(matchingStacks).allMatch(i));
			if (matched) continue;
			return false;
		}
		return true;
	}

	@ExpectPlatform
	public static boolean doFluidInputsMatch(ProcessingRecipe<?, ?> a, ProcessingRecipe<?, ?> b) {
		return true;
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

	public void registerAwardables(List<BlockEntityBehaviour> behaviours, BlazingAdvancement... advancements) {
		((IAdvancementBehaviour) this).blazinghot$registerAwardables(behaviours, advancements);
	}

	public void award(BlazingAdvancement advancement) {
		((IAdvancementBehaviour) this).blazinghot$award(advancement);
	}

	public void awardPlayerIfNear(BlazingAdvancement advancement, int maxDistance) {
		((IAdvancementBehaviour) this).blazinghot$award(advancement);
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Mode getMode() {
		return mode;
	}

	public enum Mode {
		BLAZE, INFERNO;
	}
}
