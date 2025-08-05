package com.dudko.blazinghot.gui.ponder.scenes;

import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.gui.ponder.BlazingPonderScenes;
import com.dudko.blazinghot.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

public class CastingScenes {

	public static void castingBySpout(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("spout_casting", "Casting items using a Spout");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spoutPos = util.grid().at(2, 3, 2);
		BlockPos depotPos = util.grid().at(2, 1, 2);
		BlockPos tankPos = util.grid().at(1, 1, 4);

		Selection depotS = util.select().position(2, 1, 2);
		Selection spoutS = util.select().position(2, 3, 2);

		Selection largeCog = util.select().position(3, 0, 5);
		Selection kinetics = util.select().fromTo(2, 1, 5, 2, 2, 3);
		Selection tank = util.select().fromTo(1, 1, 4, 1, 2, 4);
		Selection pipes = util.select().fromTo(1, 3, 4, 2, 3, 3);

		// This is mostly for multiloader compatibility
		Fluid fluid = BlazingMetals.GOLD.getFluid().get();
		BlazingPonderScenes.setFluidInTank(scene, tankPos, fluid, MultiAmount.BUCKET.multiply(12).get());
		BlazingPonderScenes.setFluidInTank(scene, spoutPos, fluid, MultiAmount.BUCKET.get());

		scene.world().modifyBlock(util.grid().at(2, 3, 3), s -> s.setValue(PumpBlock.FACING, Direction.NORTH), false);

		scene.world().showSection(depotS, Direction.DOWN);

		scene.idle(10);
		scene.world().showSection(spoutS, Direction.DOWN);

		scene.idle(10);
		Vec3 spoutSide = util.vector().blockSurface(spoutPos, Direction.WEST);
		scene
				.overlay()
				.showText(60)
				.pointAt(spoutSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("The Spout can cast fluids onto a Casting Depot below.");

		scene.idle(50);
		scene.world().showSection(tank, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(largeCog, Direction.UP);
		scene.world().showSection(kinetics, Direction.NORTH);
		scene.world().showSection(pipes, Direction.NORTH);

		scene.idle(20);
		Vec3 depotCenter = util.vector().centerOf(depotPos);
		scene
				.overlay()
				.showText(60)
				.pointAt(depotCenter)
				.placeNearTarget()
				.attachKeyFrame()
				.text("When Casting Depot has a valid mold...");

		scene.idle(60);
		ItemStack porcelainMold = Molds.INGOT.get(Molds.MoldType.PORCELAIN).asStack();
		scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(porcelainMold);
		castingDepotInsert(scene, depotPos, porcelainMold.copy());

		scene.idle(40);
		scene
				.overlay()
				.showText(60)
				.pointAt(depotCenter)
				.placeNearTarget()
				.attachKeyFrame()
				.text("...the spout will fill the depot...");

		scene.idle(140);
		scene
				.overlay()
				.showText(60)
				.pointAt(depotCenter)
				.placeNearTarget()
				.attachKeyFrame()
				.text("...and the metal will cool down into a specified form.");

		scene.idle(50);
		ItemStack ingot = Items.GOLD_INGOT.getDefaultInstance();
		scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(ingot);

		scene.idle(40);
		scene.world().hideSection(depotS, Direction.UP);
		castingDepotReset(scene, depotPos);

		scene.idle(20);
		// TODO: add filters in this section
		scene.world().showSection(depotS, Direction.DOWN);

		scene.idle(20);
		ItemStack sturdyMold = Molds.SHEET.get(Molds.MoldType.STURDY).asStack();
		scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(sturdyMold);
		castingDepotInsert(scene, depotPos, sturdyMold.copy());

		scene.idle(40);
		scene
				.overlay()
				.showText(80)
				.pointAt(depotCenter)
				.placeNearTarget()
				.attachKeyFrame()
				.text("Sturdy molds don't get consumed on cast.");
		scene.idle(160);
	}

	public static void molds(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("casting_molds", "Using different mold types");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spout1Pos = util.grid().at(1, 3, 2);
		BlockPos depot1Pos = util.grid().at(1, 1, 2);
		BlockPos spout2Pos = util.grid().at(3, 3, 2);
		BlockPos depot2Pos = util.grid().at(3, 1, 2);
		BlockPos tankPos = util.grid().at(2, 1, 4);

		Selection depot1S = util.select().position(depot1Pos);
		Selection spout1S = util.select().position(spout1Pos);
		Selection depot2S = util.select().position(depot2Pos);
		Selection spout2S = util.select().position(spout2Pos);

		Selection largeCog = util.select().position(2, 0, 5);
		Selection kinetics = util.select().fromTo(3, 1, 5, 3, 3, 3);
		Selection tank = util.select().fromTo(2, 1, 4, 2, 2, 4);
		Selection pipes = util.select().fromTo(2, 3, 4, 2, 3, 2);

		// This is mostly for multiloader compatibility
		Fluid fluid = BlazingMetals.BLAZE_GOLD.getFluid().get();
		BlazingPonderScenes.setFluidInTank(scene, tankPos, fluid, MultiAmount.BUCKET.multiply(12).get());
		BlazingPonderScenes.setFluidInTank(scene, spout1Pos, fluid, MultiAmount.BUCKET.get());
		BlazingPonderScenes.setFluidInTank(scene, spout2Pos, fluid, MultiAmount.BUCKET.get());

		scene.world().modifyBlock(util.grid().at(2, 3, 3), s -> s.setValue(PumpBlock.FACING, Direction.NORTH), false);
		scene.world().showSection(depot1S, Direction.DOWN);
		scene.world().showSection(depot2S, Direction.DOWN);

		scene.idle(10);
		scene.world().showSection(spout1S, Direction.DOWN);
		scene.world().showSection(spout2S, Direction.DOWN);

		scene.idle(10);
		scene.world().showSection(tank, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(largeCog, Direction.UP);
		scene.world().showSection(kinetics, Direction.NORTH);
		scene.world().showSection(pipes, Direction.NORTH);

		scene
				.overlay()
				.showText(60)
				.independent()
				.placeNearTarget()
				.attachKeyFrame()
				.text("Molds can be either sturdy or porcelain.");

		scene.idle(50);

		Molds.Mold mold = Molds.ROD;
		ItemStack porcelain = mold.get(Molds.MoldType.PORCELAIN).asStack();
		ItemStack sturdy = mold.get(Molds.MoldType.STURDY).asStack();
		Vec3 depot1Center = util.vector().centerOf(depot1Pos);
		Vec3 depot2Center = util.vector().centerOf(depot2Pos);

		castingDepotInsert(scene, depot1Pos, porcelain.copy());
		castingDepotInsert(scene, depot2Pos, sturdy.copy());
		scene.overlay().showControls(depot1Center, Pointing.UP, 30).withItem(porcelain);
		scene.overlay().showControls(depot2Center, Pointing.UP, 30).withItem(sturdy);
	}

	public static void transporting(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("casting_transporting", "Transporting items to and from Casting Depots");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);
	}

	public static void airCurrent(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("casting_air_current", "Modifying cooling speed with air current");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);
	}

	private static void castingDepotInsert(CreateSceneBuilder scene, BlockPos pos, ItemStack stack) {
		scene.addInstruction(ponderScene -> {
			PonderLevel world = ponderScene.getWorld();
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (!(blockEntity instanceof CastingDepotBlockEntity castingDepot)) return;
			CastingDepotBehaviour depotBehaviour = castingDepot.getBehaviour(CastingDepotBehaviour.TYPE);
			if (depotBehaviour == null) return;
			depotBehaviour.insert(stack, Direction.UP, false);
		});
	}

	private static void castingDepotReset(CreateSceneBuilder scene, BlockPos pos) {
		scene.addInstruction(ponderScene -> {
			PonderLevel world = ponderScene.getWorld();
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (!(blockEntity instanceof CastingDepotBlockEntity castingDepot)) return;
			CastingDepotBehaviour depotBehaviour = castingDepot.getBehaviour(CastingDepotBehaviour.TYPE);
			if (depotBehaviour == null) return;
			depotBehaviour.removeHeldStack();
			castingDepot.setOutputItem(ItemStack.EMPTY);
			SpoutCastingBehaviour castingBehaviour = castingDepot.getBehaviour(SpoutCastingBehaviour.TYPE);
			if (castingBehaviour == null) return;
			castingBehaviour.resetProcessing();
		});
	}

}
