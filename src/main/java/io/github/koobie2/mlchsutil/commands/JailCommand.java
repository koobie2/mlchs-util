package io.github.koobie2.mlchsutil.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.koobie2.mlchsutil.MlchsUtil;
import net.kyori.adventure.util.Ticks;

public class JailCommand {
	protected static final NamespacedKey JAILED = new NamespacedKey(MlchsUtil.pluginInstance, "jailed");
	protected static final NamespacedKey POSX_ON_JAILED = new NamespacedKey(MlchsUtil.pluginInstance, "x-on-jailed");
	protected static final NamespacedKey POSY_ON_JAILED = new NamespacedKey(MlchsUtil.pluginInstance, "y-on-jailed");
	protected static final NamespacedKey POSZ_ON_JAILED = new NamespacedKey(MlchsUtil.pluginInstance, "z-on-jailed");
	
	public boolean command(CommandSender sender, Command command, String label, String[] args) {
		
		Player targetPlayer = null;
		if (command.getName().equalsIgnoreCase("jail") && sender.isOp()) {
			if (args.length >= 1) {
				targetPlayer = Bukkit.getPlayer(args[0]);
			} else {
				if (sender instanceof Player) {
					targetPlayer = (Player) sender;
				} else {
					return false;
				}
			}
			if (args.length >= 2) {
				BukkitScheduler scheduler = MlchsUtil.pluginInstance.getServer().getScheduler();
				final Player finalPlayer = targetPlayer;
				scheduler.runTaskLater(MlchsUtil.pluginInstance, () -> {
					unjailPlayer(finalPlayer);
				}, Long.parseLong(args[1])*Ticks.TICKS_PER_SECOND);
			}
			if (targetPlayer.getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN) == null || targetPlayer.getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN) == false) {
				jailPlayer(targetPlayer);
			}
				return true;
		}
		return false;
	}

	private void jailPlayer(Player player) {
		Location location = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ());
		
		player.getPersistentDataContainer().set(JAILED, PersistentDataType.BOOLEAN, true);
		player.getPersistentDataContainer().set(POSX_ON_JAILED, PersistentDataType.DOUBLE, player.getX());
		player.getPersistentDataContainer().set(POSY_ON_JAILED, PersistentDataType.DOUBLE, player.getY());
		player.getPersistentDataContainer().set(POSZ_ON_JAILED, PersistentDataType.DOUBLE, player.getZ());
		
		buildSquare(location.offset(0, 2, 0).toLocation(player.getWorld()), 1);
		buildSquare(location.offset(0, -1, 0).toLocation(player.getWorld()), 1);
		buildHollowSquare(location.offset(0, 0, 0).toLocation(player.getWorld()), 1);
		buildHollowSquare(location.offset(0, 1, 0).toLocation(player.getWorld()), 1);
	}
	
	protected static void unjailPlayer(Player player) {
		double jailedX = player.getPersistentDataContainer().get(POSX_ON_JAILED, PersistentDataType.DOUBLE);
		double jailedY = player.getPersistentDataContainer().get(POSY_ON_JAILED, PersistentDataType.DOUBLE);
		double jailedZ = player.getPersistentDataContainer().get(POSZ_ON_JAILED, PersistentDataType.DOUBLE);
		Location location = new Location(player.getWorld(), jailedX, jailedY, jailedZ);
		
		player.getPersistentDataContainer().set(JAILED, PersistentDataType.BOOLEAN, false);
		buildSquare(location.offset(0, 2, 0).toLocation(player.getWorld()), 1, Material.STONE_BRICKS, Material.AIR);
		buildSquare(location.offset(0, -1, 0).toLocation(player.getWorld()), 1, Material.STONE_BRICKS, Material.AIR);
		buildHollowSquare(location.offset(0, 0, 0).toLocation(player.getWorld()), 1, Material.IRON_BARS, Material.AIR);
		buildHollowSquare(location.offset(0, 1, 0).toLocation(player.getWorld()), 1, Material.IRON_BARS, Material.AIR);
	}

	private static void buildSquare(Location location, int size) {
		buildSquare(location, size, Material.AIR, Material.STONE_BRICKS);
	}
	
	private static void buildSquare(Location location, int size, Material materialFrom, Material materialTo) {
		for (int r = -size; r <= size; r++) {
			for (int c = -size; c <= size; c++) {
				if (location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().getType() == materialFrom) {
					location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().setType(materialTo);
				}
			}
		}
	}
	
	private static void buildHollowSquare(Location location, int size) {
		buildHollowSquare(location, size, Material.AIR, Material.IRON_BARS);
	}

	private static void buildHollowSquare(Location location, int size, Material materialFrom, Material materialTo) {
		for (int r = -size; r <= size; r++) {
			for (int c = -size; c <= size; c++) {
				if (location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().getType() == materialFrom && r == size || c == size || r == -size || c == -size) {
					location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().setType(materialTo);
				}
			}
		}
	}
}
