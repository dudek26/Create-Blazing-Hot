package com.dudko.blazinghot.multiloader.fabric;

import static com.dudko.blazinghot.multiloader.Platform.FORGE;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;

public class MultiRecipeConditions {

	public static JsonObject toForgeCondition(ConditionJsonProvider provider) {
		JsonObject fabricCondition = provider.toJson();
		switch (provider.getConditionId().getPath()) {
			case "tags_populated" -> {
				JsonArray tags = fabricCondition.getAsJsonArray("values");
				if (tags.size() > 1) {
					JsonArray tagConditions = new JsonArray();
					tags.forEach(t -> tagConditions.add(ForgeConditions.tagEmpty(t.getAsString())));
					return ForgeConditions.not(ForgeConditions.or(tagConditions));
				}
				else return ForgeConditions.not(ForgeConditions.tagEmpty(tags.get(0).getAsString()));
			}
			case "all_mods_loaded" -> {
				JsonArray mods = fabricCondition.getAsJsonArray("values");
				if (mods.size() > 1) {
					JsonArray modConditions = new JsonArray();
					for (JsonElement mod : mods) {
						modConditions.add(ForgeConditions.modLoaded(mod.getAsString()));
					}
					return ForgeConditions.and(modConditions);
				}
				else return ForgeConditions.modLoaded(mods.get(0).getAsString());
			}
			case "any_mod_loaded" -> {
				JsonArray mods = fabricCondition.getAsJsonArray("values");
				if (mods.size() > 1) {
					JsonArray modConditions = new JsonArray();
					for (JsonElement mod : mods) {
						modConditions.add(ForgeConditions.modLoaded(mod.getAsString()));
					}
					return ForgeConditions.or(modConditions);
				}
				else return ForgeConditions.modLoaded(mods.get(0).getAsString());
			}
			default -> throw new UnsupportedOperationException("Unsupported condition type: " + provider
					.getConditionId()
					.toString());
		}
	}


	public static class ForgeConditions {

		public static JsonObject modLoaded(String modid) {
			JsonObject condition = new JsonObject();
			condition.addProperty("type", FORGE.asResource("mod_loaded"));
			condition.addProperty("modid", modid);
			return condition;
		}

		public static JsonObject tagEmpty(String tag) {
			JsonObject condition = new JsonObject();
			condition.addProperty("type", FORGE.asResource("tag_empty"));
			condition.addProperty("tag", tag);
			return condition;
		}

		public static JsonObject not(JsonObject condition) {
			JsonObject forgeCondition = new JsonObject();
			forgeCondition.addProperty("type", FORGE.asResource("not"));
			forgeCondition.add("value", condition);
			return forgeCondition;
		}

		public static JsonObject arrayCondition(JsonArray conditions, String condition) {
			JsonObject forgeCondition = new JsonObject();
			forgeCondition.addProperty("type", FORGE.asResource(condition));
			forgeCondition.add("values", conditions);
			return forgeCondition;
		}

		public static JsonObject and(JsonArray conditions) {
			return arrayCondition(conditions, "and");
		}

		public static JsonObject or(JsonArray conditions) {
			return arrayCondition(conditions, "or");
		}

	}

}
