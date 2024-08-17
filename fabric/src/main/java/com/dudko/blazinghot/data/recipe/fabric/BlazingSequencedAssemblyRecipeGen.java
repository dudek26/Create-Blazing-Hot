package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

import static com.dudko.blazinghot.content.metal.MoltenMetal.*;
import static com.dudko.blazinghot.data.recipe.fabric.BlazingProcessingRecipeGen.*;
import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.*;
import static com.dudko.blazinghot.data.recipe.fabric.Ingredients.stellarGoldenApple;
import static com.dudko.blazinghot.registry.BlazingItems.*;

@SuppressWarnings("unused")
public class BlazingSequencedAssemblyRecipeGen extends BlazingRecipeProvider {

    public BlazingSequencedAssemblyRecipeGen(PackOutput dataOutput) {
        super(dataOutput);
    }

    GeneratedRecipe
            ENCHANTED_GOLDEN_APPLE =
            create("enchanted_golden_apple",
                    b -> b
                            .require(stellarGoldenApple())
                            .transitionTo(GILDED_STELLAR_GOLDEN_APPLE)
                            .addOutput(Items.ENCHANTED_GOLDEN_APPLE, 1)
                            .loops(6)
                            .addStep(FillingRecipe::new, r -> custom(r).blazinghot$convertMeltables().blazinghot$finish().require(GOLD.fluidTag(), INGOT_COVER))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(diamond()))
                            .addStep(PressingRecipe::new, r -> r)),
            ENCHANTED_BLAZE_APPLE =
                    create("enchanted_blaze_apple",
                            b -> b
                                    .require(stellarBlazeApple())
                                    .transitionTo(BURNING_STELLAR_BLAZE_APPLE)
                                    .addOutput(BlazingItems.ENCHANTED_BLAZE_APPLE, 1)
                                    .loops(6)
                                    .addStep(FillingRecipe::new,
                                            r -> custom(r).blazinghot$convertMeltables().blazinghot$finish().require(
                                                    BLAZE_GOLD.fluidTag(), INGOT_COVER))
                                    .addStep(DeployerApplicationRecipe::new, r -> r.require(diamond()))
                                    .addStep(PressingRecipe::new, r -> r)),
            ENCHANTED_IRON_APPLE =
                    create("enchanted_iron_apple",
                            b -> b
                                    .require(stellarIronApple())
                                    .transitionTo(HEAVY_STELLAR_IRON_APPLE)
                                    .addOutput(BlazingItems.ENCHANTED_IRON_APPLE, 1)
                                    .loops(6)
                                    .addStep(FillingRecipe::new,
                                            r -> custom(r).blazinghot$convertMeltables().blazinghot$finish().require(IRON.fluidTag(), INGOT_COVER))
                                    .addStep(DeployerApplicationRecipe::new, r -> r.require(diamond()))
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

    private GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe
                generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(BlazingHot.asResource(name))).build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public @NotNull String getName() {
        return "Blazing Hot Sequenced Assembly Recipes";
    }
}
