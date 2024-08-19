package com.dudko.blazinghot.data.fabric;

import com.dudko.blazinghot.content.metal.Form;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingTags;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.*;

import static com.dudko.blazinghot.registry.CommonTags.Namespace.*;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;

public class BlazingTagGen {

    private static final Map<TagKey<Block>, List<ResourceLocation>> OPTIONAL_TAGS = new HashMap<>();

    @SafeVarargs
    public static void addOptionalTag(ResourceLocation id, TagKey<Block>... tags) {
        for (TagKey<Block> tag : tags) {
            OPTIONAL_TAGS.computeIfAbsent(tag, (e) -> new ArrayList<>()).add(id);
        }
    }

    public static void generateBlockTags(RegistrateTagsProvider<Block> prov) {
        for (BlazingTags.Blocks tag : BlazingTags.Blocks.values()) {
            if (tag.alwaysDatagen) {
                tagAppender(prov, tag);
            }
        }
        for (TagKey<Block> tag : OPTIONAL_TAGS.keySet()) {
            var appender = tagAppender(prov, tag);
            for (ResourceLocation loc : OPTIONAL_TAGS.get(tag))
                appender.addOptional(loc);
        }
        for (CommonTags.Blocks tag : CommonTags.Blocks.values()) {
            tagAppender(prov, tag.fabric);
            tagAppender(prov, tag.forge);
            tagAppender(prov, tag.internal).addTag(tag.forge).addTag(tag.fabric);
        }
    }

    public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov) {
        prov
                .addTag(BlazingTags.Fluids.BLAZE_MIXER_FUEL.tag)
                .add(Fluids.LAVA)
                .add(BlazingFluidsImpl.NETHER_LAVA.getSource());

        for (BlazingTags.Fluids tag : BlazingTags.Fluids.values()) {
            if (tag.alwaysDatagen) {
                tagAppender(prov, tag);
            }
        }

        for (MoltenMetal metal : MoltenMetal.ALL_METALS) {
            TagKey<Fluid> forgeTag = CommonTags.fluidTagOf(metal.moltenName(), FORGE);
            TagKey<Fluid> commonTag = CommonTags.fluidTagOf(metal.moltenName(), COMMON);
            TagKey<Fluid> internalTag = CommonTags.fluidTagOf(metal.moltenName(), INTERNAL);

            tagAppender(prov, forgeTag);
            tagAppender(prov, commonTag);
            tagAppender(prov, internalTag).addTag(forgeTag).addTag(commonTag);
        }

        for (CommonTags.Fluids tag : CommonTags.Fluids.values()) {
            tagAppender(prov, tag.fabric);
            tagAppender(prov, tag.forge);
            tagAppender(prov, tag.internal).addTag(tag.forge).addTag(tag.fabric);
        }
    }

    private static final Set<TagKey<Item>> ALL_INTERNAL_ITEM_TAGS = new HashSet<>();

    public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
        prov
                .addTag(BlazingTags.Items.NETHER_FLORA.tag)
                .add(Items.WARPED_FUNGUS,
                        Items.CRIMSON_FUNGUS,
                        Items.WARPED_ROOTS,
                        Items.CRIMSON_ROOTS,
                        Items.WEEPING_VINES,
                        Items.TWISTING_VINES,
                        Items.NETHER_SPROUTS);

        prov.addTag(BlazingTags.Items.METAL_CARROTS.tag).add(Items.GOLDEN_CARROT);
        prov.addTag(BlazingTags.Items.METAL_APPLES.tag).add(Items.GOLDEN_APPLE);
        prov.addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag).add(Items.ENCHANTED_GOLDEN_APPLE);

        tagAppender(prov, BlazingTags.Items.METAL_FOOD).addTag(BlazingTags.Items.METAL_CARROTS.tag)
                .addTag(BlazingTags.Items.METAL_APPLES.tag)
                .addTag(BlazingTags.Items.STELLAR_METAL_APPLES.tag)
                .addTag(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag);

        for (BlazingTags.Items tag : BlazingTags.Items.values()) {
            if (tag.alwaysDatagen) tagAppender(prov, tag);
        }

        for (MoltenMetal metal : MoltenMetal.ALL_METALS) {
            if (metal.ignoreTagGen) continue;
            for (Form form : metal.nonCustomForms()) {
                TagKey<Item> forgeTag = itemTagOf(FORGE.tagPath(form.tagFolder, metal.name), FORGE);
                TagKey<Item> commonTag = itemTagOf(COMMON.tagPath(form.tagFolder, metal.name), COMMON);
                TagKey<Item> internalTag = itemTagOf(INTERNAL.tagPath(form.tagFolder, metal.name), INTERNAL);

                tagAppender(prov, forgeTag);
                tagAppender(prov, commonTag);

                TagsProvider.TagAppender<Item> internalTagAppender = tagAppender(prov, internalTag);
                addTagsIfAbsent(internalTagAppender, commonTag, forgeTag);
            }
        }

        for (CommonTags.Items tag : CommonTags.Items.values()) {
            tagAppender(prov, tag.fabric);
            tagAppender(prov, tag.forge);
            TagsProvider.TagAppender<Item> internalTagAppender = tagAppender(prov, tag.internal);
            addTagsIfAbsent(internalTagAppender, tag.forge, tag.fabric);
        }

    }

    public static TagsProvider.TagAppender<Item> tagAppender(RegistrateTagsProvider<Item> prov, BlazingTags.Items tag) {
        return tagAppender(prov, tag.tag);
    }

    public static TagsProvider.TagAppender<Block> tagAppender(RegistrateTagsProvider<Block> prov, BlazingTags.Blocks tag) {
        return tagAppender(prov, tag.tag);
    }

    public static TagsProvider.TagAppender<Fluid> tagAppender(RegistrateTagsProvider<Fluid> prov, BlazingTags.Fluids tag) {
        return tagAppender(prov, tag.tag);
    }

    public static <T> TagsProvider.TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        return prov.addTag(tag);
    }

    @SafeVarargs
    public static void addTagsIfAbsent(TagsProvider.TagAppender<Item> appender, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            boolean absent = ALL_INTERNAL_ITEM_TAGS.add(tag);
            if (absent) appender.addTag(tag);
        }
    }
}
