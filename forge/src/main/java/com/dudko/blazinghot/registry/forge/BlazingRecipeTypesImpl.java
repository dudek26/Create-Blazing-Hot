package com.dudko.blazinghot.registry.forge;

import com.dudko.blazinghot.BlazingHot;
import com.dudko.blazinghot.content.kinetics.blaze_mixer.BlazeMixingRecipe;
import com.dudko.blazinghot.registry.BlazingRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public enum BlazingRecipeTypesImpl implements IRecipeTypeInfo {

    BLAZE_MIXING(BlazeMixingRecipe::new);

    public static void platformRegister(IEventBus modEventBus) {
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    BlazingRecipeTypesImpl(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = BlazingHot.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
        type = typeObject;
    }

    BlazingRecipeTypesImpl(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }

    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager().getRecipeFor(getType(), inv, world);
    }

    public static <T extends RecipeType<?>> T getType(BlazingRecipeTypes recipe) {
        return switch (recipe) {
            case BLAZE_MIXING -> BLAZE_MIXING.getType();
        };
    }

    public static IRecipeTypeInfo get(BlazingRecipeTypes recipe) {
        return switch (recipe) {
            case BLAZE_MIXING -> BLAZE_MIXING;
        };
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>>
                SERIALIZER_REGISTER =
                DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BlazingHot.ID);
        private static final DeferredRegister<RecipeType<?>>
                TYPE_REGISTER =
                DeferredRegister.create(Registries.RECIPE_TYPE, BlazingHot.ID);
    }

}
