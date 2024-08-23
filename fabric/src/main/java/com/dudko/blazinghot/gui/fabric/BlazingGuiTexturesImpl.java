package com.dudko.blazinghot.gui.fabric;

import com.dudko.blazinghot.gui.BlazingGuiTextures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

public class BlazingGuiTexturesImpl {

	@Environment(EnvType.CLIENT)
	public static void render(BlazingGuiTextures texture, GuiGraphics graphics, int x, int y) {
		graphics.blit(texture.location, x, y, texture.startX, texture.startY, texture.width, texture.height);
	}

}
