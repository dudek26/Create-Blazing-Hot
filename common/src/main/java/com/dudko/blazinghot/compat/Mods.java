package com.dudko.blazinghot.compat;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.simibubi.create.Create;

import net.minecraft.resources.ResourceLocation;

public enum Mods {

	VANILLA("minecraft", true),
	BLAZINGHOT(BlazingHot.ID, true),
	CREATE(Create.ID, true),

	// Create addons
	CREATE_ADDITIONS("createaddition"), // Create: Crafts & Additions
	CREATE_DD("create_dd"), // Create: Dreams & Desires

	// Other
	TECH_REBORN("tech_reborn"),
	MEKANISM("mekanism"),
	AD_ASTRA("ad_astra");

	public final String id;
	public final boolean alwaysIncluded;

	Mods(String id, boolean alwaysIncluded) {
		this.id = id;
		this.alwaysIncluded = alwaysIncluded;
	}

	Mods(String id) {
		this(id, false);
	}

	public ResourceLocation asResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(id, path);
	}

	public LoadCondition<String> asLoadCondition() {
		return DefaultLoadConditions.anyModLoaded(this);
	}

}
