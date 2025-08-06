package com.dudko.blazinghot.gui.ponder;

import java.util.function.BiConsumer;

import com.dudko.blazinghot.BlazingHot;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;

public class BlazingPonderPlugin implements PonderPlugin {

	@Override
	public String getModId() {
		return BlazingHot.ID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		BlazingPonderScenes.register(helper);
	}

	@Override
	public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
		BlazingPonderTags.register(helper);
	}

	public static void providePonderLang(BiConsumer<String, String> consumer) {
		PonderIndex.addPlugin(new CreatePonderPlugin());
		PonderIndex.getLangAccess().provideLang(BlazingHot.ID, consumer);
	}

}
