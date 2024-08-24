package com.dudko.blazinghot.data.recipe.fabric;

import static com.dudko.blazinghot.content.metal.Forms.INGOT;
import static com.dudko.blazinghot.content.metal.Forms.NUGGET;
import static com.dudko.blazinghot.content.metal.Forms.PLATE;
import static com.dudko.blazinghot.content.metal.Forms.ROD;
import static com.dudko.blazinghot.registry.CommonTags.Namespace.INTERNAL;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BlazingIngredients {

	//    Shortcuts

	public static TagKey<Item> ingotTag(String material) {
		return INGOT.internalTag(material);
	}

	public static TagKey<Item> nuggetTag(String material) {
		return NUGGET.internalTag(material);
	}

	public static Item modApple(String material) {
		return BuiltInRegistries.ITEM.get(BlazingHot.asResource(material + "_apple"));
	}

	public static Item modStellarApple(String material) {
		return BuiltInRegistries.ITEM.get(BlazingHot.asResource("stellar_" + material + "_apple"));
	}

	public static Item modEnchantedApple(String material) {
		return BuiltInRegistries.ITEM.get(BlazingHot.asResource("enchanted_" + material + "_apple"));
	}

	//    Kinetics

	public static Item cogwheel() {
		return AllBlocks.COGWHEEL.asItem();
	}

	public static Item extensionPole() {
		return AllBlocks.PISTON_EXTENSION_POLE.asItem();
	}

	public static Item blazeWhisk() {
		return BlazingItems.BLAZE_WHISK.get();
	}

	//    Casings

	public static Item copperCasing() {
		return AllBlocks.COPPER_CASING.asItem();
	}

	public static Item blazeCasing() {
		return BlazingBlocks.BLAZE_CASING.asItem();
	}

	//    Fluids

	public static TagKey<Fluid> moltenGold() {
		return MoltenMetals.GOLD.fluidTag();
	}

	public static TagKey<Fluid> moltenIron() {
		return MoltenMetals.IRON.fluidTag();
	}

	public static TagKey<Fluid> moltenCopper() {
		return MoltenMetals.COPPER.fluidTag();
	}

	public static TagKey<Fluid> moltenZinc() {
		return MoltenMetals.ZINC.fluidTag();
	}

	public static TagKey<Fluid> moltenBlazeGold() {
		return MoltenMetals.BLAZE_GOLD.fluidTag();
	}

	public static TagKey<Fluid> moltenAncientDebris() {
		return MoltenMetals.ANCIENT_DEBRIS.fluidTag();
	}

	public static TagKey<Fluid> moltenNetherite() {
		return MoltenMetals.NETHERITE.fluidTag();
	}

	public static Fluid water() {
		return Fluids.WATER;
	}

	public static Fluid lava() {
		return Fluids.LAVA;
	}

	public static TagKey<Fluid> fuel() {
		return BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag;
	}

	//    Materials

	public static Item netherEssence() {
		return BlazingItems.NETHER_ESSENCE.get();
	}

	public static Item diamond() {
		return Items.DIAMOND;
	}

	public static TagKey<Item> coal() {
		return ItemTags.COALS;
	}

	public static Item clayBall() {
		return Items.CLAY_BALL;
	}

	public static TagKey<Item> netherFlora() {
		return BlazingTags.Items.NETHER_FLORA.tag;
	}

	public static Item netherCompound() {
		return BlazingItems.NETHER_COMPOUND.get();
	}

	public static Item cinderFlour() {
		return AllItems.CINDER_FLOUR.get();
	}

	public static Item feather() {
		return Items.FEATHER;
	}

	//    Dusts

	public static TagKey<Item> stoneDust() {
		return CommonTags.itemTagOf(INTERNAL.tagPath("dusts", "stone"), INTERNAL);
	}

	public static TagKey<Item> netherrackDust() {
		return CommonTags.itemTagOf(INTERNAL.tagPath("dusts", "netherrack"), INTERNAL);
	}

	public static TagKey<Item> soulDust() {
		return CommonTags.itemTagOf(INTERNAL.tagPath("dusts", "soul_sand"), INTERNAL);
	}

	//    Blocks

	public static Item stone() {
		return Items.STONE;
	}

	public static Item netherrack() {
		return Items.NETHERRACK;
	}

	public static Item soulSand() {
		return Items.SOUL_SAND;
	}

	public static Item glowstone() {
		return Items.GLOWSTONE;
	}

	public static Item andesite() {
		return Items.ANDESITE;
	}

	//    Metals

	public static TagKey<Item> ironIngot() {
		return ingotTag("iron");
	}

	public static TagKey<Item> ironNugget() {
		return nuggetTag("iron");
	}

	public static TagKey<Item> goldIngot() {
		return ingotTag("gold");
	}

	public static TagKey<Item> goldNugget() {
		return nuggetTag("gold");
	}

	public static TagKey<Item> copperIngot() {
		return ingotTag("copper");
	}

	public static TagKey<Item> copperNugget() {
		return nuggetTag("copper");
	}

	public static TagKey<Item> zincIngot() {
		return ingotTag("zinc");
	}

	public static TagKey<Item> zincNugget() {
		return nuggetTag("zinc");
	}

	public static TagKey<Item> brassIngot() {
		return ingotTag("brass");
	}

	public static TagKey<Item> brassNugget() {
		return nuggetTag("brass");
	}

	public static TagKey<Item> blazeGoldIngot() {
		return ingotTag("blaze_gold");
	}

	public static TagKey<Item> blazeGoldNugget() {
		return nuggetTag("blaze_gold");
	}

	public static TagKey<Item> blazeGoldSheet() {
		return PLATE.internalTag(MoltenMetals.BLAZE_GOLD);
	}

	public static TagKey<Item> blazeGoldRod() {
		return ROD.internalTag(MoltenMetals.BLAZE_GOLD);
	}

	//    Food

	public static Item apple() {
		return Items.APPLE;
	}

	public static Item melon() {
		return Items.MELON;
	}

	public static Item carrot() {
		return Items.CARROT;
	}

	//    Metal Food

	public static ItemLike goldenApple() {
		return Items.GOLDEN_APPLE;
	}

	public static ItemLike stellarGoldenApple() {
		return modStellarApple("golden");
	}

	public static ItemLike blazeApple() {
		return modApple("blaze");
	}

	public static ItemLike stellarBlazeApple() {
		return modStellarApple("blaze");
	}

	public static ItemLike ironApple() {
		return modApple("iron");
	}

	public static ItemLike stellarIronApple() {
		return modStellarApple("iron");
	}

	public static ItemLike brassApple() {
		return modApple("brass");
	}

	public static ItemLike stellarBrassApple() {
		return modStellarApple("brass");
	}

	public static ItemLike copperAppple() {
		return modApple("copper");
	}

	public static ItemLike stellarCopperApple() {
		return modStellarApple("copper");
	}

	public static ItemLike zincApple() {
		return modApple("zinc");
	}

	public static ItemLike stellarZincApple() {
		return modStellarApple("zinc");
	}

	public static Ingredient netheriteAppleIngredients() {
		return Ingredient.of(BlazingItems.ENCHANTED_BLAZE_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
	}


}
