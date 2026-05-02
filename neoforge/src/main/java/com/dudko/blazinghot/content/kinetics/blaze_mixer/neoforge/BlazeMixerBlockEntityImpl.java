package com.dudko.blazinghot.content.kinetics.blaze_mixer.neoforge;

import static com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe.getFuelCost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.recipe.BlazeMixingRecipe;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingBlockEntityTypes;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.item.SmartInventory;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;

public class BlazeMixerBlockEntityImpl extends BlazeMixerBlockEntity {

	public int fuelCost;

	protected BlazeMixerBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
			BlazingBlockEntityTypes.BLAZE_MIXER.get(),
			(be, context) -> {
				if (context != Direction.DOWN) return be.tank.getCapability();
				return null;
			});
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		tank = SmartFluidTankBehaviour.single(this, (int) MultiAmount.BUCKET.get());
		tank.whenFluidUpdates(() -> {
			if (getBasin().isPresent()) getBasin().get().notifyChangeOfContents();
		});
		behaviours.add(tank);

		super.addBehaviours(behaviours);
	}

	@NotNull
	public FluidStack getFluidStack() {
		return tank.getPrimaryHandler().getFluid();
	}

	public void updateFueled() {
		FluidState fluidState = getFluidStack().getFluid().defaultFluidState();

		fueled = fluidState.is(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag()) && getFuelAmount() > 0;
	}

	@Override
	public boolean hasFuel(long amount) {
		return hasFuel(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag(), amount);
	}

	@Override
	public boolean hasFuel(TagKey<Fluid> tag, long amount) {
		return amount <= 0 || hasFuel(SizedFluidIngredient.of(tag, (int) amount));
	}

	@Override
	public boolean hasFuel(SizedFluidIngredient fluidIngredient) {
		return fluidIngredient.ingredient().isEmpty() || fluidIngredient.test(getFluidStack());
	}

	public long getFuelAmount() {
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
					if (mode == Mode.BLAZE) {
						if (currentRecipe instanceof ProcessingRecipe<?, ? extends ProcessingRecipeParams> processingRecipe) {
							int t = processingRecipe.getProcessingDuration();
							if (t != 0) {
								recipeSpeed = t / 100f;
							}
						}
						if (currentRecipe instanceof BlazeMixingRecipe blazeMixingRecipe) {
							if (blazeMixingRecipe.getMixerFuel().test(getFluidStack())) {
								fuelCost = blazeMixingRecipe.getMixerFuelAmount();
							}
						} else {
							int calculatedCost = (int) getFuelCost(currentRecipe, level);
							if (hasFuel(calculatedCost)) {
								recipeSpeed = 1 / recipeSpeedMultiplier(currentRecipe);
								fuelCost = calculatedCost;
							}
						}
					}
					processingTicks =
						Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1, 8192);

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

				} else {
					processingTicks--;
					if (processingTicks == 0) {
						runningTicks++;
						processingTicks = -1;
						if (currentRecipe != null) updateAdvancements(currentRecipe);
						FluidStack updatedFuel = getFluidStack().copy();
						if (updatedFuel.getAmount() != 0) // forge: check if empty to avoid crash
							updatedFuel.shrink(Math.min(fuelCost, updatedFuel.getAmount()));
						tank.getPrimaryHandler().setFluid(updatedFuel);
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

		if (getFluidStack().getFluid().defaultFluidState().is(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag()))
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
	public List<ItemStack> getAvailableItems() {
		List<ItemStack> availableItems = new ArrayList<>();
		if (level == null) return availableItems;

		BasinBlockEntity basin = getBasin().orElse(null);
		if (basin == null) return availableItems;

		IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, basin.getBlockPos(), null);
		if (handler == null) return availableItems;
		for (int i = 0; i < handler.getSlots(); i++) {
			availableItems.add(handler.getStackInSlot(i));
		}
		return availableItems;
	}

	@Override
	protected List<Recipe<?>> getMatchingRecipes() {
		List<Recipe<?>> matchingRecipes = super.getMatchingRecipes();

		if (!BlazingConfigs.server().recipes.allowBrewingInBlazeMixer.get()) return matchingRecipes;

		Optional<BasinBlockEntity> basin = getBasin();
		if (basin.isEmpty()) return matchingRecipes;

		BasinBlockEntity basinBlockEntity = basin.get();

		IItemHandler
			availableItems =
			level.getCapability(Capabilities.ItemHandler.BLOCK, basinBlockEntity.getBlockPos(), null);
		if (availableItems == null) return matchingRecipes;

		for (int i = 0; i < availableItems.getSlots(); i++) {
			ItemStack stack = availableItems.getStackInSlot(i);
			if (stack.isEmpty()) continue;

			List<MixingRecipe> list = PotionMixingRecipes.sortRecipesByItem(level).get(stack.getItem());
			if (list == null) continue;
			for (MixingRecipe mixingRecipe : list)
				if (matchBasinRecipe(mixingRecipe)) matchingRecipes.add(mixingRecipe);
		}

		return matchingRecipes;
	}

	@Override
	protected <I extends RecipeInput> boolean matchBasinRecipe(Recipe<I> recipe) {
		if (recipe == null) return false;
		Optional<BasinBlockEntity> basin = getBasin();
		if (basin.isEmpty()) return false;

		if (recipe instanceof BlazeMixingRecipe bmxRecipe) {
			return BasinRecipe.match(basin.get(), bmxRecipe) && hasFuel(bmxRecipe.getMixerFuel());
		}

		return BasinRecipe.match(basin.get(), recipe);
	}

	public static boolean doFluidInputsMatch(ProcessingRecipe<?, ?> a, ProcessingRecipe<?, ?> b) {
		if (a.getFluidIngredients().isEmpty() && b.getFluidIngredients().isEmpty()) return true;

		List<FluidStack[]> allFluidsA = a.getFluidIngredients().stream().map(SizedFluidIngredient::getFluids).toList();
		for (FluidStack[] matchingStacks : allFluidsA) {
			boolean matched;
			if (matchingStacks.length == 0) return false;

			matched = b.getFluidIngredients().stream().anyMatch(i -> Arrays.stream(matchingStacks).allMatch(i::test));
			if (matched) continue;
			return false;
		}

		List<FluidStack[]> allFluidsB = b.getFluidIngredients().stream().map(SizedFluidIngredient::getFluids).toList();
		for (FluidStack[] matchingStacks : allFluidsB) {
			boolean matched;
			if (matchingStacks.length == 0) return false;

			matched = a.getFluidIngredients().stream().anyMatch(i -> Arrays.stream(matchingStacks).allMatch(i::test));
			if (matched) continue;
			return false;
		}
		return true;
	}

	@Override
	protected boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> holder) {
		Recipe<?> recipe = holder.value();
		if (mode == Mode.INFERNO) {
			return false;
		}

		return ((recipe instanceof CraftingRecipe
			&& !(recipe instanceof ShapedRecipe)
			&& BlazingConfigs.server().recipes.allowShapelessInBlazeMixer.get()
			&& recipe.getIngredients().size() > 1
			&& !MechanicalPressBlockEntity.canCompress(recipe)) && !AllRecipeTypes.shouldIgnoreInAutomation(holder)
			|| (recipe.getType() == AllRecipeTypes.MIXING.getType()
			&& BlazingConfigs.server().recipes.allowMixingInBlazeMixer.get()
			&& BlazingRecipeTypes.shouldAllowBlazeMixing(holder)))
			|| recipe.getType() == BlazingRecipeTypes.BLAZE_MIXING.getType();
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		boolean kinetics = !tooltip.isEmpty();
		if (kinetics) tooltip.add(Component.empty());
		boolean
			fluids =
			containedFluidTooltip(tooltip,
				isPlayerSneaking,
				level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null));

		return kinetics || fluids;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void tickAudio() {
		super.tickAudio();

		boolean slow = Math.abs(getSpeed()) < 65;
		if (slow && AnimationTickHolder.getTicks() % 2 == 0) return;
		if (runningTicks == 20) AllSoundEvents.MIXING.playAt(level, worldPosition, .75f, 1, true);
	}

	public static BlazeMixerBlockEntity of(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new BlazeMixerBlockEntityImpl(type, pos, state);
	}
}
