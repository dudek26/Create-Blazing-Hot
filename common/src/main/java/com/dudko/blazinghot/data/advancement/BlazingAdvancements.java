package com.dudko.blazinghot.data.advancement;

import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.google.common.collect.Sets;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.dudko.blazinghot.content.metal.MoltenMetal.allBuckets;
import static com.dudko.blazinghot.data.advancement.BlazingAdvancement.TaskType.*;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlazingAdvancements implements DataProvider {

    public static final List<BlazingAdvancement> ENTRIES = new ArrayList<>();
    public static final BlazingAdvancement START = null,

    ROOT = create("root", b -> b.icon(BlazingItems.BLAZE_WHISK)
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
            .description("Melt Gold in Mixer")
            .special(NOISY)
            .after(NETHER_ESSENCE)),

    // All Molten Metals

    ALL_MOLTEN_METALS = create("all_molten_metals", b -> b.icon(MoltenMetal.NETHERITE.bucket().get())
            .title("Tinkers' Construct")
            .description("Obtain a bucket of every non-compat molten metal.")
            .after(MOLTEN_GOLD)
            .special(CHALLENGE)
            .rewards(r -> r.addExperience(100).build())
            .whenItemsCollected(allBuckets(false))),

    // Metal Food

    METAL_APPLE_SPOUT = create("metal_apple_spout_0000", b -> b.icon(BlazingItems.IRON_APPLE)
            .title("Budget Snacks")
            .description("Obtain any Metal Apple by filling regular Apple in Spout")
            .after(MOLTEN_GOLD)),

    GOLDEN_APPLE_FACTORY = create("golden_apple_factory", b -> b.icon(Items.GOLDEN_APPLE)
            .title("Gold Addiction")
            .description("Produce 64 Golden Apples with one Spout")
            .special(EXPERT)
            .after(METAL_APPLE_SPOUT)),

    ALL_METAL_FOOD = create("all_metal_food", b -> b.icon(Items.ENCHANTED_GOLDEN_APPLE)
            .title("A Heavy Diet")
            .description("Eat every Metal Apple and Carrot")
            .after(GOLDEN_APPLE_FACTORY)
            .special(CHALLENGE)
            .rewards(r -> r.addExperience(100).build())
            .whenAllUsed(BlazingItems.METAL_FOOD)),

    EXTINGUISHING_FOOD_SAVE = create("extinguishing_food_save", b -> b.icon(BlazingItems.BLAZE_CARROT)
            .title("Last Resort")
            .description(
                    "Save yourself from burning down by eating any extinguishing food when under 2 hearts of health.")
            .after(ALL_METAL_FOOD)
            .special(SECRET)),

    // Blaze Gold

    MOLTEN_BLAZE_GOLD = create("molten_blaze_gold_00", b -> b.icon(MoltenMetal.BLAZE_GOLD.bucket().get())
            .title("Fake Alloys")
            .description("Mix Molten Gold and Nether Essence together to obtain Molten Blaze Gold")
            .after(MOLTEN_GOLD)),

    BLAZE_GOLD = create("blaze_gold", b -> b.icon(BlazingItems.BLAZE_GOLD_INGOT)
            .title("Hot Treasure")
            .description("Compact Molten Blaze Gold in Basin to obtain a Blaze Gold Ingot")
            .after(MOLTEN_BLAZE_GOLD)
            .whenIconCollected()),

    BLAZE_CASING = create("blaze_casing", b -> b.icon(BlazingBlocks.BLAZE_CASING)
            .title("The Blaze Age")
            .description("Use Blaze Gold Sheets to upgrade your Copper Casings")
            .special(NOISY)
            .after(BLAZE_GOLD)),

    BLAZE_MIXER = create("blaze_mixer", b -> b.icon(BlazingBlocks.BLAZE_MIXER)
            .title("New Era of Mixing")
            .description("Combine or melt ingredients in a Blaze Mixer")
            .after(BLAZE_CASING)),

    // Blaze Gold - Combat

    BLAZE_ARROW = create("blaze_arrow", b -> b.icon(BlazingItems.BLAZE_ARROW)
            .title("Power of the Nether")
            .description("Shoot something with a Blaze Arrow in the Nether")
            .special(NOISY)
            .after(BLAZE_MIXER)),

    BLAZE_ARROW_INTERDIMENSIONAL = create("blaze_arrow_interdimensional", b -> b.icon(BlazingItems.BLAZE_ARROW)
            .title("Interdimensional Sniper")
            .description("Kill an enemy in the Nether with a Blaze Arrow shot in the Overworld")
            .special(CHALLENGE)
            .after(BLAZE_ARROW)),

    // Blaze Gold - Machines

    MODERN_LAMP = create("modern_lamp", b -> b.icon(BlazingBlocks.MODERN_LAMP_BLOCKS.get(DyeColor.WHITE))
            .title("Modern Technology")
            .description("Manually activate any Modern Lamp")
            .after(BLAZE_MIXER)),

    // Blaze Mixer - Expert

    BLAZE_MIXER_MAX = create("blaze_mixer_max", b -> b.icon(BlazingBlocks.BLAZE_MIXER)
            .title("Fast and Furious")
            .description("Run a fully fueled Blaze Mixer at max speed")
            .after(BLAZE_MIXER)
            .special(EXPERT)),

    ANCIENT_DEBRIS_MELTING = create("ancient_debris_melting", b -> b.icon(Items.ANCIENT_DEBRIS)
            .title("Debris Utilisation")
            .description("Melt 15 Ancient Debris in a single Blaze Mixer")
            .after(BLAZE_MIXER_MAX)
            .special(EXPERT)),

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
        return "Blazing Hot's Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (BlazingAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }

    public static void register() {
    }

}
