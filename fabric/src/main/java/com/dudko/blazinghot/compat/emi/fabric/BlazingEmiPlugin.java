package com.dudko.blazinghot.compat.emi.fabric;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixerBlockEntity;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingBlocks;
import com.dudko.blazinghot.registry.fabric.BlazingRecipeTypesImpl;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.emi.CreateEmiPlugin;
import com.simibubi.create.compat.emi.DoubleItemIcon;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.simibubi.create.compat.emi.CreateEmiPlugin.doInputsMatch;

public class BlazingEmiPlugin implements EmiPlugin {

    public static final Map<ResourceLocation, EmiRecipeCategory> ALL = new LinkedHashMap<>();

    public static final EmiRecipeCategory BLAZE_MIXING = register("blaze_mixing",
            DoubleItemIcon.of(BlazingBlocks.BLAZE_MIXER.get(), AllBlocks.BASIN.get())), BLAZE_AUTOMATIC_SHAPELESS =
            register("blaze_automatic_shapeless",
                    DoubleItemIcon.of(BlazingBlocks.BLAZE_MIXER.get(), Items.CRAFTING_TABLE)), BLAZE_AUTOMATIC_BREWING =
            register("blaze_automatic_brewing",
                    DoubleItemIcon.of(BlazingBlocks.BLAZE_MIXER.get(), Items.BREWING_STAND));

    @Override
    public void register(EmiRegistry registry) {
        ALL.forEach((id, category) -> registry.addCategory(category));

        registry.addWorkstation(CreateEmiPlugin.MIXING, EmiStack.of(BlazingBlocks.BLAZE_MIXER));
        registry.addWorkstation(CreateEmiPlugin.AUTOMATIC_SHAPELESS, EmiStack.of(BlazingBlocks.BLAZE_MIXER));
        registry.addWorkstation(CreateEmiPlugin.AUTOMATIC_BREWING, EmiStack.of(BlazingBlocks.BLAZE_MIXER));

        registry.addWorkstation(BLAZE_MIXING, EmiStack.of(BlazingBlocks.BLAZE_MIXER));
        registry.addWorkstation(BLAZE_MIXING, EmiStack.of(AllBlocks.BASIN));
        registry.addWorkstation(BLAZE_AUTOMATIC_SHAPELESS, EmiStack.of(BlazingBlocks.BLAZE_MIXER));
        registry.addWorkstation(BLAZE_AUTOMATIC_SHAPELESS, EmiStack.of(AllBlocks.BASIN));
        registry.addWorkstation(BLAZE_AUTOMATIC_BREWING, EmiStack.of(BlazingBlocks.BLAZE_MIXER));
        registry.addWorkstation(BLAZE_AUTOMATIC_BREWING, EmiStack.of(AllBlocks.BASIN));

        RecipeManager manager = registry.getRecipeManager();

        addAll(registry, BlazingRecipeTypesImpl.BLAZE_MIXING, BLAZE_MIXING, BlazeMixingEmiRecipe::new);

        List<MixingRecipe> mixingRecipes = manager.getAllRecipesFor(AllRecipeTypes.MIXING.getType());
        List<BlazeMixingRecipe> blazeMixingRecipes =
                manager.getAllRecipesFor(BlazingRecipeTypesImpl.BLAZE_MIXING.getType());
        outer:
        for (MixingRecipe recipe : mixingRecipes) {
            for (BlazeMixingRecipe blazeMix : blazeMixingRecipes) {
                if (doInputsMatch(recipe, blazeMix)) {
                    continue outer;
                }
            }
            registry.addRecipe(new BlazeMixingEmiRecipe(BLAZE_MIXING, recipe));
        }

        for (CraftingRecipe recipe : manager.getAllRecipesFor(RecipeType.CRAFTING)) {
            if (recipe instanceof ShapelessRecipe
                    && AllConfigs.server().recipes.allowShapelessInMixer.get()
                    && !MechanicalPressBlockEntity.canCompress(recipe)
                    && !AllRecipeTypes.shouldIgnoreInAutomation(recipe)
                    && recipe.getIngredients().size() > 1) {
                registry.addRecipe(new BlazeShapelessEmiRecipe(BLAZE_AUTOMATIC_SHAPELESS,
                        BasinRecipe.convertShapeless(recipe)));
            }
        }

        for (MixingRecipe recipe : PotionMixingRecipes.ALL) {
            if (AllConfigs.server().recipes.allowBrewingInMixer.get()) registry.addRecipe(new BlazeMixingEmiRecipe(
                    BLAZE_AUTOMATIC_BREWING,
                    recipe));
        }
    }

    private static EmiRecipeCategory register(String name, EmiRenderable icon) {
        ResourceLocation id = BlazingHot.asResource(name);
        EmiRecipeCategory category = new EmiRecipeCategory(id, icon);
        ALL.put(id, category);
        return category;
    }

    public static boolean doInputsMatch(Recipe<?> a, Recipe<?> b) {
        if (a instanceof MixingRecipe mixing && b instanceof BlazeMixingRecipe blazeMixing) {
            return BlazeMixerBlockEntity.doInputsMatch(mixing, blazeMixing);
        }

        if (!a.getIngredients().isEmpty() && !b.getIngredients().isEmpty()) {
            ItemStack[] matchingStacks = a.getIngredients().get(0).getItems();
            if (matchingStacks.length != 0) {
                if (b.getIngredients().get(0).test(matchingStacks[0])) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private <T extends Recipe<?>> void addAll(EmiRegistry registry, BlazingRecipeTypesImpl type, EmiRecipeCategory category, BiFunction<EmiRecipeCategory, T, EmiRecipe> constructor) {
        for (T recipe : (List<T>) registry.getRecipeManager().getAllRecipesFor(type.getType())) {
            registry.addRecipe(constructor.apply(category, recipe));
        }
    }

}
