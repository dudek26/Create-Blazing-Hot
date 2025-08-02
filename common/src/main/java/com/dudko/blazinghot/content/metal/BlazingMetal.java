package com.dudko.blazinghot.content.metal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.registry.BlazingForms;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class BlazingMetal {

	public final String name;
	public final List<Mods> mods;
	public final List<BlazingForm> forms;
	public final Map<Fluid, NonNullSupplier<Block>> fluidInteractions;

	private BlazingMetal(Builder builder) {
		name = builder.name;
		mods = List.copyOf(builder.mods);
		forms = List.copyOf(builder.forms);
		fluidInteractions = Map.copyOf(builder.fluidInteractions);
	}

	public static BlazingMetal create(String name, Function<Builder, BlazingMetal> builderFunction) {
		return builderFunction.apply(new Builder(name));
	}

	public BlazingMetal register() {
		BlazingMetals.ALL.add(this);
		return this;
	}

	public String getMoltenName() {
		return "molten_" + name;
	}

	public ResourceLocation getFluidLocation() {
		return BlazingHot.asResource(getMoltenName());
	}

	public Supplier<Fluid> getFluid() {
		return MultiRegistries.getFluidFromRegistry(getFluidLocation());
	}

	public Supplier<ItemLike> getBucket() {
		return () -> getFluid().get().getBucket();
	}

	public static class Builder {
		private final String name;
		private final List<Mods> mods;
		private final List<BlazingForm> forms;
		private final Map<Fluid, NonNullSupplier<Block>> fluidInteractions;

		private Builder(String name) {
			this.name = name;
			this.mods = new ArrayList<>();
			this.forms = new ArrayList<>();
			this.fluidInteractions = new HashMap<>();
		}

		/**
		 * <p>Defines which mods add this metal. Recipes with this metal will be loaded if any mod in the list is present.</p>
		 * <p><b>Mods should be specified BEFORE forms.</b></p>
		 *
		 * @param mods Mods adding this metal. When multiple mods are present, casting recipes will be loaded for the first present mod.
		 */
		public Builder fromMods(Mods... mods) {
			this.mods.addAll(List.of(mods));
			return this;
		}

		/**
		 * Defines supported forms for this metal. Both metal and form mod conditions must be fulfilled to load a recipe.
		 */
		public Builder withForms(BlazingForm... forms) {
			this.forms.addAll(List.of(forms));
			return this;
		}

		public Builder vanillaForms() {
			return withForms(BlazingForms.INGOT, BlazingForms.NUGGET);
		}

		public Builder createForms() {
			if (mods.contains(Mods.VANILLA) || mods.contains(Mods.CREATE))
				return vanillaForms().withForms(BlazingForms.SHEET.fromMods(Mods.CREATE));
			else return vanillaForms().withForms(BlazingForms.SHEET);
		}

		/**
		 * <p>Defines the fluid interactions. </p>
		 * <p>Never add AllPaletteStoneTypes directly by {@code [...].getBaseBlock()}! You should always add them by <code>() -> [...].getBaseBlock().get()</code></p>
		 * <p>Fabric: remember to update {@link com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl#fluidTags}</p>
		 */
		@SuppressWarnings("JavadocReference")
		public Builder addFluidInteraction(Fluid fluid, NonNullSupplier<Block> block) {
			this.fluidInteractions.put(fluid, block);
			return this;
		}

		public Builder waterCobble() {
			return addFluidInteraction(Fluids.WATER, () -> Blocks.COBBLESTONE);
		}

		/**
		 * Builds and returns the metal.
		 * <ul>
		 *     <li>If no mods were specified, add VANILLA;</li>
		 * </ul>
		 */
		public BlazingMetal build() {
			if (mods.isEmpty()) mods.add(Mods.VANILLA);
			return new BlazingMetal(this);
		}

	}
}
