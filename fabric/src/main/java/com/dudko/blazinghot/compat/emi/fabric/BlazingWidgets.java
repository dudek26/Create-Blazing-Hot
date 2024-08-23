package com.dudko.blazinghot.compat.emi.fabric;

import com.dudko.blazinghot.gui.BlazingGuiTextures;

import dev.emi.emi.api.widget.TextureWidget;
import dev.emi.emi.api.widget.WidgetHolder;

public class BlazingWidgets {

	public static TextureWidget addTexture(WidgetHolder widgets, BlazingGuiTextures texture, int x, int y) {
		return widgets.addTexture(texture.location,
				x,
				y,
				texture.width,
				texture.height,
				texture.startX,
				texture.startY);
	}

}
