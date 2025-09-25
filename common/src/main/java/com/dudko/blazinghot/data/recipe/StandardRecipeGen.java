package com.dudko.blazinghot.data.recipe;

import static com.dudko.blazinghot.data.recipe.BlazingIngredients.apple;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.blazeGoldRod;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.blazeGoldSheet;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.brassIngot;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.carrot;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.coal;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.electronTube;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.feather;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.glowstone;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.sturdyAlloy;
import static com.dudko.blazinghot.data.recipe.BlazingIngredients.sturdyCasing;
import static com.dudko.blazinghot.registry.BlazingBlocks.BLAZE_GOLD_BLOCK;
import static com.dudko.blazinghot.registry.BlazingForms.INGOT;
import static com.dudko.blazinghot.registry.BlazingForms.NUGGET;
import static com.dudko.blazinghot.registry.BlazingItems.BLAZE_GOLD_INGOT;
import static com.dudko.blazinghot.registry.BlazingItems.BLAZE_GOLD_NUGGET;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.BlazingMetal;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.dudko.blazinghot.util.ItemUtil;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemProviderEntry;

import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "unused"})
public class StandardRecipeGen extends BaseRecipeProvider {

	public StandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BlazingHot.ID);
	}

	GeneratedRecipe BLAZE_GOLD_INGOT_C = compressing(BLAZE_GOLD_INGOT, BLAZE_GOLD_BLOCK, BLAZE_GOLD_INGOT),
			BLAZE_GOLD_NUGGET_C =
					compressing(BLAZE_GOLD_NUGGET, BLAZE_GOLD_INGOT, BLAZE_GOLD_INGOT);

	GeneratedRecipe BLAZE_GOLD_BLOCK_D = decompressing(BLAZE_GOLD_BLOCK, BLAZE_GOLD_INGOT, 9, BLAZE_GOLD_INGOT),
			BLAZE_GOLD_INGOT_D =
					decompressing(BLAZE_GOLD_INGOT, BLAZE_GOLD_NUGGET, 9, BLAZE_GOLD_INGOT);

	GeneratedRecipe IRON_APPLE = metalApple(BlazingMetals.IRON, BlazingItems.IRON_APPLE),
			IRON_CARROT =
					metalCarrot(BlazingMetals.IRON, BlazingItems.IRON_CARROT),
			BLAZE_APPLE =
					metalApple(BlazingMetals.BLAZE_GOLD, BlazingItems.BLAZE_APPLE),
			BLAZE_CARROT =
					metalCarrot(BlazingMetals.BLAZE_GOLD, BlazingItems.BLAZE_CARROT),
			BRASS_APPLE =
					metalApple(BlazingMetals.BRASS, BlazingItems.BRASS_APPLE),
			BRASS_CARROT =
					metalCarrot(BlazingMetals.BRASS, BlazingItems.BRASS_CARROT),
			ZINC_APPLE =
					metalApple(BlazingMetals.ZINC, BlazingItems.ZINC_APPLE),
			ZINC_CARROT =
					metalCarrot(BlazingMetals.ZINC, BlazingItems.ZINC_CARROT),
			COPPER_APPLE =
					metalApple(BlazingMetals.COPPER, BlazingItems.COPPER_APPLE),
			COPPER_CARROT =
					metalCarrot(BlazingMetals.COPPER, BlazingItems.COPPER_CARROT);

	GeneratedRecipe
			WHITE_MODERN_LAMP =
			create(BlazingBlocks.MODERN_LAMP_BLOCKS.get(DyeColor.WHITE))
					.unlockedByTag(BlazingIngredients::blazeGoldRod)
					.returns(2)
					.viaShaped(b -> b
							.define('X', blazeGoldRod())
							.define('Y', glowstone())
							.define('G', Items.GLASS)
							.pattern(" G ")
							.pattern("GYG")
							.pattern(" X "));
	GeneratedRecipe
			BLAZE_ARROW =
			create(BlazingItems.BLAZE_ARROW)
					.unlockedByTag(BlazingIngredients::blazeGoldRod)
					.returns(4)
					.viaShaped(b -> b
							.define('X', coal())
							.define('Y', blazeGoldRod())
							.define('Z', feather())
							.pattern(" X ")
							.pattern(" Y ")
							.pattern(" Z "));
	GeneratedRecipe
			BLAZE_WHISK =
			create(BlazingItems.BLAZE_WHISK)
					.unlockedByTag(BlazingIngredients::blazeGoldIngot)
					.viaShaped(b -> b
							.define('X', brassIngot())
							.define('Y', blazeGoldSheet())
							.pattern(" X ")
							.pattern("YXY")
							.pattern("YYY"));

	GeneratedRecipe
			CASTING_DEPOT =
			create(BlazingBlocks.CASTING_DEPOT)
					.unlockedBy(BlazingIngredients::sturdyCasing)
					.viaShaped(b -> b
							.define('C', sturdyCasing())
							.define('A', sturdyAlloy())
							.define('E', electronTube())
							.pattern("A")
							.pattern("C")
							.pattern("E"));

	//

	private GeneratedRecipe compressing(ItemLike ingredient, ItemLike result, ItemLike unlockedBy) {
		return create(() -> result)
				.unlockedBy(() -> unlockedBy)
				.withSuffix("_from_" + ItemUtil.getItemID(ingredient).getPath())
				.viaShaped(b -> b.define('X', ingredient).pattern("XXX").pattern("XXX").pattern("XXX"));
	}

	private GeneratedRecipe decompressing(ItemLike ingredient, ItemLike result, int count, ItemLike unlockedBy) {
		return create(() -> result)
				.returns(count)
				.unlockedBy(() -> unlockedBy)
				.withSuffix("_from_" + ItemUtil.getItemID(ingredient).getPath())
				.viaShapeless(b -> b.requires(ingredient));
	}

	private GeneratedRecipe covering(ItemLike ingredient, TagKey<Item> cover, ItemLike result, TagKey<Item> unlockedBy) {
		return create(() -> result)
				.unlockedByTag(() -> unlockedBy)
				.viaShaped(b -> b
						.define('Y', ingredient)
						.define('X', cover)
						.pattern("XXX")
						.pattern("XYX")
						.pattern("XXX"));
	}

	private GeneratedRecipe metalApple(BlazingMetal metal, ItemLike result) {
		return covering(apple(), INGOT.getItemTag(metal), result, INGOT.getItemTag(metal));
	}

	private GeneratedRecipe metalCarrot(BlazingMetal metal, ItemLike result) {
		return covering(carrot(), NUGGET.getItemTag(metal), result, INGOT.getItemTag(metal));
	}

	GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
		return new GeneratedRecipeBuilder("/", result);
	}

	GeneratedRecipeBuilder create(ResourceLocation result) {
		return new GeneratedRecipeBuilder("/", result);
	}

	GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike, ?> result) {
		return create(result::get);
	}

	class GeneratedRecipeBuilder {

		private final String path;
		private String suffix;
		private Supplier<? extends ItemLike> result;
		private ResourceLocation compatDatagenOutput;

		private Supplier<ItemPredicate> unlockedBy;
		private int amount;

		private GeneratedRecipeBuilder(String path) {
			this.path = path;
			this.suffix = "";
			this.amount = 1;
		}

		public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
			this(path);
			this.result = result;
		}

		public GeneratedRecipeBuilder(String path, ResourceLocation result) {
			this(path);
			this.compatDatagenOutput = result;
		}

		GeneratedRecipeBuilder returns(int amount) {
			this.amount = amount;
			return this;
		}

		GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
			this.unlockedBy = () -> ItemPredicate.Builder.item().of(item.get()).build();
			return this;
		}

		GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
			this.unlockedBy = () -> ItemPredicate.Builder.item().of(tag.get()).build();
			return this;
		}

		GeneratedRecipeBuilder withSuffix(String suffix) {
			this.suffix = suffix;
			return this;
		}

		GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
			return register(consumer -> {
				ShapedRecipeBuilder
						b =
						builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
				if (unlockedBy != null)
					b.unlockedBy("has_item", RegistrateRecipeProvider.inventoryTrigger(unlockedBy.get()));
				b.save(consumer, createLocation("crafting"));
			});
		}

		GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
			return register(consumer -> {
				ShapelessRecipeBuilder
						b =
						builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
				if (unlockedBy != null)
					b.unlockedBy("has_item", RegistrateRecipeProvider.inventoryTrigger(unlockedBy.get()));
				b.save(consumer, createLocation("crafting"));
			});
		}

		private static ResourceLocation clean(ResourceLocation loc) {
			String path = loc.getPath();
			while (path.contains("//")) path = path.replaceAll("//", "/");
			return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), path);
		}

		private ResourceLocation createSimpleLocation(String recipeType) {
			return clean(BlazingHot.asResource(recipeType + "/" + getRegistryName().getPath() + suffix));
		}

		private ResourceLocation createLocation(String recipeType) {
			return clean(BlazingHot.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix));
		}

		private ResourceLocation getRegistryName() {
			return compatDatagenOutput == null ?
				   RegisteredObjectsHelper.getKeyOrThrow(result.get().asItem()) :
				   compatDatagenOutput;
		}

	}
}
