package com.dudko.blazinghot.data.conditions.forge;

import java.util.List;
import java.util.stream.Stream;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;
import com.google.gson.JsonObject;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.AndCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unchecked")
public class LoadConditionImpl {

	private static ICondition[] forgeArrayCondition(LoadCondition<LoadCondition<?>> condition) {
		return condition.getValues().stream().map(LoadConditionImpl::getForgeCondition).toArray(ICondition[]::new);
	}

	private static ICondition getForgeCondition(LoadCondition<?> condition) {
		switch (condition.getType()) {
			case TAGS_POPULATED -> {
				TagKey<?>[] tags = LoadConditionHelper.tagValues(condition.getValues());
				if (tags.length == 1) {
					return new NotCondition(new TagEmptyCondition(tags[0].location().toString()));
				}
				List<NotCondition>
						nots =
						Stream
								.of(tags)
								.map(tag -> new NotCondition(new TagEmptyCondition(tag.location().toString())))
								.toList();
				return new AndCondition(nots.toArray(new NotCondition[]{}));
			}
			case ALL_MODS_LOADED -> {
				String[] mods = LoadConditionHelper.stringValues(condition.getValues());
				if (mods.length == 1) {
					return new ModLoadedCondition(mods[0]);
				}
				List<ModLoadedCondition> modLoaded = Stream.of(mods).map(ModLoadedCondition::new).toList();
				return new AndCondition(modLoaded.toArray(new ModLoadedCondition[]{}));
			}
			case ANY_MOD_LOADED -> {
				String[] mods = LoadConditionHelper.stringValues(condition.getValues());
				if (mods.length == 1) {
					return new ModLoadedCondition(mods[0]);
				}
				List<ModLoadedCondition> modLoaded = Stream.of(mods).map(ModLoadedCondition::new).toList();
				return new OrCondition(modLoaded.toArray(new ModLoadedCondition[]{}));
			}
			case AND -> {
				return new AndCondition(forgeArrayCondition((LoadCondition<LoadCondition<?>>) condition));
			}
			case OR -> {
				return new OrCondition(forgeArrayCondition((LoadCondition<LoadCondition<?>>) condition));
			}
			case NOT -> {
				return new NotCondition(getForgeCondition((LoadCondition<?>) condition.getValues().get(0)));
			}
			case ITEMS_REGISTERED -> {
				ItemLike[] items = LoadConditionHelper.itemLikeValues(condition.getValues());
				if (items.length == 1) {
					return new ItemExistsCondition(ForgeRegistries.ITEMS.getKey(items[0].asItem()));
				}
				return new OrCondition(Stream
						.of(items)
						.map(item -> new ItemExistsCondition(ForgeRegistries.ITEMS.getKey(item.asItem())))
						.toArray(ICondition[]::new));
			}
			default -> throw new UnsupportedOperationException("Unsupported forge condition type: "
					+ condition.getType().id);
		}
	}

	public static <T> JsonObject platformJson(LoadCondition<T> condition) {

		if (condition.getValues().isEmpty()) return new JsonObject();

		ICondition forgeCondition = getForgeCondition(condition);
		String id = forgeCondition.getID().toString();

		switch (id) {
			case "forge:and" -> {
				return AndCondition.Serializer.INSTANCE.getJson((AndCondition) forgeCondition);
			}
			case "forge:or" -> {
				return OrCondition.Serializer.INSTANCE.getJson((OrCondition) forgeCondition);
			}
			case "forge:not" -> {
				return NotCondition.Serializer.INSTANCE.getJson((NotCondition) forgeCondition);
			}
			case "forge:mod_loaded" -> {
				return ModLoadedCondition.Serializer.INSTANCE.getJson((ModLoadedCondition) forgeCondition);
			}
			case "forge:tag_empty" -> {
				return TagEmptyCondition.Serializer.INSTANCE.getJson((TagEmptyCondition) forgeCondition);
			}
			case "forge:item_exists" -> {
				return ItemExistsCondition.Serializer.INSTANCE.getJson((ItemExistsCondition) forgeCondition);
			}
			default -> throw new UnsupportedOperationException("Unsupported forge condition type: " + id);
		}

	}
}
