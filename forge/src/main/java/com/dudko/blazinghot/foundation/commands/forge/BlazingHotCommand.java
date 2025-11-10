package com.dudko.blazinghot.foundation.commands.forge;

import com.dudko.blazinghot.BlazingHot;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.server.command.EnumArgument;

public class BlazingHotCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands
				.literal("blazinghot")
				.then(Commands
						.literal("fluid")
						.executes(BlazingHotCommand::getFluidAmounts)
						.requires(r -> r.hasPermission(2))
						.then(Commands
								.argument("fluid_amount", EnumArgument.enumArgument(FluidAmounts.class))
								.executes(BlazingHotCommand::setFluidAmounts))));
	}

	private static int getFluidAmounts(CommandContext<CommandSourceStack> context) {
		FluidAmounts option = BlazingHot.USE_LEGACY_FLUID_AMOUNTS ? FluidAmounts.LEGACY : FluidAmounts.DEFAULT;
		context.getSource().sendSystemMessage(Component.literal("Currently using " + option + " fluid amounts."));
		return Command.SINGLE_SUCCESS;
	}

	private static int setFluidAmounts(CommandContext<CommandSourceStack> context) {
		FluidAmounts option = context.getArgument("fluid_amount", FluidAmounts.class);
		boolean useLegacyFluidAmounts = option == FluidAmounts.LEGACY;
		if (useLegacyFluidAmounts == BlazingHot.USE_LEGACY_FLUID_AMOUNTS) {
			context
					.getSource()
					.sendSystemMessage(Component
							.literal("Already using " + option.toString() + " fluid amounts.")
							.withStyle(ChatFormatting.RED));
			return 0;
		}

		context
				.getSource()
				.sendSystemMessage(Component
						.literal("Now using " + option.toString() + " fluid amounts.")
						.withStyle(ChatFormatting.GREEN)
						.append(Component.literal("\nRun /reload to apply changes.").withStyle(ChatFormatting.GRAY)));
		BlazingHot.USE_LEGACY_FLUID_AMOUNTS = useLegacyFluidAmounts;

		return Command.SINGLE_SUCCESS;
	}

	enum FluidAmounts {
		DEFAULT,
		LEGACY
	}

}
