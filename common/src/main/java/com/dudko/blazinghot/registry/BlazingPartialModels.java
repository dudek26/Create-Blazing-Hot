package com.dudko.blazinghot.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.dudko.blazinghot.BlazingHot;
import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.world.item.DyeColor;

public class BlazingPartialModels {

	public static final PartialModel BLAZE_MIXER_HEAD = block("blaze_mixer/head");
	public static final PartialModel BLAZE_MIXER_POLE = block("blaze_mixer/pole");
	public static final PartialModel SHAFTLESS_CRIMSON_COGWHEEL = block("crimson_cogwheel_shaftless");

	private static final List<LampModel> MODERN_LAMPS = ModernLampTypes.registerAll();

	public static PartialModel getModernLamp(DyeColor color, boolean powered, ModernLampTypes type) {
		return MODERN_LAMPS
				.stream()
				.filter(m -> m.color == color && m.powered == powered && m.type == type)
				.map(m -> m.model)
				.findFirst()
				.orElseGet(() -> type.model(color, powered));
	}

	private static PartialModel block(String path) {
		return new PartialModel(BlazingHot.asResource("block/" + path));
	}

	public static void init() {
	}

	public enum ModernLampTypes {
		PANEL("panel"),
		DOUBLE_PANEL("double_panel"),
		QUAD_PANEL("quad_panel"),
		SMALL_PANEL("small_panel"),
		HALF_PANEL("half_panel");

		public final String name;

		ModernLampTypes(String name) {
			this.name = name;
		}

		public PartialModel model(DyeColor color, boolean powered) {
			String suffix = powered ? "_powered" : "";
			return block("modern_lamp/" + name + "/" + color.getSerializedName() + suffix);
		}

		private static List<LampModel> registerAll() {
			List<LampModel> models = new ArrayList<>();
			for (boolean bool : Set.of(true, false))
				for (DyeColor color : DyeColor.values())
					for (ModernLampTypes type : values())
						models.add(new LampModel(type, type.model(color, bool), color, bool));
			return models;
		}
	}

	private record LampModel(ModernLampTypes type, PartialModel model, DyeColor color, boolean powered) {
	}

}
