package com.dudko.blazinghot.config;

import net.createmod.catnip.config.ConfigBase;

public class CServer extends ConfigBase {

	public final CRecipes recipes = nested(0, CRecipes::new, Comments.recipes);
	public final CStress stressValues = nested(0, CStress::new, Comments.stressValues);

	@Override
	public String getName() {
		return "server";
	}

	private static class Comments {
		static String recipes = "Adjust mechanics related to recipes";
		static String stressValues = "Fine-tune stress impact of Create: Blazing Hot's machinery";
	}
}
