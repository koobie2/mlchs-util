package io.github.koobie2.mlchsutil.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
				unjailPlayer(targetPlayer);
			} else {
				sender.sendMessage("That player is already not in jail.");
			}

			return true;
		}
		return false;
	}
	
	protected static void unjailPlayer(Player player) {
		for (int[] blockPosition : player.getPersistentDataContainer().get(JailCommand.JAILED_BLOCKS_POSITIONS, PersistentDataType.LIST.integerArrays())) {
			Location blockLocation = new Location(player.getWorld(), blockPosition[0], blockPosition[1], blockPosition[2]);
			blockLocation.getBlock().setType(Material.AIR);
		}
		player.getPersistentDataContainer().set(JailCommand.JAILED, PersistentDataType.BOOLEAN, false);
	}
}
