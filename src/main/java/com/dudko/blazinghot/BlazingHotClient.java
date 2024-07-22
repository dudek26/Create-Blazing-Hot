package com.dudko.blazinghot;

import com.dudko.blazinghot.content.entity.renderer.BlazeArrowRenderer;
import com.dudko.blazinghot.registry.BlazingEntityTypes;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Objects;

public class BlazingHotClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(BlazingEntityTypes.BLAZE_ARROW.get(), BlazeArrowRenderer::new);

        ItemTooltipCallback.EVENT.register(((stack, context, lines) -> {

            if (Objects.equals(stack.getItem().getCreatorModId(stack), BlazingHot.ID)) {
                Item item = stack.getItem();
                if (item.getFoodProperties() != null) {
                    item
                            .getFoodProperties()
                            .getEffects()
                            .stream()
                            .map(Pair::getFirst)
                            .forEach(mobEffectInstance -> addEffectTooltip(lines, mobEffectInstance));
                }
            }

        }));
    }

    private void addEffectTooltip(List<Component> lines, MobEffectInstance effect) {
        Component
                amplifier =
                effect.getAmplifier() == 0 ?
                Component.empty() :
                Component.translatable("potion.potency." + effect.getAmplifier()).append(" ");
        lines.add(Component
                          .translatable(effect.getDescriptionId())
                          .append(" ")
                          .append(amplifier)
                          .append("(")
                          .append(MobEffectUtil.formatDuration(effect, 1))
                          .append(")")
                          .withStyle(effect.getEffect().getCategory().getTooltipFormatting()));


    }
}
