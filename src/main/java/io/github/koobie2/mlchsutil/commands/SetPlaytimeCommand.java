package io.github.koobie2.mlchsutil.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import io.github.koobie2.mlchsutil.listeners.TimeListener;

public class SetPlaytimeCommand implements CommandExecutor {
	Player targetPlayer = null;
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if (command.getName().equalsIgnoreCase("setplaytime") && sender.isOp() && Bukkit.getPlayer(args[0]) != null) {
			targetPlayer = Bukkit.getPlayer(args[0]);
			if (args.length >= 4) {
				long[] timeArray = new long[3];
				timeArray[2] = Long.parseLong(args[1]);
				timeArray[1] = Long.parseLong(args[2]);
				timeArray[0] = Long.parseLong(args[3]);
				targetPlayer.getPersistentDataContainer().set(TimeListener.TIME_ARRAY, PersistentDataType.LONG_ARRAY, timeArray);
				return true;
			}
		}
		return false;
	}
}
