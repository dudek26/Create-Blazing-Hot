package com.dudko.blazinghot.registry;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampGenerator;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelGenerator;
import com.dudko.blazinghot.util.DyeUtil;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.dudko.blazinghot.BlazingHot.REGISTRATE;

public class BlazingBlocks {

    public static final BlockEntry<Block> BLAZE_GOLD_BLOCK = REGISTRATE
            .block("blaze_gold_block", Block::new)
            .initialProperties(() -> net.minecraft.world.level.block.Blocks.GOLD_BLOCK)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .tag(BlazingTags.Blocks.BLAZE_GOLD_BLOCKS.tag)
            .item()
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();

    public static final BlockEntry<CasingBlock> BLAZE_CASING = REGISTRATE
            .block("blaze_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> BlazingSpriteShifts.BLAZE_CASING))
            .properties(p -> p.mapColor(MapColor.CRIMSON_NYLIUM).sound(SoundType.NETHER_WOOD))
            .register();

    public static final DyedBlockList<ModernLampBlock> MODERN_LAMP_BLOCKS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return REGISTRATE
                .block(colorName + "_modern_lamp", p -> new ModernLampBlock(p, color))
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.GLOWSTONE)
                .properties(p -> p.mapColor(color).lightLevel(s -> s.getValue(ModernLampBlock.LIT) ? 15 : 0))
                .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag, BlazingTags.Blocks.MODERN_LAMPS.tag)
                .blockstate(new ModernLampGenerator(color)::generate)
                .recipe((c, p) -> {
                    DyeUtil
                            .dyeingMultiple(RecipeCategory.REDSTONE, BlazingTags.Items.MODERN_LAMPS.tag, c.get(), color)
                            .save(p,
                                  BlazingHot.asResource("crafting/modern_lamp/" + c.getName() + "_from_other_lamps"));
                    DyeUtil
                            .dyeingSingle(RecipeCategory.REDSTONE, BlazingTags.Items.MODERN_LAMPS.tag, c.get(), color)
                            .save(p, BlazingHot.asResource("crafting/modern_lamp/" + c.getName() + "_from_other_lamp"));
                })
                .item()
                .tag(BlazingTags.Items.MODERN_LAMPS.tag)
                .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.modern_lamp"))
                .model((c, b) -> b.blockItem(c).texture("#all", b.modLoc("block/modern_lamp/" + colorName)))
                .build()
                .register();
    });

    public static final DyedBlockList<ModernLampBlock> MODERN_LAMP_PANELS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return REGISTRATE
                .block(colorName + "_modern_lamp_panel", p -> new ModernLampPanelBlock(p, color))
                .initialProperties(() -> net.minecraft.world.level.block.Blocks.GLOWSTONE)
                .properties(p -> p.mapColor(color).lightLevel(s -> s.getValue(ModernLampPanelBlock.LIT) ? 15 : 0))
                .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag, BlazingTags.Blocks.MODERN_LAMP_PANELS.tag)
                .blockstate(new ModernLampPanelGenerator(color)::generate)
                .recipe((c, p) -> {
                    ShapedRecipeBuilder
                            .shaped(RecipeCategory.REDSTONE, c.get(), 4)
                            .pattern("ll")
                            .define('l', MODERN_LAMP_BLOCKS.get(color))
                            .unlockedBy("has_modern_lamps",
                                        RegistrateRecipeProvider.has(BlazingTags.Items.MODERN_LAMP_PANELS.tag))
                            .save(p, BlazingHot.asResource(
                                    "crafting/modern_lamp_panel/" + c.getName() + "_from_full_block"));
                    DyeUtil
                            .dyeingMultiple(RecipeCategory.REDSTONE, BlazingTags.Items.MODERN_LAMP_PANELS.tag, c.get(),
                                            color)
                            .save(p, BlazingHot.asResource(
                                    "crafting/modern_lamp_panel/" + c.getName() + "_from_other_lamps"));
                    DyeUtil
                            .dyeingSingle(RecipeCategory.REDSTONE, BlazingTags.Items.MODERN_LAMP_PANELS.tag, c.get(),
                                          color)
                            .save(p, BlazingHot.asResource(
                                    "crafting/modern_lamp_panel/" + c.getName() + "_from_other_lamp"));
                })
                .item()
                .tag(BlazingTags.Items.MODERN_LAMP_PANELS.tag)
                .onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "item.blazinghot.modern_lamp"))
                .transform(ModelGen.customItemModel("modern_lamp_panel", colorName))
                .register();
    });

    public static void register() {
    }
}
