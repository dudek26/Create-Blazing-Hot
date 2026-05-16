package com.dudko.blazinghot.compat;

import java.util.function.Supplier;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.simibubi.create.Create;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;

public enum Mods {

	VANILLA("minecraft", true),
	BLAZINGHOT(BlazingHot.ID, true),
	CREATE(Create.ID, true),

	// Create addons
	CREATE_ADDITIONS("createaddition"), // Create: Crafts & Additions
	CREATE_DREAMS_AND_DESIRES("dndesires"), // Create: Dreams & Desires
	CREATE_DRAGONS_PLUS("create_dragons_plus"), // Create: Dragons Plus
	CREATE_TFMG("tfmg"), // Create: The Factory Must Grow
	CREATE_BIG_CANNONS("createbigcannons"), // Create: Big Cannons

	// Other
	TECH_REBORN("tech_reborn"), // fabric-exclusive
	MEKANISM("mekanism"), // neoforge-exclusive
	AD_ASTRA("ad_astra"), // 1.20.1
	IMMERSIVE_ENGINEERING("immersiveengineering");

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

	public boolean isLoaded() {
		return isModLoaded(this);
	}

	public void runIfLoaded(Runnable runnable) {
		if (isLoaded()) {
			runnable.run();
		}
	}

	public <T> T getIfLoaded(Supplier<T> supplier, T fallback) {
		if (isLoaded()) {
			return supplier.get();
		}
		return fallback;
	}

	@ExpectPlatform
	public static boolean isModLoaded(Mods mod) {
		throw new AssertionError();
	}

}
