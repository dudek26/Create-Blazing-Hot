package com.dudko.blazinghot.gui.ponder.scenes;

import com.dudko.blazinghot.content.casting.Molds;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBehaviour;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlock;
import com.dudko.blazinghot.content.casting.casting_depot.CastingDepotBlockEntity;
import com.dudko.blazinghot.content.casting.casting_depot.SpoutCastingBehaviour;
import com.dudko.blazinghot.foundation.multiloader.fluid.MultiAmount;
import com.dudko.blazinghot.gui.ponder.BlazingPonderScenes;
import com.dudko.blazinghot.registry.BlazingItems;
import com.dudko.blazinghot.registry.BlazingMetals;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
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
		ItemStack mold = Molds.INGOT.get(Molds.MoldType.STURDY).asStack();
		scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(mold);
		scene.idle(10);
		castingDepotInsert(scene, depotPos, mold.copy());

		scene.idle(40);
		scene
				.overlay()
				.showText(60)
				.pointAt(depotCenter)
				.placeNearTarget()
				.attachKeyFrame()
				.text("...the Spout will fill the Depot...");

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
		scene.world().showSection(depotS, Direction.DOWN);

		scene.idle(20);
		ItemStack sheetMold = Molds.SHEET.get(Molds.MoldType.STURDY).asStack();

		Vec3 filter = util.vector().blockSurface(depotPos, Direction.NORTH).add(0, -0.5 / 16f, 0);

		scene.overlay().showFilterSlotInput(filter, Direction.NORTH, 80);
		scene.overlay().showControls(filter, Pointing.RIGHT, 60).withItem(ingot).rightClick();
		scene.world().setFilterData(depotS, CastingDepotBlockEntity.class, ingot);
		scene.idle(10);
		scene
				.overlay()
				.showText(80)
				.text("The filter slot can be used in case you want to restrict recipes.")
				.attachKeyFrame()
				.pointAt(filter)
				.placeNearTarget();
		scene.idle(90);

		scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(sheetMold);
		scene.idle(10);
		castingDepotInsert(scene, depotPos, sheetMold.copy());

		scene.idle(40);
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
		BlockPos funnel1Pos = depot1Pos.north();
		BlockPos funnel2Pos = depot2Pos.north();

		Selection depot1S = util.select().position(depot1Pos);
		Selection spout1S = util.select().position(spout1Pos);
		Selection depot2S = util.select().position(depot2Pos);
		Selection spout2S = util.select().position(spout2Pos);

		Selection largeCog = util.select().position(2, 0, 5);
		Selection kinetics = util.select().fromTo(3, 1, 5, 3, 3, 3);
		Selection tank = util.select().fromTo(2, 1, 4, 2, 2, 4);
		Selection pipes = util.select().fromTo(2, 3, 4, 2, 3, 2);
		Selection funnels = util.select().fromTo(1, 1, 1, 3, 1, 1);

		// This is mostly for multiloader compatibility
		Fluid fluid = BlazingMetals.BLAZE_GOLD.getFluid().get();
		BlazingPonderScenes.setFluidInTank(scene, tankPos, fluid, MultiAmount.BUCKET.multiply(12).get());
		BlazingPonderScenes.setFluidInTank(scene, spout1Pos, fluid, MultiAmount.BUCKET.get());
		BlazingPonderScenes.setFluidInTank(scene, spout2Pos, fluid, MultiAmount.BUCKET.get());

		scene.world().modifyBlock(util.grid().at(2, 3, 3), s -> s.setValue(PumpBlock.FACING, Direction.NORTH), false);
		scene.world().showSection(depot1S, Direction.DOWN);
		scene.world().showSection(depot2S, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(spout1S, Direction.DOWN);
		scene.world().showSection(spout2S, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(tank, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(largeCog, Direction.UP);
		scene.world().showSection(kinetics, Direction.NORTH);
		scene.world().showSection(pipes, Direction.NORTH);

		scene.idle(5);
		scene
				.overlay()
				.showText(60)
				.independent()
				.placeNearTarget()
				.attachKeyFrame()
				.text("Molds can be either Sturdy or Porcelain.");

		scene.idle(50);
		Molds.Mold mold = Molds.ROD;
		ItemStack porcelain = mold.get(Molds.MoldType.PORCELAIN).asStack();
		ItemStack sturdy = mold.get(Molds.MoldType.STURDY).asStack();
		Vec3 depot1Center = util.vector().centerOf(depot1Pos);
		Vec3 depot2Center = util.vector().centerOf(depot2Pos);

		scene.overlay().showControls(depot1Center, Pointing.UP, 30).withItem(porcelain);
		scene.overlay().showControls(depot2Center, Pointing.UP, 30).withItem(sturdy);

		scene.idle(10);
		castingDepotInsert(scene, depot1Pos, porcelain.copy());
		castingDepotInsert(scene, depot2Pos, sturdy.copy());

		scene.idle(50);
		scene
				.overlay()
				.showText(60)
				.pointAt(depot1Center)
				.placeNearTarget()
				.attachKeyFrame()
				.text("Porcelain Molds will be consumed on cast...");

		scene.idle(30);
		scene.world().showSection(funnels, Direction.SOUTH);

		scene.idle(30);
		castingDepotExtract(scene, depot1Pos, 1);
		castingDepotExtract(scene, depot2Pos, 1);
		scene.world().flapFunnel(funnel1Pos, true);
		scene.world().flapFunnel(funnel2Pos, true);

		scene
				.world()
				.createItemEntity(util.vector().blockSurface(funnel1Pos, Direction.DOWN).add(0, 0, -0.25),
						Vec3.ZERO,
						BlazingItems.BLAZE_GOLD_ROD.asStack());
		scene
				.world()
				.createItemEntity(util.vector().blockSurface(funnel2Pos, Direction.DOWN).add(0, 0, -0.25),
						Vec3.ZERO,
						BlazingItems.BLAZE_GOLD_ROD.asStack());

		scene.idle(10);
		scene
				.overlay()
				.showText(60)
				.pointAt(depot2Center)
				.placeNearTarget()
				.attachKeyFrame()
				.text("...while Sturdy Molds are reusable.");
		scene.idle(100);
	}

	public static void airCurrent(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("casting_air_current", "Altering cooling speed with Air Current");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spoutPos = util.grid().at(1, 3, 1);
		BlockPos depotPos = util.grid().at(1, 1, 1);
		BlockPos tankPos = util.grid().at(1, 1, 3);
		BlockPos fanCatalystPos = util.grid().at(3, 1, 1);

		Selection depotS = util.select().position(depotPos);
		Selection spoutS = util.select().position(spoutPos);
		Selection fanS = util.select().fromTo(4, 1, 0, 2, 1, 2);

		Selection air = util.select().fromTo(fanCatalystPos, depotPos);

		Selection largeCog = util.select().position(5, 0, 2);
		Selection fanCog = util.select().position(5, 1, 1);
		Selection pumpKinetics = util.select().fromTo(5, 1, 3, 2, 3, 3).add(util.select().position(2, 3, 2));
		Selection tank = util.select().fromTo(1, 1, 3, 1, 2, 3);
		Selection pipes = util.select().fromTo(1, 3, 2, 1, 3, 3);

		// This is mostly for multiloader compatibility
		Fluid fluid = BlazingMetals.COPPER.getFluid().get();
		BlazingPonderScenes.setFluidInTank(scene, tankPos, fluid, MultiAmount.BUCKET.multiply(12).get());
		BlazingPonderScenes.setFluidInTank(scene, spoutPos, fluid, MultiAmount.BUCKET.get());

		scene.world().modifyBlock(util.grid().at(1, 3, 2), s -> s.setValue(PumpBlock.FACING, Direction.NORTH), false);

		scene.world().showSection(depotS, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(spoutS, Direction.DOWN);

		scene.idle(10);
		scene.world().showSection(fanS, Direction.WEST);
		scene.world().showSection(largeCog, Direction.WEST);
		scene.world().showSection(fanCog, Direction.WEST);

		scene.idle(10);
		Vec3 catalystCenter = util.vector().centerOf(fanCatalystPos);
		Vec3 catalystTop = util.vector().topOf(fanCatalystPos);
		scene
				.overlay()
				.showOutlineWithText(air, 80)
				.pointAt(catalystCenter)
				.attachKeyFrame()
				.placeNearTarget()
				.text("Air Currents can be used to alter the cooling speed of molten metals.");

		scene.idle(80);
		scene
				.overlay()
				.showControls(catalystCenter, Pointing.DOWN, 20)
				.withItem(Items.WATER_BUCKET.getDefaultInstance());

		scene.idle(10);
		scene.world().setBlock(fanCatalystPos, Blocks.WATER.defaultBlockState(), false);

		scene.idle(40);
		scene.world().showSection(tank, Direction.NORTH);

		scene.idle(5);
		Vec3 depotCenter = util.vector().centerOf(depotPos);
		scene.world().showSection(pumpKinetics, Direction.WEST);
		scene.world().showSection(pipes, Direction.NORTH);

		scene.idle(20);
		ItemStack mold = Molds.INGOT.get(Molds.MoldType.STURDY).asStack();
		scene.overlay().showControls(depotCenter, Pointing.UP, 30).withItem(mold);

		scene.idle(10);
		castingDepotInsert(scene, depotPos, mold.copy());

		scene.idle(20);
		scene
				.overlay()
				.showText(60)
				.attachKeyFrame()
				.placeNearTarget()
				.pointAt(catalystTop)
				.text("Water particles will speed up the cooling process.");

		scene.idle(60);
		scene
				.overlay()
				.showText(60)
				.placeNearTarget()
				.pointAt(depotCenter)
				.colored(PonderPalette.MEDIUM)
				.text("Cooling speed: x1.5");

		scene.idle(100);
		scene
				.overlay()
				.showControls(catalystCenter, Pointing.DOWN, 30)
				.withItem(Items.LAVA_BUCKET.getDefaultInstance());

		scene.idle(10);
		scene.world().setBlock(fanCatalystPos, Blocks.LAVA.defaultBlockState(), false);

		scene.idle(10);
		scene.world().hideSection(depotS, Direction.UP);

		scene.idle(15);
		castingDepotExtract(scene, depotPos, 1);
		scene.world().showSection(depotS, Direction.DOWN);

		scene.idle(50);
		scene
				.overlay()
				.showText(80)
				.attachKeyFrame()
				.placeNearTarget()
				.pointAt(catalystTop)
				.text("On the other hand, lava particles will make the cooling process longer.");

		scene.idle(80);
		scene
				.overlay()
				.showText(80)
				.placeNearTarget()
				.pointAt(depotCenter)
				.colored(PonderPalette.RED)
				.text("Cooling speed: x0.75");

		scene.idle(100);
	}

	public static void automating(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("casting_automating", "Automating the Casting process");
		scene.configureBasePlate(1, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spoutPos = util.grid().at(3, 3, 2);
		BlockPos depotPos = util.grid().at(3, 1, 2);
		BlockPos tankPos = util.grid().at(2, 1, 4);
		BlockPos topFunnelPos = util.grid().at(3, 2, 2);
		BlockPos beltPos = util.grid().at(1, 1, 2);
		BlockPos leverPos = util.grid().at(5, 1, 2);
		BlockPos sideFunnelPos = util.grid().at(3, 1, 1);
		BlockPos armPos = util.grid().at(2, 1, 1);
		BlockPos armDepotPos = util.grid().at(4, 1, 1);

		Selection depotS = util.select().position(3, 1, 2);
		Selection spoutS = util.select().position(3, 3, 2);

		Selection largeCog = util.select().position(4, 0, 5);
		Selection kinetics = util.select().fromTo(3, 1, 5, 3, 2, 3);
		Selection tank = util.select().fromTo(2, 1, 4, 2, 2, 4);
		Selection pipes = util.select().fromTo(2, 3, 4, 3, 3, 3);

		Selection chestAndFunnel = util.select().fromTo(4, 1, 2, 4, 2, 2).add(util.select().position(3, 2, 2));
		Selection lever = util.select().position(5, 1, 2);
		Selection belt = util.select().fromTo(1, 1, 2, 2, 1, 3);
		Selection sideFunnel = util.select().position(sideFunnelPos);

		Selection armLargeCog = util.select().position(0, 0, 0);
		Selection arm = util.select().position(4, 1, 1).add(util.select().fromTo(2, 1, 1, 1, 1, 0));

		// This is mostly for multiloader compatibility
		Fluid fluid = BlazingMetals.ZINC.getFluid().get();
		BlazingPonderScenes.setFluidInTank(scene, tankPos, fluid, MultiAmount.BUCKET.multiply(12).get());
		BlazingPonderScenes.setFluidInTank(scene, spoutPos, fluid, MultiAmount.BUCKET.get());

		scene.world().modifyBlock(util.grid().at(3, 3, 3), s -> s.setValue(PumpBlock.FACING, Direction.NORTH), false);

		scene.world().showSection(depotS, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(spoutS, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(tank, Direction.DOWN);

		scene.idle(5);
		scene.world().showSection(largeCog, Direction.UP);
		scene.world().showSection(kinetics, Direction.NORTH);
		scene.world().showSection(pipes, Direction.NORTH);

		scene.idle(10);
		ElementLink<WorldSectionElement>
				chestFunnel =
				scene.world().showIndependentSection(chestAndFunnel, Direction.WEST);

		scene.idle(20);
		Vec3 depotCenter = util.vector().centerOf(depotPos);
		Vec3 topFunnel = util.vector().centerOf(topFunnelPos).add(0.25 / 16f, 0, 0);
		Vec3 beltVec = util.vector().centerOf(beltPos);
		scene
				.overlay()
				.showText(70)
				.pointAt(topFunnel)
				.placeNearTarget()
				.attachKeyFrame()
				.text("This is the simplest setup for Casting automation.");

		scene.idle(80);
		scene
				.overlay()
				.showText(80)
				.pointAt(topFunnel)
				.placeNearTarget()
				.colored(PonderPalette.OUTPUT)
				.text("The funnel above Casting Depot will pull any output items.");

		scene.idle(90);
		ElementLink<WorldSectionElement> beltLink = scene.world().showIndependentSection(belt, Direction.EAST);

		scene.idle(10);
		scene
				.overlay()
				.showText(60)
				.pointAt(beltVec)
				.placeNearTarget()
				.attachKeyFrame()
				.colored(PonderPalette.INPUT)
				.text("Molds can be inserted from a belt.");

		scene.idle(20);
		ItemStack mold = Molds.INGOT.get(Molds.MoldType.STURDY).asStack();
		scene.overlay().showControls(beltVec.add(0, 0.5, 0), Pointing.DOWN, 30).withItem(mold.copy());

		scene.idle(10);
		scene.world().createItemOnBelt(beltPos, Direction.UP, mold.copy());

		scene.idle(20);
		castingDepotInsert(scene, depotPos, mold.copy());

		scene.idle(40);
		ElementLink<WorldSectionElement>
				sideFunnelLink =
				scene.world().showIndependentSection(sideFunnel, Direction.SOUTH);

		scene.idle(10);
		scene
				.overlay()
				.showText(80)
				.pointAt(util.vector().centerOf(sideFunnelPos))
				.placeNearTarget()
				.attachKeyFrame()
				.colored(PonderPalette.OUTPUT)
				.text("Funnels on the side can also extract output items.");

		scene.idle(80);
		scene.world().hideIndependentSection(sideFunnelLink, Direction.NORTH);

		scene.idle(80);
		castingDepotExtract(scene, depotPos, 1);
		scene.world().flapFunnel(topFunnelPos, false);

		scene.idle(10);
		scene.rotateCameraY(60);

		scene.world().hideIndependentSection(chestFunnel, Direction.EAST);

		scene.idle(15);
		ElementLink<WorldSectionElement> leverLink = scene.world().showIndependentSection(lever, Direction.DOWN);
		scene.world().moveSection(leverLink, util.vector().of(-1, 0, 0), 0);

		scene.idle(10);
		scene.world().cycleBlockProperty(leverPos, LeverBlock.POWERED);
		scene.world().cycleBlockProperty(depotPos, CastingDepotBlock.POWERED);
		scene.effects().indicateRedstone(leverPos.west());

		scene.idle(20);
		Vec3 leverCenter = util.vector().centerOf(leverPos.west());
		scene
				.overlay()
				.showText(80)
				.pointAt(leverCenter)
				.placeNearTarget()
				.attachKeyFrame()
				.colored(PonderPalette.RED)
				.text("When redstone signal is provided, Casting Depot won't accept any new fluids.");

		scene.idle(90);
		sideFunnelLink = scene.world().showIndependentSection(sideFunnel, Direction.SOUTH);

		scene.idle(10);
		castingDepotExtract(scene, depotPos, 0);
		ElementLink<EntityElement>
				entityLink =
				scene
						.world()
						.createItemEntity(util.vector().blockSurface(sideFunnelPos, Direction.DOWN).add(0, 0, -0.25),
								Vec3.ZERO,
								mold.copy());
		scene.world().flapFunnel(sideFunnelPos, true);
		scene
				.overlay()
				.showText(60)
				.pointAt(util.vector().centerOf(sideFunnelPos))
				.placeNearTarget()
				.attachKeyFrame()
				.colored(PonderPalette.OUTPUT)
				.text("The signal also allows mold extraction...");

		scene.idle(50);
		ItemStack mold2 = Molds.NUGGET.get(Molds.MoldType.STURDY).asStack();
		scene.world().createItemOnBelt(beltPos, Direction.UP, mold2.copy());

		scene.idle(30);

		scene
				.overlay()
				.showText(50)
				.pointAt(depotCenter.add(-0.75, 0.2, 0))
				.placeNearTarget()
				.colored(PonderPalette.RED)
				.text("...and forbids insertion.");

		scene.idle(60);
		scene.world().hideIndependentSection(leverLink, Direction.UP);
		scene.world().hideIndependentSection(sideFunnelLink, Direction.NORTH);
		scene.world().cycleBlockProperty(depotPos, CastingDepotBlock.POWERED);
		scene.world().modifyEntity(entityLink, e -> e.remove(Entity.RemovalReason.DISCARDED));

		scene.idle(1);
		castingDepotInsert(scene, depotPos, mold2.copy());

		scene.idle(10);
		scene.world().hideIndependentSection(beltLink, Direction.WEST);

		scene.idle(20);
		scene.rotateCameraY(30);

		scene.idle(10);
		scene.world().showSection(arm, Direction.DOWN);
		scene.world().showSection(armLargeCog, Direction.EAST);

		scene.idle(10);
		Vec3 armCenter = util.vector().centerOf(armPos);
		scene
				.overlay()
				.showText(60)
				.pointAt(armCenter)
				.attachKeyFrame()
				.placeNearTarget()
				.text("Mechanical Arms can also interact with the Casting Depot.");

		scene.idle(20);
		ItemStack zinc = AllItems.ZINC_NUGGET.asStack();
		scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);

		scene.idle(35);
		castingDepotExtract(scene, depotPos, 1);
		scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_OUTPUTS, zinc.copy(), 0);

		scene.idle(20);
		scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, zinc.copy(), 0);

		scene.idle(35);
		scene.world().createItemOnBeltLike(armDepotPos, Direction.UP, zinc.copy());
		scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, 0);

		scene.idle(20);
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

	private static void castingDepotExtract(CreateSceneBuilder scene, BlockPos pos, int slot) {
		scene.addInstruction(ponderScene -> {
			PonderLevel world = ponderScene.getWorld();
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (!(blockEntity instanceof CastingDepotBlockEntity castingDepot)) return;
			CastingDepotBehaviour depotBehaviour = castingDepot.getBehaviour(CastingDepotBehaviour.TYPE);
			if (depotBehaviour == null) return;
			depotBehaviour.extract(slot, 1, false);
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
