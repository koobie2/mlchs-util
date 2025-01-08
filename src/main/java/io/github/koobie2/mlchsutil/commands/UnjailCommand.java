package io.github.koobie2.mlchsutil.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class UnjailCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player targetPlayer = null;

		if (command.getName().equalsIgnoreCase("unjail") && sender.isOp()) {
			if (args.length >= 1) {
				targetPlayer = Bukkit.getPlayer(args[0]);
			} else {
				if (sender instanceof Player) {
					targetPlayer = (Player) sender;
				} else {
					return false;
				}
			} 
			if (targetPlayer.getPersistentDataContainer().get(JailCommand.JAILED, PersistentDataType.BOOLEAN)) {
				JailCommand.unjailPlayer(targetPlayer);
			} else {
				sender.sendMessage("That player is already jailed");
			}

			return true;
		}
		return false;
	}
}
