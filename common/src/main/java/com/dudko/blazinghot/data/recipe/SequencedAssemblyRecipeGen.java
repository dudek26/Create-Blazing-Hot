package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.blazeCasing;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.blazeWhisk;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.cogwheel;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.diamond;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.extensionPole;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.moltenNetherite;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.netheriteAppleIngredients;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stellarBlazeApple;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stellarBrassApple;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stellarCopperApple;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stellarGoldenApple;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stellarIronApple;
import static com.dudko.blazinghot.foundation.recipe.BlazingIngredients.stellarZincApple;
import static com.dudko.blazinghot.registry.BlazingItems.ANCIENT_ENCHANTED_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.BRASSY_STELLAR_BRASS_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.BURNING_STELLAR_BLAZE_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.COATED_STELLAR_COPPER_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.GALVANIZED_STELLAR_ZINC_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.GILDED_STELLAR_GOLDEN_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.HEAVY_STELLAR_IRON_APPLE;
import static com.dudko.blazinghot.registry.BlazingItems.INCOMPLETE_BLAZE_MIXER;

import java.util.concurrent.CompletableFuture;

import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.foundation.recipe.BlazingSequencedAssemblyRecipeGen;
import com.dudko.blazinghot.foundation.recipe.BlazingStandardRecipeBuilder;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.util.ItemUtil;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class SequencedAssemblyRecipeGen extends BlazingSequencedAssemblyRecipeGen {

	public SequencedAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	GeneratedRecipe
			ENCHANTED_GOLDEN_APPLE =
			enchantedMetalApple(BlazingMetals.GOLD,
					stellarGoldenApple(),
					GILDED_STELLAR_GOLDEN_APPLE,
					Items.ENCHANTED_GOLDEN_APPLE),

	ENCHANTED_BLAZE_APPLE =
			enchantedMetalApple(BlazingMetals.BLAZE_GOLD,
					stellarBlazeApple(),
					BURNING_STELLAR_BLAZE_APPLE,
					BlazingItems.ENCHANTED_BLAZE_APPLE),

	ENCHANTED_IRON_APPLE =
			enchantedMetalApple(BlazingMetals.IRON,
					stellarIronApple(),
					HEAVY_STELLAR_IRON_APPLE,
					BlazingItems.ENCHANTED_IRON_APPLE),

	ENCHANTED_BRASS_APPLE =
			enchantedMetalApple(BlazingMetals.BRASS,
					stellarBrassApple(),
					BRASSY_STELLAR_BRASS_APPLE,
					BlazingItems.ENCHANTED_BRASS_APPLE),

	ENCHANTED_ZINC_APPLE =
			enchantedMetalApple(BlazingMetals.ZINC,
					stellarZincApple(),
					GALVANIZED_STELLAR_ZINC_APPLE,
					BlazingItems.ENCHANTED_ZINC_APPLE),

	ENCHANTED_COPPER_APPLE =
			enchantedMetalApple(BlazingMetals.COPPER,
					stellarCopperApple(),
					COATED_STELLAR_COPPER_APPLE,
					BlazingItems.ENCHANTED_COPPER_APPLE),

	ENCHANTED_NETHERITE_APPLE =
			bCreate("enchanted_netherite_apple",
					b -> b
							.require(netheriteAppleIngredients())
							.transitionTo(ANCIENT_ENCHANTED_APPLE)
							.addOutput(BlazingItems.ENCHANTED_NETHERITE_APPLE, 1)
							.loops(3)
							.addBlazingStep(id -> new BlazingStandardRecipeBuilder<>(FillingRecipe::new, id),
									r -> r.require(moltenNetherite(), MultiAmount.INGOT_COVER))
							.addStep(PressingRecipe::new, r -> r));

	GeneratedRecipe
			BLAZE_MIXER =
			create("blaze_mixer",
					b -> b
							.require(blazeCasing())
							.transitionTo(INCOMPLETE_BLAZE_MIXER)
							.addOutput(BlazingBlocks.BLAZE_MIXER, 1)
							.loops(1)
							.addStep(PressingRecipe::new, r -> r)
							.addStep(DeployerApplicationRecipe::new, r -> r.require(blazeWhisk()))
							.addStep(DeployerApplicationRecipe::new, r -> r.require(cogwheel()))
							.addStep(DeployerApplicationRecipe::new, r -> r.require(extensionPole())));

	private GeneratedRecipe enchantedMetalApple(BlazingMetal metal, ItemLike input, ItemLike transition, ItemLike output) {
		return bCreate(ItemUtil.getItemID(output).getPath(),
				b -> b
						.require(input)
						.transitionTo(transition)
						.addOutput(output, 1)
						.loops(6)
						.addBlazingStep(id -> new BlazingStandardRecipeBuilder<>(FillingRecipe::new, id),
								r -> r.require(metal.getFluidTag(), MultiAmount.INGOT_COVER))
						.addStep(DeployerApplicationRecipe::new, r -> r.require(diamond()))
						.addStep(PressingRecipe::new, r -> r));
	}

}
