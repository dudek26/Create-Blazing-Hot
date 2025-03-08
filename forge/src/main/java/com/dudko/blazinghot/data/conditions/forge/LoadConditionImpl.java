package com.dudko.blazinghot.data.conditions.forge;

import java.util.List;
import java.util.stream.Stream;

import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.data.conditions.LoadConditionHelper;
import com.google.gson.JsonObject;

import net.minecraft.tags.TagKey;
import net.minecraftforge.common.crafting.conditions.AndCondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

public class LoadConditionImpl {

	public static <T> JsonObject platformJson(LoadCondition<T> condition) {

		if (condition.getValues().isEmpty()) return new JsonObject();

		switch (condition.getType()) {
			case TAGS_POPULATED -> {
				TagKey<?>[] tags = LoadConditionHelper.tagValues(condition.getValues());
				if (tags.length == 1) {
					NotCondition not = new NotCondition(new TagEmptyCondition(tags[0].location().toString()));
					return NotCondition.Serializer.INSTANCE.getJson(not);
				}
				List<NotCondition>
						nots =
						Stream
								.of(tags)
								.map(tag -> new NotCondition(new TagEmptyCondition(tag.location().toString())))
								.toList();
				AndCondition and = new AndCondition(nots.toArray(new NotCondition[]{}));
				return AndCondition.Serializer.INSTANCE.getJson(and);
			}
			case ALL_MODS_LOADED -> {
				String[] mods = LoadConditionHelper.stringValues(condition.getValues());
				if (mods.length == 1) {
					ModLoadedCondition modLoaded = new ModLoadedCondition(mods[0]);
					return ModLoadedCondition.Serializer.INSTANCE.getJson(modLoaded);
				}
				List<ModLoadedCondition> modLoaded = Stream.of(mods).map(ModLoadedCondition::new).toList();
				AndCondition and = new AndCondition(modLoaded.toArray(new ModLoadedCondition[]{}));
				return AndCondition.Serializer.INSTANCE.getJson(and);
			}
			case ANY_MOD_LOADED -> {
				String[] mods = LoadConditionHelper.stringValues(condition.getValues());
				if (mods.length == 1) {
					ModLoadedCondition modLoaded = new ModLoadedCondition(mods[0]);
					return ModLoadedCondition.Serializer.INSTANCE.getJson(modLoaded);
				}
				List<ModLoadedCondition> modLoaded = Stream.of(mods).map(ModLoadedCondition::new).toList();
				OrCondition or = new OrCondition(modLoaded.toArray(new ModLoadedCondition[]{}));
				return OrCondition.Serializer.INSTANCE.getJson(or);
			}
			default -> throw new UnsupportedOperationException("Unsupported condition type: " + condition.getType().id);
		}

	}
}
