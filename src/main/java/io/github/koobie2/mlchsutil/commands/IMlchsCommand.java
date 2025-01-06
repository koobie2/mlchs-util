package io.github.koobie2.mlchsutil.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface IMlchsCommand {
	boolean command(CommandSender sender, Command command, String label, String[] args);
}
