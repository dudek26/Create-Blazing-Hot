package com.dudko.blazinghot.multiloader.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampDoublePanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampHalfPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.ModernLampPanelBlock;
import com.dudko.blazinghot.content.block.modern_lamp.SmallModernLampPanelBlock;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.data.lang.ItemDescriptions;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

public class BlazingBuilderTransformersImpl {
	public static <B extends ModernLampBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampBlock(DyeColor color) {
		return a -> a
				.initialProperties(() -> Blocks.GLOWSTONE)
				.properties(p -> p.mapColor(color).lightLevel(s -> s.getValue(ModernLampBlock.LIT) ? 15 : 0))
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag, BlazingTags.Blocks.MODERN_LAMPS.tag)
				.blockstate((c, p) -> p
						.getVariantBuilder(c.get())
						.forAllStates(state -> ConfiguredModel
								.builder()
								.modelFile(p
										.models()
										.cubeAll(color.getName()
														+ "_modern_lamp"
														+ (state.getValue(ModernLampBlock.LIT) ? "_powered" : ""),
												p.modLoc("block/modern_lamp/block/" + color.getName() + (state.getValue(
														ModernLampBlock.LIT) ? "_powered" : ""))))
								.build()

						))
				.item()
				.tag(BlazingTags.Items.MODERN_LAMPS.tag)
				.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
				.model((c, b) -> b.blockItem(c).texture("#all", b.modLoc("block/modern_lamp/block/" + color.getName())))
				.build();
	}

	public static <B extends ModernLampPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampPanel(DyeColor color, String name) {
		String tagName = name.replace('/', '_') + "s";
		return b -> b
				.tag(CommonTags.blockTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> {
							Direction facing = state.getValue(ModernLampPanelBlock.FACING);
							int xRotation = facing == Direction.DOWN ? 180 : 0;
							int yRotation = facing.getAxis().isVertical() ? 0 : (int) facing.toYRot();

							String variant = color.getName();
							if (state.getValue(ModernLampPanelBlock.FACING).getAxis().isHorizontal()) variant += "_vertical";
							if (state.getValue(ModernLampPanelBlock.LIT)) variant += "_powered";

							return ConfiguredModel
									.builder()
									.modelFile(p.models().getExistingFile(p.modLoc("block/" + name + "/" + variant)))
									.rotationX((xRotation + 360) % 360)
									.rotationY((yRotation + 360) % 360)
									.build();
						}

				))
				.item()
				.tag(CommonTags.itemTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
				.transform(ModelGen.customItemModel(name, color.getName()));
	}

	public static <B extends ModernLampDoublePanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampDirectionalPanel(DyeColor color, String name) {
		String tagName = name.replace('/', '_') + "s";
		return b -> b
				.tag(CommonTags.blockTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> {
					Direction facing = state.getValue(ModernLampPanelBlock.FACING);
					int xRotation = facing == Direction.DOWN ? 180 : 0;

					int yRotation = facing.getAxis().isVertical() ? 0 : (int) facing.toYRot();

					String variant = color.getName();
					if (state.getValue(ModernLampDoublePanelBlock.HORIZONTAL)) variant += "_h";
					if (state.getValue(ModernLampPanelBlock.FACING).getAxis().isHorizontal()) variant += "_vertical";
					if (state.getValue(ModernLampPanelBlock.LIT)) variant += "_powered";

					return ConfiguredModel
							.builder()
							.modelFile(p.models().getExistingFile(p.modLoc("block/" + name + "/" + variant)))
							.rotationX((xRotation + 360) % 360)
							.rotationY((yRotation + 360) % 360)
							.build();
				}))
				.item()
				.tag(CommonTags.itemTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
				.transform(ModelGen.customItemModel(name, color.getName() + "_h"));
	}

	public static <B extends ModernLampHalfPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampHalfPanel(DyeColor color, String name) {
		String tagName = name.replace('/', '_') + "s";
		return b -> b
				.tag(CommonTags.blockTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> {
					Direction facing = state.getValue(ModernLampPanelBlock.FACING);
					int xRotation = facing == Direction.DOWN ? 180 : 0;

					int yRotation = facing.getAxis().isVertical() ? 0 : (int) facing.toYRot();

					String variant = color.getName() + "_" + state.getValue(ModernLampHalfPanelBlock.OFFSET).shortName;
					if (state.getValue(ModernLampPanelBlock.FACING).getAxis().isHorizontal()) variant += "_vertical";
					if (state.getValue(ModernLampPanelBlock.LIT)) variant += "_powered";

					return ConfiguredModel
							.builder()
							.modelFile(p.models().getExistingFile(p.modLoc("block/" + name + "/" + variant)))
							.rotationX((xRotation + 360) % 360)
							.rotationY((yRotation + 360) % 360)
							.build();
				}))
				.item()
				.tag(CommonTags.itemTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
				.transform(ModelGen.customItemModel(name, color.getName() + "_h"));
	}

	public static <B extends SmallModernLampPanelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> modernLampSmallPanel(DyeColor color, String name) {
		String tagName = name.replace('/', '_') + "s";
		return b -> b
				.tag(CommonTags.blockTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> {
					Direction facing = state.getValue(ModernLampPanelBlock.FACING);
					int xRotation = facing == Direction.DOWN ? 180 : 0;

					int yRotation = facing.getAxis().isVertical() ? 0 : (int) facing.toYRot();

					String
							variant =
							color.getName() + "_" + state.getValue(SmallModernLampPanelBlock.OFFSET).getShortName();
					if (state.getValue(ModernLampPanelBlock.FACING).getAxis().isHorizontal()) variant += "_vertical";
					if (state.getValue(ModernLampPanelBlock.LIT)) variant += "_powered";

					return ConfiguredModel
							.builder()
							.modelFile(p.models().getExistingFile(p.modLoc("block/" + name + "/" + variant)))
							.rotationX((xRotation + 360) % 360)
							.rotationY((yRotation + 360) % 360)
							.build();
				}))
				.item()
				.tag(CommonTags.itemTagOf(tagName, CommonTags.Namespace.INTERNAL))
				.onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, ItemDescriptions.MODERN_LAMP.getKey()))
				.transform(ModelGen.customItemModel(name, color.getName() + "_c"));
	}

	public static <B extends ModernLampBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> anyModernLamp(DyeColor color) {
		return b -> b
				.initialProperties(() -> Blocks.GLOWSTONE)
				.properties(p -> p
						.mapColor(color)
						.lightLevel(s -> s.getValue(ModernLampPanelBlock.LIT) ? 15 : 0)
						.forceSolidOn())
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag);
	}

	public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> mold(String name, Molds.MoldType type) {
		return b -> b.tag(BlazingTags.Items.MOLDS.tag, type.tag).properties(p -> {
			if (type.fireResistant) p.fireResistant();
			return p;
		}).model((c, p) -> p.generated(c::get, BlazingHot.asResource("item/" + type.name + "_mold/" + name)));
	}
}
