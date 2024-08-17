package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import static com.dudko.blazinghot.content.metal.MoltenMetal.*;
import static com.dudko.blazinghot.content.metal.Form.*;
import static com.dudko.blazinghot.registry.CommonTags.Namespace.INTERNAL;

public class Ingredients {

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
        return BuiltInRegistries.ITEM.get(BlazingHot.asResource( "stellar_" + material + "_apple"));
    }

    public static Item modEnchantedApple(String material) {
        return BuiltInRegistries.ITEM.get(BlazingHot.asResource( "enchanted_" + material + "_apple"));
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
        return GOLD.fluidTag();
    }

    public static TagKey<Fluid> moltenIron() {
        return IRON.fluidTag();
    }

    public static TagKey<Fluid> moltenBlazeGold() {
        return BLAZE_GOLD.fluidTag();
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

    public static Item clay() {
        return Items.CLAY;
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
        return PLATE.internalTag(BLAZE_GOLD);
    }

    public static TagKey<Item> blazeGoldRod() {
        return ROD.internalTag(BLAZE_GOLD);
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

    public static Item goldenApple() {
        return Items.GOLDEN_APPLE;
    }

    public static Item stellarGoldenApple() {
        return modStellarApple("golden");
    }

    public static Item blazeApple() {
        return modApple("blaze");
    }

    public static Item stellarBlazeApple() {
        return modStellarApple("blaze");
    }

    public static Item ironApple() {
        return modApple("iron");
    }

    public static Item stellarIronApple() {
        return modStellarApple("iron");
    }


}
