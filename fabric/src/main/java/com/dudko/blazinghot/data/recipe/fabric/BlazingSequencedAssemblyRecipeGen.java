package com.dudko.blazinghot.data.recipe.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.CommonTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

import static com.dudko.blazinghot.data.recipe.fabric.BlazingProcessingRecipeGen.BUCKET;
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
                            .require(STELLAR_GOLDEN_APPLE)
                            .transitionTo(GILDED_STELLAR_GOLDEN_APPLE)
                            .addOutput(Items.ENCHANTED_GOLDEN_APPLE, 1)
                            .loops(4)
                            .addStep(FillingRecipe::new, r -> r.require(CommonTags.Fluids.MOLTEN_GOLD.internal, BUCKET))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.DIAMOND))
                            .addStep(PressingRecipe::new, r -> r)),
            ENCHANTED_BLAZE_APPLE =
                    create("enchanted_blaze_apple",
                            b -> b
                                    .require(STELLAR_BLAZE_APPLE)
                                    .transitionTo(BURNING_STELLAR_BLAZE_APPLE)
                                    .addOutput(BlazingItems.ENCHANTED_BLAZE_APPLE, 1)
                                    .loops(4)
                                    .addStep(FillingRecipe::new,
                                            r -> r.require(CommonTags.Fluids.MOLTEN_BLAZE_GOLD.internal, BUCKET))
                                    .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.DIAMOND))
                                    .addStep(PressingRecipe::new, r -> r)),
            ENCHANTED_IRON_APPLE =
                    create("enchanted_iron_apple",
                            b -> b
                                    .require(STELLAR_IRON_APPLE)
                                    .transitionTo(HEAVY_STELLAR_IRON_APPLE)
                                    .addOutput(BlazingItems.ENCHANTED_IRON_APPLE, 1)
                                    .loops(4)
                                    .addStep(FillingRecipe::new,
                                            r -> r.require(CommonTags.Fluids.MOLTEN_IRON.internal, BUCKET))
                                    .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.DIAMOND))
                                    .addStep(PressingRecipe::new, r -> r));

    GeneratedRecipe
            BLAZE_MIXER =
            create("blaze_mixer",
                    b -> b
                            .require(BlazingBlocks.BLAZE_CASING)
                            .transitionTo(INCOMPLETE_BLAZE_MIXER)
                            .addOutput(BlazingBlocks.BLAZE_MIXER, 1)
                            .loops(1)
                            .addStep(PressingRecipe::new, r -> r)
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(BLAZE_WHISK))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(AllBlocks.COGWHEEL))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(AllBlocks.PISTON_EXTENSION_POLE)));

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
