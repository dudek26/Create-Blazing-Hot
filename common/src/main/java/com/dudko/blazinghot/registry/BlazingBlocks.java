package com.dudko.blazinghot.registry;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.AbstractModernLampPanel;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampDoublePanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampHalfPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampQuadPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.SmallModernLampPanelBlock;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlock;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.dudko.blazinghot.multiloader.BlazingBuilderTransformers;
import com.dudko.blazinghot.util.DyeUtil;
import com.dudko.blazinghot.util.LangUtil;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class BlazingBlocks {

	private static final CreateRegistrate REGISTRATE = BlazingHot.registrate();

	// MACHINES

	static {
		BlazingCreativeTabs.useBaseTab();
	}

	public static final BlockEntry<BlazeMixerBlock>
			BLAZE_MIXER =
			REGISTRATE
					.block("blaze_mixer", BlazeMixerBlock::new)
					.initialProperties(SharedProperties::stone)
					.properties(p -> p.noOcclusion().mapColor(MapColor.STONE))
					.transform(axeOrPickaxe())
					.blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
					.addLayer(() -> RenderType::cutoutMipped)
					.transform(BlockStressDefaults.setImpact(4.0))
					.item(AssemblyOperatorBlockItem::new)
					.onRegisterAfter(Registries.ITEM,
							v -> ItemDescription.useKey(v, ItemDescriptions.BLAZE_MIXER.getKey()))
					.transform(customItemModel())
					.register();

	public static final BlockEntry<CasingBlock>
			BLAZE_CASING =
			REGISTRATE
					.block("blaze_casing", CasingBlock::new)
					.transform(BuilderTransformers.casing(() -> BlazingSpriteShifts.BLAZE_CASING))
					.properties(p -> p.mapColor(MapColor.CRIMSON_NYLIUM).sound(SoundType.NETHER_WOOD))
					.register();

	// BUILDING BLOCKS

	static {
		BlazingCreativeTabs.useBuildingTab();
	}

	public static final BlockEntry<Block>
			BLAZE_GOLD_BLOCK =
			REGISTRATE
					.block("blaze_gold_block", Block::new)
					.initialProperties(() -> net.minecraft.world.level.block.Blocks.GOLD_BLOCK)
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.tag(BlockTags.NEEDS_IRON_TOOL)
					.tag(CommonTags.Blocks.STORAGE_BLOCKS.bothTags())
					.tag(BlockTags.BEACON_BASE_BLOCKS)
					.tag(CommonTags.Blocks.BLAZE_GOLD_BLOCKS.bothTags())
					.item()
					.tag(CommonTags.Items.STORAGE_BLOCKS.bothTags())
					.tag(CommonTags.Items.BLAZE_GOLD_BLOCKS.bothTags())
					.build()
					.register();


	public static final DyedBlockList<ModernLampBlock> MODERN_LAMP_BLOCKS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();
		return REGISTRATE
				.block(colorName + "_modern_lamp", p -> new ModernLampBlock(p, color))
				.transform(BlazingBuilderTransformers.anyModernLamp(color))
				.transform(BlazingBuilderTransformers.modernLampBlock(color))
				.recipe((c, p) -> {
					ShapedRecipeBuilder
							.shaped(RecipeCategory.REDSTONE, c.get(), 2)
							.pattern(" g ")
							.pattern("glg")
							.pattern(" r ")
							.define('g', DyeUtil.getStainedGlass(color))
							.define('l', Blocks.GLOWSTONE)
							.define('r', BlazingItems.BLAZE_GOLD_ROD)
							.unlockedBy("has_" + BlazingItems.BLAZE_GOLD_ROD.getId().getPath(),
									RegistrateRecipeProvider.has(BlazingItems.BLAZE_GOLD_ROD))
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/"
											+ c.getName()
											+ "_from_stained_glass"));
					modernLampDyeing(c, p, BlazingTags.Items.MODERN_LAMPS.tag, color, "block");
				})
				.register();
	});

	public static final DyedBlockList<ModernLampPanelBlock> MODERN_LAMP_PANELS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();
		return REGISTRATE
				.block(colorName + "_modern_lamp_panel", p -> new ModernLampPanelBlock(p, color))
				.transform(BlazingBuilderTransformers.anyModernLamp(color))
				.transform(BlazingBuilderTransformers.modernLampPanel(color, "modern_lamp/panel"))
				.recipe((c, p) -> {
					ShapedRecipeBuilder
							.shaped(RecipeCategory.REDSTONE, c.get(), 4)
							.pattern("ll")
							.define('l', MODERN_LAMP_BLOCKS.get(color))
							.unlockedBy("has_modern_lamps",
									RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMP_PANELS.tag))
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/panel/"
											+ c.getName()
											+ "_from_full_block"));
					panelStoneCuttingRecipe(c, p, color);
					lampStoneCuttingRecipe(c, p, color, 2);
					modernLampDyeing(c, p, BlazingTags.Items.MODERN_LAMP_PANELS.tag, color, "panel");
				})
				.register();
	});

	public static final DyedBlockList<ModernLampDoublePanelBlock>
			MODERN_LAMP_DOUBLE_PANELS =
			new DyedBlockList<>(color -> {
				String colorName = color.getSerializedName();
				return REGISTRATE
						.block(colorName + "_modern_lamp_double_panel", p -> new ModernLampDoublePanelBlock(p, color))
						.transform(BlazingBuilderTransformers.anyModernLamp(color))
						.transform(BlazingBuilderTransformers.modernLampDirectionalPanel(color,
								"modern_lamp/double_panel"))
						.recipe((c, p) -> {
							panelStoneCuttingRecipe(c, p, color);
							lampStoneCuttingRecipe(c, p, color, 2);
							modernLampDyeing(c,
									p,
									BlazingTags.Items.MODERN_LAMP_DOUBLE_PANELS.tag,
									color,
									"double_panel");
						})
						.register();
			});

	public static final DyedBlockList<ModernLampQuadPanelBlock> MODERN_LAMP_QUAD_PANELS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();
		return REGISTRATE
				.block(colorName + "_modern_lamp_quad_panel", p -> new ModernLampQuadPanelBlock(p, color))
				.transform(BlazingBuilderTransformers.anyModernLamp(color))
				.transform(BlazingBuilderTransformers.modernLampPanel(color, "modern_lamp/quad_panel"))
				.recipe((c, p) -> {
					panelStoneCuttingRecipe(c, p, color);
					modernLampDyeing(c, p, BlazingTags.Items.MODERN_LAMP_QUAD_PANELS.tag, color, "quad_panel");
					lampStoneCuttingRecipe(c, p, color, 2);
				})
				.register();
	});

	public static final DyedBlockList<ModernLampHalfPanelBlock> MODERN_LAMP_HALF_PANELS = new DyedBlockList<>(color -> {
		String colorName = color.getSerializedName();
		return REGISTRATE
				.block(colorName + "_modern_lamp_half_panel", p -> new ModernLampHalfPanelBlock(p, color))
				.transform(BlazingBuilderTransformers.anyModernLamp(color))
				.transform(BlazingBuilderTransformers.modernLampHalfPanel(color, "modern_lamp/half_panel"))
				.recipe((c, p) -> {
					ShapedRecipeBuilder
							.shaped(RecipeCategory.REDSTONE, c.get(), 4)
							.pattern("ll")
							.define('l', MODERN_LAMP_PANELS.get(color))
							.unlockedBy("has_modern_lamps",
									RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/half_panel/"
											+ c.getName()
											+ "_from_panel"));
					BlockEntry<ModernLampDoublePanelBlock>
							doublePanel =
							BlazingBlocks.MODERN_LAMP_DOUBLE_PANELS.get(color);
					ShapelessRecipeBuilder
							.shapeless(RecipeCategory.REDSTONE, doublePanel)
							.requires(c.get(), 2)
							.unlockedBy("has_modern_lamps",
									RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/panel/"
											+ doublePanel.getId().getPath()
											+ "_from_half_panels"));
					lampStoneCuttingRecipe(c, p, color, 2);
					panelStoneCuttingRecipe(c, p, color, c.getName() + "_from_panel", 2);
					modernLampDyeing(c, p, BlazingTags.Items.MODERN_LAMP_HALF_PANELS.tag, color, "half_panel");
				})
				.register();
	});

	public static final DyedBlockList<SmallModernLampPanelBlock>
			MODERN_LAMP_SMALL_PANELS =
			new DyedBlockList<>(color -> {
				String colorName = color.getSerializedName();
				return REGISTRATE
						.block(colorName + "_modern_lamp_small_panel", p -> new SmallModernLampPanelBlock(p, color))
						.lang(LangUtil.titleCaseConversion(colorName.replace('_', ' ')) + " Small Modern Lamp Panel")
						.transform(BlazingBuilderTransformers.anyModernLamp(color))
						.transform(BlazingBuilderTransformers.modernLampSmallPanel(color, "modern_lamp/small_panel"))
						.recipe((c, p) -> {
							BlockEntry<ModernLampQuadPanelBlock>
									quadPanel =
									BlazingBlocks.MODERN_LAMP_QUAD_PANELS.get(color);
							ShapelessRecipeBuilder
									.shapeless(RecipeCategory.REDSTONE, quadPanel)
									.requires(c.get(), 4)
									.unlockedBy("has_modern_lamps",
											RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
									.save(p,
											BlazingHot.asResource("crafting/modern_lamp/panel/" + quadPanel
													.getId()
													.getPath() + "_from_small_panels"));
							lampStoneCuttingRecipe(c, p, color, 4);
							modernLampDyeing(c,
									p,
									BlazingTags.Items.MODERN_LAMP_SMALL_PANELS.tag,
									color,
									"small_panel");
							panelStoneCuttingRecipe(c, p, color, c.getName() + "_from_panel", 4);
						})
						.register();
			});

	public static void register() {
	}

	private static ItemLike[] lampPanels(DyeColor color) {
		List<ItemLike> items = new ArrayList<>();
		List<DyedBlockList<?>>
				panelLists =
				List.of(MODERN_LAMP_PANELS, MODERN_LAMP_DOUBLE_PANELS, MODERN_LAMP_QUAD_PANELS);
		panelLists.forEach(list -> items.add(list.get(color)));
		return items.toArray(new ItemLike[]{});
	}

	private static <T extends AbstractModernLampPanel> void panelStoneCuttingRecipe(DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov, DyeColor color) {
		panelStoneCuttingRecipe(ctx, prov, color, ctx.getName(), 1);
	}

	private static <T extends AbstractModernLampPanel> void panelStoneCuttingRecipe(DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov, DyeColor color, String name, int count) {
		SingleItemRecipeBuilder
				.stonecutting(Ingredient.of(lampPanels(color)), RecipeCategory.REDSTONE, ctx.get(), count)
				.unlockedBy("has_modern_lamps", RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
				.save(prov, BlazingHot.asResource("stonecutting/modern_lamp_panel/" + name));
	}

	private static <T extends ModernLampBlock> void lampStoneCuttingRecipe(DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov, DyeColor color, int count) {
		SingleItemRecipeBuilder
				.stonecutting(Ingredient.of(MODERN_LAMP_BLOCKS.get(color)), RecipeCategory.REDSTONE, ctx.get(), count)
				.unlockedBy("has_modern_lamps", RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
				.save(prov, BlazingHot.asResource("stonecutting/modern_lamp/" + ctx.getName() + "_from_block"));
	}

	private static <T extends ModernLampBlock> void modernLampDyeing(DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov, TagKey<Item> tag, DyeColor color, String name) {
		DyeUtil
				.dyeingMultiple(RecipeCategory.REDSTONE, tag, ctx.get(), color)
				.save(prov,
						BlazingHot.asResource("crafting/modern_lamp/"
								+ name
								+ "/"
								+ ctx.getName()
								+ "_from_other_lamps"));
		DyeUtil
				.dyeingSingle(RecipeCategory.REDSTONE, tag, ctx.get(), color)
				.save(prov,
						BlazingHot.asResource("crafting/modern_lamp/"
								+ name
								+ "/"
								+ ctx.getName()
								+ "_from_other_lamp"));
	}


}
