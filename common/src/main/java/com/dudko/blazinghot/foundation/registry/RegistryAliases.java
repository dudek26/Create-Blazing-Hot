package com.dudko.blazinghot.foundation.registry;

import java.util.ArrayList;
import java.util.List;

import com.dudko.blazinghot.BlazingHot;
import com.tterrag.registrate.util.entry.RegistryEntry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.createmod.catnip.data.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public abstract class RegistryAliases<T> {

	protected final Registry<T> registry;
	protected final List<Pair<ResourceLocation, ResourceLocation>> aliases;

	public RegistryAliases(Registry<T> registry) {
		this.registry = registry;
		this.aliases = new ArrayList<>();
	}

	protected void gatherAliases() {
		aliases.forEach(this::registerAlias);
	}

	@ExpectPlatform
	public static <T> RegistryAliases<T> create(Registry<T> registry) {
		throw new AssertionError();
	}

	protected abstract void registerAlias(Pair<ResourceLocation, ResourceLocation> alias);

	public void addAlias(ResourceLocation from, ResourceLocation to) {
		aliases.add(Pair.of(from, to));
	}

	public void addAlias(String from, String to) {
		aliases.add(Pair.of(BlazingHot.asResource(from), BlazingHot.asResource(to)));
	}

	public <R extends T> void addAlias(String from, RegistryEntry<T, R> registryEntry) {
		addAlias(BlazingHot.asResource(from), registryEntry.getId());
	}

}
