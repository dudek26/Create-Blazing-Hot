package com.dudko.blazinghot.foundation.registry.neoforge;

import org.jetbrains.annotations.ApiStatus;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.foundation.registry.RegistryAliases;

import net.createmod.catnip.data.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @see com.dudko.blazinghot.foundation.registry.RegistryAliases
 */
public class RegistryAliasesImpl<T> extends RegistryAliases<T> {

	private final DeferredRegister<T> register;

	public RegistryAliasesImpl(Registry<T> registry) {
		super(registry);
		this.register = DeferredRegister.create(registry, BlazingHot.ID);
	}
	
	public static <T> RegistryAliases<T> create(Registry<T> registry) {
		return new RegistryAliasesImpl<>(registry);
	}

	@Override
	protected void registerAlias(Pair<ResourceLocation, ResourceLocation> alias) {
		register.addAlias(alias.getFirst(), alias.getSecond());
	}

	@ApiStatus.Internal
	public void neoForgeRegistration(IEventBus bus) {
		gatherAliases();
		register.register(bus);
	}
}
