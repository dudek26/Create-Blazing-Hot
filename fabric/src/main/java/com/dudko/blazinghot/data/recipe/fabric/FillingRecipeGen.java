package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.apple;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.carrot;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.melon;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.moltenGold;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
public class FillingRecipeGen extends BlazingProcessingRecipeGen {

	public FillingRecipeGen(PackOutput output) {
		super(output);
	}

	GeneratedRecipe
			GLISTERING_MELON =
			create("glistering_melon",
					b -> b
							.convertMeltable()
							.require(melon())
							.require(moltenGold(), NUGGET_COVER)
							.output(Items.GLISTERING_MELON_SLICE)),
			GOLDEN_APPLE =
					metalApple(MoltenMetals.GOLD, Items.GOLDEN_APPLE),
			GOLDEN_CARROT =
					metalCarrot(MoltenMetals.GOLD, Items.GOLDEN_CARROT),
			BLAZE_CARROT =
					metalCarrot(MoltenMetals.BLAZE_GOLD, BlazingItems.BLAZE_CARROT),
			BLAZE_APPLE =
					metalApple(MoltenMetals.BLAZE_GOLD, BlazingItems.BLAZE_APPLE),
			IRON_CARROT =
					metalCarrot(MoltenMetals.IRON, BlazingItems.IRON_CARROT),
			IRON_APPLE =
					metalApple(MoltenMetals.IRON, BlazingItems.IRON_APPLE),
			BRASS_CARROT =
					metalCarrot(MoltenMetals.BRASS, BlazingItems.BRASS_CARROT),
			BRASS_APPLE =
					metalApple(MoltenMetals.BRASS, BlazingItems.BRASS_APPLE),
			COPPER_CARROT =
					metalCarrot(MoltenMetals.COPPER, BlazingItems.COPPER_CARROT),
			COPPER_APPLE =
					metalApple(MoltenMetals.COPPER, BlazingItems.COPPER_APPLE),
			ZINC_CARROT =
					metalCarrot(MoltenMetals.ZINC, BlazingItems.ZINC_CARROT),
			ZINC_APPLE =
					metalApple(MoltenMetals.ZINC, BlazingItems.ZINC_APPLE);

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.FILLING;
	}

	private GeneratedRecipe metalApple(MoltenMetal metal, ItemLike result) {
		return create(result.asItem().toString(),
				b -> b.convertMeltable().require(apple()).require(metal.fluidTag(), INGOT_COVER).output(result));
	}

	private GeneratedRecipe metalCarrot(MoltenMetal metal, ItemLike result) {
		return create(result.asItem().toString(),
				b -> b.convertMeltable().require(carrot()).require(metal.fluidTag(), NUGGET_COVER).output(result));
	}
}
