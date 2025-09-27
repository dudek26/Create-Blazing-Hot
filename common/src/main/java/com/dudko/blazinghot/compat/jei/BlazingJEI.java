package com.dudko.blazinghot.compat.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.ParametersAreNonnullByDefault;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.jei.category.BlazeMixingCategory;
import com.dudko.blazinghot.compat.jei.category.CastingCategory;
import com.dudko.blazinghot.content.casting.casting_depot.recipe.CastingRecipe;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingConfigs;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.RecipeGenericsUtil;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("SameParameterValue")
public abstract class BlazingJEI implements IModPlugin {

	protected static final ResourceLocation ID = BlazingHot.asResource("jei_plugin");

	protected final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

	public static IJeiRuntime runtime;

	@SuppressWarnings("unused")
	private void loadCategories() {
		allCategories.clear();

		CreateRecipeCategory<?>
				blazeMixing =
				builder(BasinRecipe.class)
						.addTypedRecipes(BlazingRecipeTypes.BLAZE_MIXING)
						.addTypedRecipesExcluding(AllRecipeTypes.MIXING::getType,
								BlazingRecipeTypes.BLAZE_MIXING::getType)
						.catalyst(BlazingBlocks.BLAZE_MIXER::get)
						.catalyst(AllBlocks.BASIN::get)
						.doubleItemIcon(BlazingBlocks.BLAZE_MIXER.get(), AllBlocks.BASIN.get())
						.emptyBackground(177, 103)
						.build("blaze_mixing", BlazeMixingCategory::standard),

				blazeAutoShapeless =
						builder(BasinRecipe.class)
								.enableWhen(BlazingConfigs.server().recipes.allowShapelessInBlazeMixer)
								.addAllRecipesIf(r -> r.value() instanceof CraftingRecipe
										&& !(r.value() instanceof ShapedRecipe)
										&& r.value().getIngredients().size() > 1
										&& !MechanicalPressBlockEntity.canCompress(r.value())
										&& !AllRecipeTypes.shouldIgnoreInAutomation(r), BasinRecipe::convertShapeless)
								.catalyst(BlazingBlocks.BLAZE_MIXER::get)
								.catalyst(AllBlocks.BASIN::get)
								.doubleItemIcon(BlazingBlocks.BLAZE_MIXER.get(), Items.CRAFTING_TABLE)
								.emptyBackground(177, 103)
								.build("blaze_automatic_shapeless", BlazeMixingCategory::autoShapeless),

				blazeBrewing =
						builder(BasinRecipe.class)
								.enableWhen(BlazingConfigs.server().recipes.allowBrewingInBlazeMixer)
								.addRecipes(() -> RecipeGenericsUtil.cast(PotionMixingRecipes.createRecipes(Minecraft.getInstance().level)))
								.catalyst(BlazingBlocks.BLAZE_MIXER::get)
								.catalyst(AllBlocks.BASIN::get)
								.doubleItemIcon(BlazingBlocks.BLAZE_MIXER.get(), Blocks.BREWING_STAND)
								.emptyBackground(177, 103)
								.build("blaze_automatic_brewing", BlazeMixingCategory::autoBrewing),

				casting =
						builder(CastingRecipe.class)
								.addTypedRecipes(BlazingRecipeTypes.CASTING)
								.catalyst(AllBlocks.SPOUT::get)
								.catalyst(BlazingBlocks.CASTING_DEPOT::get)
								.doubleItemIcon(AllBlocks.SPOUT.get(), BlazingBlocks.CASTING_DEPOT.get())
								.emptyBackground(177, 70)
								.build("spout_casting", CastingCategory::new);
	}

	protected <T extends Recipe<? extends RecipeInput>> CategoryBuilder<T> builder(Class<T> recipeClass) {
		return new CategoryBuilder<>(recipeClass);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		loadCategories();
		registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		allCategories.forEach(c -> c.registerCatalysts(registration));
	}

	protected class CategoryBuilder<T extends Recipe<?>> extends CreateRecipeCategory.Builder<T> {
		public CategoryBuilder(Class<? extends T> recipeClass) {
			super(recipeClass);
		}

		@Override
		public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
			return super.build(BlazingHot.asResource(name), factory);
		}

		public CreateRecipeCategory<T> build(ResourceLocation id, CreateRecipeCategory.Factory<T> factory) {
			CreateRecipeCategory<T> category = super.build(id, factory);
			allCategories.add(category);
			return category;
		}
	}

	public static void consumeAllRecipes(Consumer<? super RecipeHolder<?>> consumer) {
		assert Minecraft.getInstance().level != null;
		Minecraft.getInstance().getConnection().getRecipeManager().getRecipes().forEach(consumer);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void consumeTypedRecipes(Consumer<RecipeHolder<?>> consumer, RecipeType<?> type) {
		assert Minecraft.getInstance().getConnection() != null;
		List<? extends RecipeHolder<?>>
				map =
				Minecraft.getInstance().getConnection().getRecipeManager().getAllRecipesFor((RecipeType) type);
		if (!map.isEmpty()) map.forEach(consumer);
	}

	public static List<RecipeHolder<?>> getTypedRecipes(RecipeType<?> type) {
		List<RecipeHolder<?>> recipes = new ArrayList<>();
		consumeTypedRecipes(recipes::add, type);
		return recipes;
	}

	public static List<RecipeHolder<?>> getTypedRecipesExcluding(RecipeType<?> type, Predicate<RecipeHolder<?>> exclusionPred) {
		List<RecipeHolder<?>> recipes = getTypedRecipes(type);
		recipes.removeIf(exclusionPred);
		return recipes;
	}

	public static boolean doInputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
		if (recipe1.getIngredients().isEmpty() || recipe2.getIngredients().isEmpty()) {
			return false;
		}
		ItemStack[] matchingStacks = recipe1.getIngredients().getFirst().getItems();
		if (matchingStacks.length == 0) {
			return false;
		}
		return recipe2.getIngredients().getFirst().test(matchingStacks[0]);
	}

	public static boolean doOutputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
		assert Minecraft.getInstance().level != null;
		RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
		return ItemHelper.sameItem(recipe1.getResultItem(registryAccess), recipe2.getResultItem(registryAccess));
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime) {
		BlazingJEI.runtime = runtime;
	}
}
