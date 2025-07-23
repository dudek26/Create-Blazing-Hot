package com.dudko.blazinghot.content.metal;

import static com.dudko.blazinghot.compat.Mods.CREATE_ADDITIONS;
import static com.dudko.blazinghot.compat.Mods.VANILLA;
import static com.dudko.blazinghot.registry.CommonTags.itemTagOf;
import static com.dudko.blazinghot.util.LangUtil.titleCaseConversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.compat.Mods;
import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.data.advancement.BlazingAdvancements;
import com.dudko.blazinghot.data.conditions.DefaultLoadConditions;
import com.dudko.blazinghot.data.conditions.LoadCondition;
import com.dudko.blazinghot.multiloader.MultiRegistries;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.CommonTags;
import com.dudko.blazinghot.util.ListUtil;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.createmod.catnip.data.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("unused")
public class MoltenMetal {

	public final String name;
	public final Mods mod;
	public final Map<Forms, Mods> supportedForms;
	public final Map<Forms, Mods> optionalForms;
	public final Map<Forms, Mods> compatForms;
	public final boolean ignoreTagGen;
	public final boolean mechanicalMixerMeltable;

	public final Map<Fluid, NonNullSupplier<Block>> fluidInteractions;

	public final Map<Forms, Mods> customForms;
	public final Pair<ResourceLocation, MultiAmount> compactingOverride;
	public final HashMap<Forms, Pair<ResourceLocation, MultiAmount>> castingOverrides;

	MoltenMetal(String name, Mods mod, Map<Forms, Mods> supportedForms, Map<Forms, Mods> optionalForms, Map<Forms, Mods> compatForms, boolean ignoreTagGen, boolean mechanicalMixerMeltable, Map<Fluid, NonNullSupplier<Block>> fluidInteractions, Map<Forms, Mods> customForms, Pair<ResourceLocation, MultiAmount> compactingOverride, HashMap<Forms, Pair<ResourceLocation, MultiAmount>> castingOverrides) {
		this.name = name;
		this.mod = mod;
		this.optionalForms = optionalForms;
		this.compatForms = compatForms;
		this.supportedForms = supportedForms;
		this.mechanicalMixerMeltable = mechanicalMixerMeltable;
		this.fluidInteractions = fluidInteractions;
		this.customForms = customForms;
		this.compactingOverride = compactingOverride;
		this.ignoreTagGen = ignoreTagGen;
		this.castingOverrides = castingOverrides;
	}

	/**
	 * Automatically registers fluid, fluid interactions and adds this metal to the data generator
	 */
	public MoltenMetal register() {
		MoltenMetals.ALL.add(this);
		return this;
	}

	public static Builder builder(String name) {
		return new Builder(name);
	}

	/**
	 * @deprecated use {@link MoltenMetal#getLocation(Forms)}
	 */
	@Deprecated
	public ResourceLocation ingotLocation() {
		if (compactingOverride != null) return compactingOverride.getFirst();
		return Forms.INGOT.resourceLocation(this);
	}

	public ResourceLocation getLocation(Forms form) {
		return getLocation(form, mod);
	}

	public ResourceLocation getLocation(Forms form, Mods mod) {
		Pair<ResourceLocation, MultiAmount> override = castingOverrides.get(form);
		if (override != null) return override.getFirst();
		return form.resourceLocation(this, mod);
	}

	public MultiAmount getAmount(Forms form) {
		Pair<ResourceLocation, MultiAmount> override = castingOverrides.get(form);
		if (override != null) return override.getSecond();
		return form.amount;
	}

	@Deprecated
	public Pair<ItemLike, MultiAmount> compactingResult() {
		return Pair.of(MultiRegistries.getItemFromRegistry(ingotLocation()).get(),
				(compactingOverride != null ? compactingOverride.getSecond() : MultiAmount.INGOT));
	}

	public ResourceLocation fluidLocation() {
		return BlazingHot.asResource(moltenName());
	}

	public Supplier<Fluid> fluid() {
		return MultiRegistries.getFluidFromRegistry(fluidLocation());
	}

	public Supplier<ItemLike> bucket() {
		return () -> fluid().get().getBucket();
	}

	/**
	 * @see BlazingAdvancements#ALL_MOLTEN_METALS
	 */
	public static Set<ItemLike> allBuckets(boolean includeCompat) {
		return MoltenMetals.ALL
				.stream()
				.filter(m -> m.mod.alwaysIncluded || includeCompat)
				.map(m -> m.bucket().get())
				.collect(Collectors.toSet());
	}

	public TagKey<Fluid> fluidTag() {
		return CommonTags.fluidTagOf(moltenName(), CommonTags.Namespace.platform());
	}

	public String moltenName() {
		return "molten_" + name;
	}

	public List<Forms> nonCustomForms() {
		List<Forms> all = new ArrayList<>();
		ListUtil.addIfAbsent(all, supportedForms());
		ListUtil.addIfAbsent(all, compatForms.keySet());
		ListUtil.addIfAbsent(all, optionalForms.keySet());
		return all;
	}

	public List<Forms> supportedForms() {
		return new ArrayList<>(supportedForms.keySet());
	}

	public List<Forms> customForms() {
		return new ArrayList<>(customForms.keySet());
	}

	public Map<Fluid, NonNullSupplier<Block>> getFluidInteractions() {
		HashMap<Fluid, NonNullSupplier<Block>> interactions = new HashMap<>(fluidInteractions);
		if (!interactions.containsKey(Fluids.WATER)) interactions.put(Fluids.WATER, () -> Blocks.COBBLESTONE);
		return interactions;
	}

	public List<LoadCondition<?>> getLoadConditions() {
		List<LoadCondition<?>> conditions = new ArrayList<>();
		if (!mod.alwaysIncluded) conditions.add(mod.asLoadCondition());
		return conditions;
	}

	public List<LoadCondition<?>> getLoadConditions(Forms form, Mods formMod) {
		List<LoadCondition<?>> conditions = getLoadConditions();
		conditions.add(DefaultLoadConditions.anyModLoaded(formMod.id));
		return conditions;
	}

	@SuppressWarnings("JavadocReference")
	public static class Builder {

		private final String name;
		private Mods mod;
		private final Map<Forms, Mods> supportedForms = new HashMap<>();
		private final Map<Forms, Mods> optionalForms = new HashMap<>();
		private final HashMap<Forms, Mods> compatForms = new HashMap<>();
		private boolean ignoreTagDatagen;
		private final Map<Forms, Mods> customForms = new HashMap<>();
		private boolean mechanicalMixerMeltable = true;
		private final HashMap<Fluid, NonNullSupplier<Block>> fluidInteractions = new HashMap<>();

		private Pair<ResourceLocation, MultiAmount> castingOverride;
		private final HashMap<Forms, Pair<ResourceLocation, MultiAmount>> castingOverrides = new HashMap<>();

		protected Builder(String name) {
			this.name = name;
		}

		/**
		 * Set the mod that adds the metal to the game. If unset, defaults to Vanilla
		 */
		public Builder mod(Mods mod) {
			this.mod = mod;
			return this;
		}

		/**
		 * <p>Defines the fluid interactions. If no interaction with water is specified, it will default to Cobblestone.</p>
		 * <p>Never add AllPaletteStoneTypes directly by {@code [...].getBaseBlock()}! You should always add them by <code>() -> [...].getBaseBlock().get()</code></p>
		 * <p>Fabric: remember to update {@link com.dudko.blazinghot.registry.fabric.BlazingFluidsImpl#fluidTags}</p>
		 */
		public Builder addFluidInteraction(Fluid fluid, NonNullSupplier<Block> block) {
			this.fluidInteractions.put(fluid, block);
			return this;
		}


		/**
		 * For forms added by the main mod of the metal
		 */
		public Builder coreForms(Forms... supportedForms) {
			for (Forms form : supportedForms) {
				this.supportedForms.put(form, mod);
			}
			return this;
		}

		/**
		 * For forms added by Create
		 */
		public Builder createForms(Forms... createForms) {
			for (Forms form : createForms) {
				this.supportedForms.put(form, Mods.CREATE);
			}
			return this;
		}

		/**
		 * For tag-based forms that are not present in Vanilla or create.
		 */
		public Builder optionalForm(Forms form, Mods mod) {
			this.optionalForms.put(form, mod);
			return this;
		}

		/**
		 * For item-based forms that are added by a different mod than the main one.
		 */
		public Builder compatForm(Forms form, Mods mod) {
			this.compatForms.put(form, mod);
			return this;
		}

		public Builder customForm(String name, ResourceLocation item, MultiAmount amount, int processingTime, long fuelCost, boolean mechanicalMixerMeltable, @Nullable Molds.Mold mold, Mods mod) {
			this.customForms.put(Forms.custom(name,
					item,
					amount,
					processingTime,
					fuelCost,
					mechanicalMixerMeltable,
					mold), mod);
			return this;
		}

		// Shortcuts

		/**
		 * For Vanilla and Create metals except Zinc: Ingot, Nugget, Plate + Create Crafts & Additions Rod compat <br>
		 */
		public Builder createForms() {
			return basicAndPlateForms().compatForm(Forms.ROD, CREATE_ADDITIONS);
		}

		/**
		 * For metals that also have a plate form
		 */
		public Builder basicAndPlateForms() {
			return basicForms().createForms(Forms.PLATE);
		}

		/**
		 * For metals that only have ingot and nugget forms
		 */
		public Builder basicForms() {
			return coreForms(Forms.INGOT, Forms.NUGGET);
		}

		/**
		 * If compacting the molten metal should return something else than an item that starts with the metal's name and ends with <code>_ingot</code>
		 *
		 * @param item   Resource location of the item to return
		 * @param amount Amount of fluid required to compact one item
		 * @deprecated use {@link Builder#castingOverride(Forms, ResourceLocation, MultiAmount)}
		 */
		@Deprecated
		public Builder compactingOverride(ResourceLocation item, MultiAmount amount) {
			this.castingOverride = Pair.of(item, amount);
			return this;
		}

		/**
		 * If casting the molten metal should return something else than an item that starts with the metal's name and ends with form name
		 *
		 * @param form   Form to override the name of
		 * @param item   Resource location of the item to return
		 * @param amount Amount of fluid required to compact one item
		 */
		public Builder castingOverride(Forms form, ResourceLocation item, MultiAmount amount) {
			this.castingOverrides.put(form, Pair.of(item, amount));
			return this;
		}

		/**
		 * If called, the data generator will not automatically create tags for all supported forms (including the internal tag). Use when the metal doesn't have common tags (like Andesite Alloy)
		 */
		public Builder ignoreTagDatagen() {
			this.ignoreTagDatagen = true;
			return this;
		}

		/**
		 * Disables melting in Mechanical Mixer for ALL forms of this metal.
		 */
		public Builder disableMechanicalMixing() {
			this.mechanicalMixerMeltable = false;
			return this;
		}

		public MoltenMetal build() {
			return new MoltenMetal(name,
					mod == null ? VANILLA : mod,
					supportedForms,
					optionalForms,
					compatForms,
					ignoreTagDatagen,
					mechanicalMixerMeltable,
					fluidInteractions,
					customForms,
					castingOverride,
					castingOverrides);
		}

		/**
		 * Shortcut for building and registering the Molten Metal
		 */
		public MoltenMetal register() {
			return build().register();
		}
	}

	public static void provideLangEntries(BiConsumer<String, String> consumer) {

		for (MoltenMetal metal : MoltenMetals.ALL) {
			for (CommonTags.Namespace namespace : CommonTags.Namespace.values()) {
				ResourceLocation fluidLoc = metal.fluidTag().location();
				consumer.accept("tag.fluid." + namespace.namespace + "." + fluidLoc.getPath().replace('/', '.'),
						titleCaseConversion(metal.fluidLocation().getPath().replace('_', ' ')));

				for (Forms form : metal.nonCustomForms()) {
					TagKey<Item> tag = itemTagOf(form.tagFolder, metal.name, namespace);
					ResourceLocation loc = tag.location();
					consumer.accept("tag.item." + namespace.namespace + "." + loc.getPath().replace('/', '.'),
							titleCaseConversion((metal.name + ' ' + form.tagFolder).replace('_', ' ')));
				}
			}
		}

	}

}
