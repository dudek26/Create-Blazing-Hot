package com.dudko.blazinghot.compat;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.Create;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
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
		return new ResourceLocation(id, path);
	}

	public ConditionJsonProvider asLoadCondition() {
		return DefaultResourceConditions.anyModLoaded(id);
	}

}
