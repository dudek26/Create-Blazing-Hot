package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.metal.MoltenMetal;
import com.dudko.blazinghot.content.metal.MoltenMetals;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingIngredients.*;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingProcessingRecipeGen.INGOT_COVER;
import static com.dudko.blazinghot.multiloader.MultiFluids.Constants.INGOT;
import static com.dudko.blazinghot.registry.BlazingItems.*;

@SuppressWarnings("unused")
public class SequencedAssemblyRecipeGen extends BlazingRecipeProvider {

    public SequencedAssemblyRecipeGen(PackOutput dataOutput) {
        super(dataOutput);
    }

    GeneratedRecipe
            ENCHANTED_GOLDEN_APPLE =
            enchantedMetalApple(MoltenMetals.GOLD, stellarGoldenApple(), GILDED_STELLAR_GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE),

    ENCHANTED_BLAZE_APPLE = enchantedMetalApple(MoltenMetals.BLAZE_GOLD,
            stellarBlazeApple(),
            BURNING_STELLAR_BLAZE_APPLE,
            BlazingItems.ENCHANTED_BLAZE_APPLE),

    ENCHANTED_IRON_APPLE = enchantedMetalApple(MoltenMetals.IRON,
            stellarIronApple(),
            HEAVY_STELLAR_IRON_APPLE,
            BlazingItems.ENCHANTED_IRON_APPLE),

    ENCHANTED_BRASS_APPLE = enchantedMetalApple(MoltenMetals.BRASS,
            stellarBrassApple(),
            BRASSY_STELLAR_BRASS_APPLE,
            BlazingItems.ENCHANTED_BRASS_APPLE),

    ENCHANTED_ZINC_APPLE = enchantedMetalApple(MoltenMetals.ZINC,
            stellarZincApple(),
            GALVANIZED_STELLAR_ZINC_APPLE,
            BlazingItems.ENCHANTED_ZINC_APPLE),

    ENCHANTED_COPPER_APPLE = enchantedMetalApple(MoltenMetals.COPPER,
            stellarCopperApple(),
            COATED_STELLAR_COPPER_APPLE,
            BlazingItems.ENCHANTED_COPPER_APPLE),

    ENCHANTED_NETHERITE_APPLE =
            create("enchanted_netherite_apple",
                    b -> b.require(netheriteAppleIngredients())
                            .transitionTo(ANCIENT_ENCHANTED_APPLE)
                            .addOutput(BlazingItems.ENCHANTED_NETHERITE_APPLE, 1)
                            .loops(3)
                            .addMeltableStep(FillingRecipe::new, r -> r.convertMeltable().require(moltenNetherite(),
                                    INGOT.platformed() / 4))
                            .addStep(PressingRecipe::new, r -> r));

    GeneratedRecipe
            BLAZE_MIXER =
            create("blaze_mixer",
                    b -> b
                            .require(blazeCasing())
                            .transitionTo(INCOMPLETE_BLAZE_MIXER)
                            .addOutput(BlazingBlocks.BLAZE_MIXER, 1)
                            .loops(1)
                            .addStep(PressingRecipe::new, r -> r)
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(blazeWhisk()))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(cogwheel()))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(extensionPole())));

    private GeneratedRecipe enchantedMetalApple(MoltenMetal metal, ItemLike input, ItemLike transition, ItemLike output) {
        return create("enchanted_" + foodMetalName(metal.name) + "_apple",
                b -> b
                        .require(input)
                        .transitionTo(transition)
                        .addOutput(output, 1)
                        .loops(6)
                        .addMeltableStep(FillingRecipe::new,
                                r -> r.require(metal.fluidTag(), INGOT_COVER))
                        .addStep(DeployerApplicationRecipe::new, r -> r.require(diamond()))
                        .addStep(PressingRecipe::new, r -> r));
    }

    private GeneratedRecipe create(String name, Function<BlazingSequencedAssemblyRecipeBuilder, SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe
                generatedRecipe =
                c -> transform.apply(new BlazingSequencedAssemblyRecipeBuilder(BlazingHot.asResource(name))).build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Blazing Hot Sequenced Assembly Recipes";
    }
}
