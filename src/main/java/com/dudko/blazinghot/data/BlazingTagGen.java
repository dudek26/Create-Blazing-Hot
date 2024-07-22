package com.dudko.blazinghot.data;

import com.dudko.blazinghot.registry.BlazingTags;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
        prov
                .addTag(BlazingTags.Items.NETHER_FLORA.tag)
                .add(net.minecraft.world.item.Items.WARPED_FUNGUS, net.minecraft.world.item.Items.CRIMSON_FUNGUS,
                     net.minecraft.world.item.Items.WARPED_ROOTS, net.minecraft.world.item.Items.CRIMSON_ROOTS,
                     net.minecraft.world.item.Items.WEEPING_VINES, net.minecraft.world.item.Items.TWISTING_VINES);

        for (BlazingTags.Items tag : BlazingTags.Items.values()) {
            if (tag.alwaysDatagen) tagAppender(prov, tag);
        }
    }

    public static TagsProvider.TagAppender<Item> tagAppender(RegistrateTagsProvider<Item> prov, BlazingTags.Items tag) {
        return tagAppender(prov, tag.tag);
    }

    public static TagsProvider.TagAppender<Block> tagAppender(RegistrateTagsProvider<Block> prov, BlazingTags.Blocks tag) {
        return tagAppender(prov, tag.tag);
    }

    public static <T> TagsProvider.TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
        return prov.addTag(tag);
    }
}
