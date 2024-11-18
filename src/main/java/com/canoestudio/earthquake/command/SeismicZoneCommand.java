package com.canoestudio.earthquake.command;

import com.canoestudio.earthquake.util.SeismicZoneUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class SeismicZoneCommand extends CommandBase {

    @Override
    public String getName() {
        return "seismiczone";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/seismiczone <add|remove|list> [x] [y] [z] [radius]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            throw new CommandException("Invalid arguments. Usage: " + getUsage(sender));
        }

        String action = args[0];

        switch (action) {
            case "add":
                if (args.length != 5) {
                    throw new CommandException("Usage: /seismiczone add <x> <y> <z> <radius>");
                }
                BlockPos addPos = new BlockPos(parseInt(args[1]), parseInt(args[2]), parseInt(args[3]));
                int radius = parseInt(args[4]);
                SeismicZoneUtil.addSeismicZone(addPos, radius);
                sender.sendMessage(getTextComponent("Added seismic zone at " + addPos + " with radius " + radius));
                break;

            case "remove":
                if (args.length != 4) {
                    throw new CommandException("Usage: /seismiczone remove <x> <y> <z>");
                }
                BlockPos removePos = new BlockPos(parseInt(args[1]), parseInt(args[2]), parseInt(args[3]));
                boolean removed = SeismicZoneUtil.removeSeismicZone(removePos);
                if (removed) {
                    sender.sendMessage(getTextComponent("Removed seismic zone at " + removePos));
                } else {
                    sender.sendMessage(getTextComponent("No seismic zone found at " + removePos));
                }
                break;

            case "list":
                sender.sendMessage(getTextComponent("Current seismic zones:"));
                for (String zoneInfo : SeismicZoneUtil.getSeismicZones().stream().map(Object::toString).toList()) {
                    sender.sendMessage(getTextComponent(zoneInfo));
                }
                break;

            default:
                throw new CommandException("Unknown action. Usage: " + getUsage(sender));
        }
    }

    private net.minecraft.util.text.ITextComponent getTextComponent(String message) {
        return new net.minecraft.util.text.TextComponentString(message);
    }
}