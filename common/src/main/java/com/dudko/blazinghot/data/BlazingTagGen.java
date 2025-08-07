package com.dudko.blazinghot.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dudko.blazinghot.registry.BlazingTagsV1;
import com.tterrag.registrate.providers.RegistrateTagsProvider;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

@Deprecated(forRemoval = true)
public class BlazingTagGen {

	public static final Map<TagKey<Block>, List<ResourceLocation>> OPTIONAL_TAGS = new HashMap<>();

	@SafeVarargs
	public static void addOptionalTag(ResourceLocation id, TagKey<Block>... tags) {
		for (TagKey<Block> tag : tags) {
			OPTIONAL_TAGS.computeIfAbsent(tag, (e) -> new ArrayList<>()).add(id);
		}
	}

	@ExpectPlatform
	public static void generateBlockTags(RegistrateTagsProvider<Block> prov) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void generateFluidTags(RegistrateTagsProvider<Fluid> prov) {
		throw new AssertionError();
	}

	public static final Set<TagKey<Item>> ALL_INTERNAL_ITEM_TAGS = new HashSet<>();

	@ExpectPlatform
	public static void generateItemTags(RegistrateTagsProvider<Item> prov) {
		throw new AssertionError();
	}

	public static TagsProvider.TagAppender<Item> tagAppender(RegistrateTagsProvider<Item> prov, BlazingTagsV1.Items tag) {
		return tagAppender(prov, tag.tag);
	}

	public static TagsProvider.TagAppender<Block> tagAppender(RegistrateTagsProvider<Block> prov, BlazingTagsV1.Blocks tag) {
		return tagAppender(prov, tag.tag);
	}

	public static TagsProvider.TagAppender<Fluid> tagAppender(RegistrateTagsProvider<Fluid> prov, BlazingTagsV1.Fluids tag) {
		return tagAppender(prov, tag.tag);
	}

	@ExpectPlatform
	public static <T> TagsProvider.TagAppender<T> tagAppender(RegistrateTagsProvider<T> prov, TagKey<T> tag) {
		throw new AssertionError();
	}

	@SafeVarargs
	public static void addTagsIfAbsent(TagsProvider.TagAppender<Item> appender, TagKey<Item>... tags) {
		for (TagKey<Item> tag : tags) {
			boolean absent = ALL_INTERNAL_ITEM_TAGS.add(tag);
			if (absent) appender.addTag(tag);
		}
	}
}
