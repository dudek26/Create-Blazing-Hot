package com.dudko.blazinghot.data.recipe;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingTags;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

import static com.dudko.blazinghot.registry.BlazingItems.*;

public class BlazingSequencedAssemblyRecipeGen extends BlazingRecipeProvider {

    public BlazingSequencedAssemblyRecipeGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    GeneratedRecipe
            ENCHANTED_GOLDEN_APPLE =
            create("enchanted_golden_apple", b -> b
                    .require(STELLAR_GOLDEN_APPLE)
                    .transitionTo(GILDED_STELLAR_GOLDEN_APPLE)
                    .addOutput(Items.ENCHANTED_GOLDEN_APPLE, 1)
                    .loops(4)
                    .addStep(FillingRecipe::new, r -> r.require(BlazingTags.Fluids.MOLTEN_GOLD.tag, 81000))
                    .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.DIAMOND))
                    .addStep(PressingRecipe::new, r -> r)),
            ENCHANTED_BLAZE_APPLE =
                    create("enchanted_blaze_apple", b -> b
                            .require(STELLAR_BLAZE_APPLE)
                            .transitionTo(BURNING_STELLAR_BLAZE_APPLE)
                            .addOutput(BlazingItems.ENCHANTED_BLAZE_APPLE, 1)
                            .loops(4)
                            .addStep(FillingRecipe::new,
                                     r -> r.require(BlazingTags.Fluids.MOLTEN_BLAZE_GOLD.tag, 81000))
                            .addStep(DeployerApplicationRecipe::new, r -> r.require(Items.DIAMOND))
                            .addStep(PressingRecipe::new, r -> r));

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
