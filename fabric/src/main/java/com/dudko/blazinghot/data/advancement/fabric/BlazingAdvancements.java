package com.dudko.blazinghot.data.advancement.fabric;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingItems;
import com.google.common.collect.Sets;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;

import static com.dudko.blazinghot.data.advancement.fabric.BlazingAdvancement.TaskType.*;

public class BlazingAdvancements implements DataProvider {

    public static final List<BlazingAdvancement> ENTRIES = new ArrayList<>();
    public static final BlazingAdvancement START = null,

    ROOT = create("root", b -> b.icon(BlazingItems.BLAZE_GOLD_INGOT)
            .title("Create: Hell Edition")
            .description("Don't burn yourself!")
            .awardedForFree()
            .special(SILENT)),

    //

    NETHER_ESSENCE = create("nether_essence", b -> b.icon(BlazingItems.NETHER_ESSENCE)
            .title("Synthetic Hell")
            .description("Haunt Nether Compound into Nether Essence")
            .after(ROOT)
            .whenIconCollected()),

    MOLTEN_GOLD = create("molten_gold", b -> b.icon(MoltenMetal.GOLD.bucket().get())
            .title("Flowing Riches")
            .description("Obtain a bucket of Molten Gold")
            .after(NETHER_ESSENCE)
            .whenIconCollected()),

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
