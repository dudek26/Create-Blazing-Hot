package com.dudko.blazinghot.registry;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampDoublePanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampQuadPanelBlock;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlock;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.dudko.blazinghot.multiloader.BlazingBuilderTransformers;
import com.dudko.blazinghot.util.DyeUtil;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
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
					DyeUtil
							.dyeingMultiple(RecipeCategory.REDSTONE, BlazingTags.Items.MODERN_LAMPS.tag, c.get(), color)
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/" + c.getName() + "_from_other_lamps"));
					DyeUtil
							.dyeingSingle(RecipeCategory.REDSTONE, BlazingTags.Items.MODERN_LAMPS.tag, c.get(), color)
							.save(p, BlazingHot.asResource("crafting/modern_lamp/" + c.getName() + "_from_other_lamp"));
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
					DyeUtil
							.dyeingMultiple(RecipeCategory.REDSTONE,
									BlazingTags.Items.MODERN_LAMP_PANELS.tag,
									c.get(),
									color)
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/panel/"
											+ c.getName()
											+ "_from_other_lamps"));
					DyeUtil
							.dyeingSingle(RecipeCategory.REDSTONE,
									BlazingTags.Items.MODERN_LAMP_PANELS.tag,
									c.get(),
									color)
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/panel/"
											+ c.getName()
											+ "_from_other_lamp"));
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
							ShapedRecipeBuilder
									.shaped(RecipeCategory.REDSTONE, c.get(), 2)
									.pattern("ll")
									.define('l', MODERN_LAMP_PANELS.get(color))
									.unlockedBy("has_modern_lamps",
											RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
									.save(p,
											BlazingHot.asResource("crafting/modern_lamp/double_panel/"
													+ c.getName()
													+ "_from_panel"));
							DyeUtil
									.dyeingMultiple(RecipeCategory.REDSTONE,
											BlazingTags.Items.MODERN_LAMP_DOUBLE_PANELS.tag,
											c.get(),
											color)
									.save(p,
											BlazingHot.asResource("crafting/modern_lamp/double_panel/"
													+ c.getName()
													+ "_from_other_lamps"));
							DyeUtil
									.dyeingSingle(RecipeCategory.REDSTONE,
											BlazingTags.Items.MODERN_LAMP_DOUBLE_PANELS.tag,
											c.get(),
											color)
									.save(p,
											BlazingHot.asResource("crafting/modern_lamp/double_panel/"
													+ c.getName()
													+ "_from_other_lamp"));
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
					ShapedRecipeBuilder
							.shaped(RecipeCategory.REDSTONE, c.get(), 4)
							.pattern("ll")
							.pattern("ll")
							.define('l', MODERN_LAMP_PANELS.get(color))
							.unlockedBy("has_modern_lamps",
									RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMPS.tag))
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/quad_panel/"
											+ c.getName()
											+ "_from_panel"));
					DyeUtil
							.dyeingMultiple(RecipeCategory.REDSTONE,
									BlazingTags.Items.MODERN_LAMP_QUAD_PANELS.tag,
									c.get(),
									color)
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/quad_panel/"
											+ c.getName()
											+ "_from_other_lamps"));
					DyeUtil
							.dyeingSingle(RecipeCategory.REDSTONE,
									BlazingTags.Items.MODERN_LAMP_QUAD_PANELS.tag,
									c.get(),
									color)
							.save(p,
									BlazingHot.asResource("crafting/modern_lamp/quad_panel/"
											+ c.getName()
											+ "_from_other_lamp"));
				})
				.register();
	});


	public static void register() {
	}

}
