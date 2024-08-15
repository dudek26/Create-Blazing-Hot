package com.dudko.blazinghot.compat;

import com.dudko.blazinghot.BlazingHot;
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

    public final String namespace;
    public final boolean alwaysIncluded;

    Mods(String namespace, boolean alwaysIncluded) {
        this.namespace = namespace;
        this.alwaysIncluded = alwaysIncluded;
    }

    Mods(String namespace) {
        this(namespace, false);
    }

    public ResourceLocation asResource(String path) {
        return new ResourceLocation(namespace, path);
    }

}
