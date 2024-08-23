package com.dudko.blazinghot.gui;

import com.dudko.blazinghot.BlazingHot;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public enum BlazingGuiTextures {

    JEI_SHORT_ARROW_LEFT("jei/widgets", 15, 9);

    public final ResourceLocation location;
    public final int width, height;
    public final int startX, startY;

    BlazingGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    BlazingGuiTextures(String location, int startX, int startY, int width, int height) {
        this(BlazingHot.ID, location, startX, startY, width, height);
    }

    BlazingGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    public void render(GuiGraphics graphics, int x, int y) {
        render(this, graphics, x, y);
    }

    @ExpectPlatform
    public static void render(BlazingGuiTextures texture, GuiGraphics graphics, int x, int y) {
        throw new AssertionError();
    }

}
