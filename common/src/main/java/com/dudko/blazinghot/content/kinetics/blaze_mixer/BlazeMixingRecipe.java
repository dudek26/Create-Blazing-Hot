package com.dudko.blazinghot.content.kinetics.blaze_mixer;

import com.dudko.blazinghot.config.BlazingConfigs;
import com.dudko.blazinghot.multiloader.MultiFluids;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.dudko.blazinghot.multiloader.MultiFluids.fromBucketFraction;

@ParametersAreNonnullByDefault
public class BlazeMixingRecipe extends BasinRecipe {

    @Nullable
    protected FluidIngredient fuelFluid;

    public BlazeMixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(BlazingRecipeTypes.BLAZE_MIXING.get(), params);
    }

    @NotNull
    public FluidIngredient getFuelFluid() {
        if (fuelFluid == null || MultiFluids.getFluidAmount(fuelFluid) <= 0) return FluidIngredient.EMPTY;
        return fuelFluid;
    }

    public NonNullList<ProcessingOutput> getResults() {
        return results;
    }

    /**
     * @apiNote Already platformed.
     */
    public static long getFuelCost(Recipe<?> r) {
        if (r == null) return MultiFluids.Constants.BUCKET.platformed() + 1;

        if (r instanceof MixingRecipe && PotionMixingRecipes.ALL.contains(r))
            return BlazingConfigs.server().blazeBrewingFuelUsage.get();

        else if (r.getType() == AllRecipeTypes.MIXING.getType())
            return durationToFuelCost(((ProcessingRecipe<?>) r).getProcessingDuration());

        else if ((r instanceof CraftingRecipe
                && !(r instanceof ShapedRecipe)
                && BlazingConfigs.server().allowShapelessInBlazeMixer.get()
                && r.getIngredients().size() > 1
                && !MechanicalPressBlockEntity.canCompress(r)) && !AllRecipeTypes.shouldIgnoreInAutomation(r))
            return BlazingConfigs.server().blazeShapelessFuelUsage.get();

        return 0;
    }

    /**
     * @apiNote Already platformed.
     */
    public static long durationToFuelCost(int duration) {
        float recipeSpeed = 1;
        if (duration != 0) {
            recipeSpeed = duration / 100f;
        }
        return Mth.ceil(recipeSpeed * BlazingConfigs.server().blazeMixingFuelUsage.get());
    }

    /**
     * Used in melting recipes' datagen
     */
    @ApiStatus.Internal
    public static long defaultDurationToFuelCost(int duration) {
        float recipeSpeed = 1;
        if (duration != 0) {
            recipeSpeed = duration / 100f;
        }
        return Mth.ceil(recipeSpeed * fromBucketFraction(1, 40));
    }

    @Override
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        if (GsonHelper.isValidNode(json, "blazinghot:fuel")) {
            fuelFluid = FluidIngredient.deserialize(json.get("blazinghot:fuel"));
            platformFuel(fuelFluid);
        }
    }

    private static final FluidIngredient PLACEHOLDER_FUEL = MultiFluids.fluidIngredientFromFluid(Fluids.LAVA, 0);

    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        FluidIngredient fuel = FluidIngredient.read(buffer);
        fuelFluid = MultiFluids.getFluidAmount(fuel) == 0 ? FluidIngredient.EMPTY : fuel;
    }

    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        FluidIngredient fuel = getFuelFluid().equals(FluidIngredient.EMPTY) ? PLACEHOLDER_FUEL : getFuelFluid();
        fuel.write(buffer);
    }

    @ExpectPlatform
    public static void platformFuel(FluidIngredient fuel) {
        throw new AssertionError();
    }
}
