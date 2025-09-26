package com.dudko.blazinghot.data.conditions.neoforge;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.AndCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.OrCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

@SuppressWarnings("unchecked")
public class LoadConditionImpl<T> extends LoadCondition<T> {

	protected LoadConditionImpl(Type type, T... values) {
		super(type, values);
	}

	@SafeVarargs
	public static <T> LoadCondition<T> create(Type type, T... values) {
		return new LoadConditionImpl<>(type, values);
	}

	private List<ICondition> neoForgeListCondition(LoadCondition<LoadCondition<?>> condition) {
		return condition.getValues().stream().map(this::getNeoForgeCondition).toList();
	}

	public ICondition getNeoForgeCondition() {
		return getNeoForgeCondition(this);
	}

	public ICondition getNeoForgeCondition(LoadCondition<?> condition) {
		switch (condition.getType()) {
			case TAGS_POPULATED -> {
				TagKey<?>[] tags = LoadConditionHelper.tagValues(condition.getValues());
				if (tags.length == 1) {
					return new NotCondition(new TagEmptyCondition(tags[0].location().toString()));
				}
				List<ICondition>
						nots =
						Stream
								.of(tags)
								.map(tag -> new NotCondition(new TagEmptyCondition(tag.location().toString())))
								.collect(Collectors.toUnmodifiableList());
				return new AndCondition(nots);
			}
			case ALL_MODS_LOADED -> {
				String[] mods = LoadConditionHelper.stringValues(condition.getValues());
				if (mods.length == 1) {
					return new ModLoadedCondition(mods[0]);
				}
				List<ICondition>
						modLoaded =
						Stream.of(mods).map(ModLoadedCondition::new).collect(Collectors.toUnmodifiableList());
				return new AndCondition(modLoaded);
			}
			case ANY_MOD_LOADED -> {
				String[] mods = LoadConditionHelper.stringValues(condition.getValues());
				if (mods.length == 1) {
					return new ModLoadedCondition(mods[0]);
				}
				List<ICondition>
						modLoaded =
						Stream.of(mods).map(ModLoadedCondition::new).collect(Collectors.toUnmodifiableList());
				return new OrCondition(modLoaded);
			}
			case AND -> {
				return new AndCondition(neoForgeListCondition((LoadCondition<LoadCondition<?>>) condition));
			}
			case OR -> {
				return new OrCondition(neoForgeListCondition((LoadCondition<LoadCondition<?>>) condition));
			}
			case NOT -> {
				return new NotCondition(getNeoForgeCondition((LoadCondition<?>) condition.getValues().getFirst()));
			}
			case ITEMS_REGISTERED -> {
				ItemLike[] items = LoadConditionHelper.itemLikeValues(condition.getValues());
				if (items.length == 1) {
					return new ItemExistsCondition(BuiltInRegistries.ITEM.getKey(items[0].asItem()));
				}
				return new OrCondition(Stream
						.of(items)
						.map(item -> new ItemExistsCondition(BuiltInRegistries.ITEM.getKey(item.asItem())))
						.collect(Collectors.toUnmodifiableList()));
			}
			default -> throw new UnsupportedOperationException("Unsupported forge condition type: "
					+ condition.getType().id);
		}
	}

}
