package com.dudko.blazinghot.data.advancement;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.google.common.collect.Sets;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import static com.dudko.blazinghot.data.advancement.BlazingAdvancement.TaskType.*;

public class BlazingAdvancements implements DataProvider {

    public static final List<BlazingAdvancement> ENTRIES = new ArrayList<>();
    public static final BlazingAdvancement START = null,

    ROOT = create("root", b -> b.icon(BlazingItems.BLAZE_GOLD_INGOT)
            .title("Blazing Hot")
            .description("Create: Hell Edition")
            .awardedForFree()
            .special(SILENT)),

    // The beginning

    NETHER_COMPOUND = create("nether_compound", b -> b.icon(BlazingItems.NETHER_COMPOUND)
            .title("Mixing the Nether")
            .description("Obtain a Nether Compound")
            .after(ROOT)
            .whenIconCollected()),

    NETHER_ESSENCE = create("nether_essence", b -> b.icon(BlazingItems.NETHER_ESSENCE)
            .title("Synthetic Hell")
            .description("Haunt Nether Compound into Nether Essence")
            .after(NETHER_COMPOUND)
            .whenIconCollected()),

    MOLTEN_GOLD = create("molten_gold", b -> b.icon(MoltenMetal.GOLD.bucket().get())
            .title("Flowing Riches")
            .description("Obtain a bucket of Molten Gold")
            .after(NETHER_ESSENCE)
            .whenIconCollected()),

    // Blaze Gold

    BLAZE_GOLD = create("blaze_gold", b -> b.icon(BlazingItems.BLAZE_GOLD_INGOT)
            .title("Hot Treasure")
            .description("Mix Molten Gold and Nether Essence together to get Molten Blaze Gold, and then compact it")
            .after(MOLTEN_GOLD)
            .whenIconCollected()),

    // Blaze Gold - Machinery

    BLAZE_CASING = create("blaze_casing", b -> b.icon(BlazingBlocks.BLAZE_CASING)
            .title("The Blaze Age")
            .description("Obtain a Blaze Casing")
            .after(BLAZE_GOLD)),

    BLAZE_MIXING = create("blaze_mixing", b -> b.icon(BlazingBlocks.BLAZE_MIXER)
            .title("A New Era of Mixing")
            .description("Use the Blaze Mixer to process a recipe")
            .after(BLAZE_CASING)),

    // Metal Food

    METAL_CARROT = create("metal_carrot", b -> b.icon(BlazingItems.IRON_CARROT)
            .title("Shiny Snacks")
            .description("Obtain a new Metal Carrot")
            .after(MOLTEN_GOLD)
            .whenUsed(BlazingTags.Items.METAL_CARROTS.tag)),

    METAL_APPLE = create("metal_apple", b -> b.icon(BlazingItems.IRON_APPLE)
            .title("Rich Apples")
            .description("Obtain any of the new Metal Apples")
            .after(METAL_CARROT)
            .whenUsed(BlazingTags.Items.METAL_APPLES.tag)),

    STELLAR_METAL_APPLE = create("stellar_metal_apple", b -> b.icon(BlazingItems.STELLAR_IRON_APPLE)
            .title("Fruits from the Stars")
            .description("Obtain any Stellar Metal Apple")
            .after(METAL_APPLE)
            .whenUsed(BlazingTags.Items.STELLAR_METAL_APPLES.tag)),

    ENCHANTED_METAL_APPLE = create("enchanted_metal_apple", b -> b.icon(BlazingItems.ENCHANTED_IRON_APPLE)
            .title("Magic Food")
            .description("Obtain any of the new Enchanted Metal Apples")
            .after(STELLAR_METAL_APPLE)
            .whenUsed(BlazingTags.Items.ENCHANTED_METAL_APPLES.tag)),

    ALL_METAL_FOOD = create("all_metal_food", b -> b.icon(Items.ENCHANTED_GOLDEN_APPLE)
            .title("A Heavy Diet")
            .description("Eat every metal food")
            .after(ENCHANTED_METAL_APPLE)
            .special(CHALLENGE)
            .whenAllUsed(BlazingItems.METAL_FOOD)),

    //
    END = null;

    private static BlazingAdvancement create(String id, UnaryOperator<BlazingAdvancement.Builder> b) {
        return new BlazingAdvancement(id, b);
    }

    // Datagen

    private final PackOutput output;

    public BlazingAdvancements(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            ResourceLocation id = advancement.getId();
            if (!set.add(id))
                throw new IllegalStateException("Duplicate advancement " + id);
            Path path = pathProvider.json(id);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
                    .serializeToJson(), path));
        };

        for (BlazingAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Create's Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (BlazingAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }

    public static void register() {}

}
