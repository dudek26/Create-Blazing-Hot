package com.dudko.blazinghot.data.advancement;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.utility.Components;
import com.tterrag.registrate.util.entry.ItemProviderEntry;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/**
 * From {@link com.simibubi.create.foundation.advancement.CreateAdvancement}
 */
public class BlazingAdvancement {

	static final ResourceLocation BACKGROUND = BlazingHot.asResource("textures/gui/advancements.png");
	static final String LANG = "advancement." + BlazingHot.ID + ".";
	static final String SECRET_SUFFIX = "\n\u00A77(Hidden Advancement)";

	private final Advancement.Builder builder;
	private SimpleBlazingTrigger builtinTrigger;
	private BlazingAdvancement parent;

	Advancement datagenResult;

	private final String id;
	private String title;
	private String description;

	BlazingAdvancement(String id, UnaryOperator<Builder> b) {
		this.builder = Advancement.Builder.advancement();
		this.id = id;

		Builder t = new Builder();
		b.apply(t);

		if (!t.externalTrigger) {
			builtinTrigger = BlazingTriggers.addSimple(id + "_builtin");
			builder.addCriterion("0", builtinTrigger.instance());
		}

		if (t.rewards != null) builder.rewards(t.rewards);

		builder.display(t.icon,
				Components.translatable(titleKey()),
				Components.translatable(descriptionKey()).withStyle(s -> s.withColor(0xDBA213)),
				id.equals("root") ? BACKGROUND : null,
				t.type.frame,
				t.type.toast,
				t.type.announce,
				t.type.hide);

		if (t.type == TaskType.SECRET) description += SECRET_SUFFIX;

		BlazingAdvancements.ENTRIES.add(this);
	}

	private String titleKey() {
		return LANG + id;
	}

	private String descriptionKey() {
		return titleKey() + ".desc";
	}

	@SuppressWarnings("DataFlowIssue")
	public boolean isAlreadyAwardedTo(Player player) {
		if (!(player instanceof ServerPlayer sp)) return true;
		Advancement advancement = sp.getServer().getAdvancements().getAdvancement(BlazingHot.asResource(id));
		if (advancement == null) return true;
		return sp.getAdvancements().getOrStartProgress(advancement).isDone();
	}

	public void awardTo(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		if (builtinTrigger == null) throw new UnsupportedOperationException("Advancement "
				+ id
				+ " uses external Triggers, it cannot be awarded directly");
		builtinTrigger.trigger(sp);
	}

	void save(Consumer<Advancement> t) {
		if (parent != null) builder.parent(parent.datagenResult);
		datagenResult = builder.save(t, BlazingHot.asResource(id).toString());
	}

	void provideLang(BiConsumer<String, String> consumer) {
		consumer.accept(titleKey(), title);
		consumer.accept(descriptionKey(), description);
	}

	enum TaskType {

		SILENT(FrameType.TASK, false, false, false),
		NORMAL(FrameType.TASK, true, false, false),
		NOISY(FrameType.TASK, true, true, false),
		EXPERT(FrameType.GOAL, true, true, false),
		SECRET(FrameType.GOAL, true, true, true),
		CHALLENGE(FrameType.CHALLENGE, true, true, false);

		private final FrameType frame;
		private final boolean toast;
		private final boolean announce;
		private final boolean hide;

		TaskType(FrameType frame, boolean toast, boolean announce, boolean hide) {
			this.frame = frame;
			this.toast = toast;
			this.announce = announce;
			this.hide = hide;
		}
	}

	@SuppressWarnings("SameParameterValue")
	class Builder {

		private TaskType type = TaskType.NORMAL;
		private boolean externalTrigger;
		private int keyIndex;
		private ItemStack icon;
		private AdvancementRewards rewards;

		Builder special(TaskType type) {
			this.type = type;
			return this;
		}

		Builder after(BlazingAdvancement other) {
			BlazingAdvancement.this.parent = other;
			return this;
		}

		Builder icon(ItemProviderEntry<?> item) {
			return icon(item.asStack());
		}

		Builder icon(ItemLike item) {
			return icon(new ItemStack(item));
		}

		Builder icon(ItemStack stack) {
			icon = stack;
			return this;
		}

		Builder title(String title) {
			BlazingAdvancement.this.title = title;
			return this;
		}

		Builder description(String description) {
			BlazingAdvancement.this.description = description;
			return this;
		}

		Builder rewards(Function<AdvancementRewards.Builder, AdvancementRewards> b) {
			AdvancementRewards.Builder rewardsBuilder = new AdvancementRewards.Builder();
			this.rewards = b.apply(rewardsBuilder);
			return this;
		}

		Builder whenBlockPlaced(Block block) {
			return externalTrigger(ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
		}

		Builder whenIconCollected() {
			return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(icon.getItem()));
		}

		Builder whenItemCollected(ItemProviderEntry<?> item) {
			return whenItemCollected(item.asStack().getItem());
		}

		Builder whenItemCollected(ItemLike itemProvider) {
			return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider));
		}

		Builder whenItemCollected(TagKey<Item> tag) {
			return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(simpleTagPredicate(tag)));
		}

		Builder whenItemsCollected(ItemLike... items) {
			return whenItemsCollected(List.of(items));
		}

		Builder whenItemsCollected(Collection<ItemLike> items) {
			for (ItemLike i : items) {
				whenItemCollected(i);
			}
			return this;
		}

		Builder whenIconUsed() {
			return whenUsed(icon.getItem());
		}

		Builder whenUsed(ItemLike itemProvider) {
			return externalTrigger(ConsumeItemTrigger.TriggerInstance.usedItem(itemProvider));
		}

		Builder whenUsed(ItemProviderEntry<?> item) {
			return whenUsed(item.asItem());
		}

		Builder whenUsed(TagKey<Item> tag) {
			return externalTrigger(ConsumeItemTrigger.TriggerInstance.usedItem(simpleTagPredicate(tag)));
		}

		Builder whenAllUsed(Collection<ItemLike> items) {
			return whenAllUsed(items.toArray(ItemLike[]::new));
		}

		Builder whenAllUsed(ItemLike[] items) {
			for (ItemLike item : items) {
				whenUsed(item);
			}
			return this;
		}

		Builder whenAllUsed(ItemProviderEntry<?>[] items) {
			for (ItemProviderEntry<?> item : items) {
				whenUsed(item);
			}
			return this;
		}

		Builder awardedForFree() {
			return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}));
		}

		Builder externalTrigger(CriterionTriggerInstance trigger) {
			builder.addCriterion(String.valueOf(keyIndex), trigger);
			externalTrigger = true;
			keyIndex++;
			return this;
		}

		private static ItemPredicate simpleTagPredicate(TagKey<Item> tag) {
			return new ItemPredicate(tag,
					null,
					MinMaxBounds.Ints.ANY,
					MinMaxBounds.Ints.ANY,
					EnchantmentPredicate.NONE,
					EnchantmentPredicate.NONE,
					null,
					NbtPredicate.ANY);
		}

	}

}
