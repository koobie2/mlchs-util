package io.github.koobie2.mlchsutil.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import io.github.koobie2.mlchsutil.listeners.TimeListener;

public class PlaytimeCommand implements CommandExecutor {
	private static final int HOURS = 2;
	private static final int MINUTES = 1;
	private static final int SECONDS = 0;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		Player targetPlayer = null;
		if (command.getName().equalsIgnoreCase("playtime")) {
			 if (args.length == 1 && Bukkit.getPlayer(args[0]) != null) {
				 targetPlayer = Bukkit.getPlayer(args[0]);
			 } else if (args.length == 0 && sender instanceof Player) {
				 targetPlayer = (Player) sender;
			 } else {
				 sender.sendMessage("That player seems to not exist or you are calling from the server terminal.");
				 return false;
			 }
			 long[] timeArray = targetPlayer.getPersistentDataContainer().get(TimeListener.TIME_ARRAY, PersistentDataType.LONG_ARRAY);
			 sender.sendMessage(String.format("playtime of: %s\nhours: %d\nminutes: %d\nseconds: %d", targetPlayer.getName(), timeArray[HOURS], timeArray[MINUTES], timeArray[SECONDS]));	
			 return true;
		}
		return false; 
	}
}