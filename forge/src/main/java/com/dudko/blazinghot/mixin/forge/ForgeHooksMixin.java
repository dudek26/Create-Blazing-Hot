package com.dudko.blazinghot.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dudko.blazinghot.BlazingHot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.ForgeHooks;

@Mixin(value = ForgeHooks.class, remap = false)
public abstract class ForgeHooksMixin {

	@Inject(at = @At(value = "TAIL"),
			method = "writeAdditionalLevelSaveData(Lnet/minecraft/world/level/storage/WorldData;Lnet/minecraft/nbt/CompoundTag;)V")
	private static void blazinghot$writeExtraData(WorldData worldData, CompoundTag levelTag, CallbackInfo ci) {
		CompoundTag blazinghotData = new CompoundTag();

		blazinghotData.putBoolean("legacy_fluid_amounts", BlazingHot.USE_LEGACY_FLUID_AMOUNTS);

		levelTag.put("blazinghot", blazinghotData);
	}

	@Inject(at = @At(value = "TAIL"),
			method = "readAdditionalLevelSaveData(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)V")
	private static void blazinghot$readExtraData(CompoundTag rootTag, LevelStorageSource.LevelDirectory levelDirectory, CallbackInfo ci) {
		CompoundTag tag = rootTag.getCompound("blazinghot");

		if (tag.contains("legacy_fluid_amounts")) {
			BlazingHot.USE_LEGACY_FLUID_AMOUNTS = tag.getBoolean("legacy_fluid_amounts");
		}
	}

}
