package com.dudko.blazinghot.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.dudko.blazinghot.registry.BlazingItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.simibubi.create.compat.jei.category.BasinCategory;

import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import net.minecraft.world.item.ItemStack;

@IfModLoaded("jei")
@Mixin(BasinCategory.class)
public class JEIBasinCategoryMixin {
	@WrapOperation(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
			remap = false,
			at = @At(value = "INVOKE",
					target = "Lmezz/jei/api/gui/builder/IRecipeSlotBuilder;addItemStack(Lnet/minecraft/world/item/ItemStack;)Lmezz/jei/api/gui/builder/IIngredientAcceptor;",
					ordinal = 2))
	private IIngredientAcceptor<?> blazinghot$addBlazeRollCatalyst(IRecipeSlotBuilder instance, ItemStack itemStack, Operation<IIngredientAcceptor<?>> original) {
		return original.call(instance, itemStack).addItemStack(BlazingItems.BLAZE_ROLL.asStack());
	}

}
