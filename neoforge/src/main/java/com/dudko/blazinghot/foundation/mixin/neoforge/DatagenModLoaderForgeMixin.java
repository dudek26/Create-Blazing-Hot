package com.dudko.blazinghot.foundation.mixin.neoforge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.neoforged.neoforge.data.loading.DatagenModLoader;

@Mixin(value = DatagenModLoader.class, remap = false)
public abstract class DatagenModLoaderForgeMixin {

//	@WrapOperation(method = "begin",
//			at = @At(value = "INVOKE",
//					target = "Lnet/minecraftforge/data/event/GatherDataEvent$DataGeneratorConfig;runAll()V"))
//	private static void begin(GatherDataEvent.DataGeneratorConfig dataGeneratorConfig, Operation<Void> operation) {
//		if (!FMLEnvironment.production && isRunningDataGen()) {
//			try {
//				operation.call(dataGeneratorConfig);
//			} catch (Throwable throwable) {
//				LogUtils.getLogger().error("Data generation failed", throwable);
//			} finally {
//				System.exit(0);
//			}
//		}
//		else {
//			operation.call(dataGeneratorConfig);
//		}
//	}

	@Shadow
	public static boolean isRunningDataGen() {
		throw new RuntimeException();
	}
}
