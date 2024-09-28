package com.dudko.blazinghot.mixin.fabric;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.dudko.blazinghot.registry.BlazingItems;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.simibubi.create.compat.emi.recipes.basin.BasinEmiRecipe;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

@IfModLoaded("emi")
@Mixin(BasinEmiRecipe.class)
public class BasinEmiRecipeMixin {

	@Final
	@Shadow(remap = false)
	private List<EmiIngredient> catalysts;

	@Unique
	private EmiIngredient blazinghot$addBlazeRoll(EmiIngredient ingredient) {
		return EmiIngredient.of(List.of(ingredient, EmiStack.of(BlazingItems.BLAZE_ROLL)));
	}

	@SuppressWarnings("unchecked")
	@ModifyArg(method = "<init>",
			remap = false,
			at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
	private <E> E blazinghot$blazeRollCatalyst(E e) {
		return (E) blazinghot$addBlazeRoll((EmiIngredient) e);
	}

	@ModifyArg(method = "addWidgets",
			remap = false,
			at = @At(value = "INVOKE",
					target = "Ldev/emi/emi/api/widget/WidgetHolder;addSlot(Ldev/emi/emi/api/stack/EmiIngredient;II)Ldev/emi/emi/api/widget/SlotWidget;",
					ordinal = 1))
	private EmiIngredient blazinghot$blazeRollCatalystWidget(EmiIngredient ingredient) {
		return blazinghot$addBlazeRoll(ingredient);
	}


}
