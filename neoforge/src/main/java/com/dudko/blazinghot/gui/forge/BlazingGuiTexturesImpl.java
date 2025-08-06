package com.dudko.blazinghot.gui.forge;

import com.dudko.blazinghot.gui.BlazingGuiTextures;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlazingGuiTexturesImpl {

	@OnlyIn(Dist.CLIENT)
	public static void render(BlazingGuiTextures texture, GuiGraphics graphics, int x, int y) {
		graphics.blit(texture.location, x, y, texture.startX, texture.startY, texture.width, texture.height);
	}

}
