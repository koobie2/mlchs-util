package io.github.koobie2.mlchsutil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class MlchsUtil extends JavaPlugin implements Listener {
	private final NamespacedKey JAILED = new NamespacedKey(this, "jailed");
	private final NamespacedKey POS_ON_JAILED = new NamespacedKey(this, "pos-on-jailed");
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("jail") && sender.isOp()) {
			if (Bukkit.getPlayer(args[1]) != null) {
				Player targetPlayer = Bukkit.getPlayer(args[1]);
				if (targetPlayer.getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN) == false) {
					jailPlayer(targetPlayer);
				} else {
					unjailPlayer(targetPlayer);
				}
				
				return true;
			}	
		}
		return false;
	}
	
	private void jailPlayer(Player player) {
		Location location = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ());
		int[] pos = {(int) player.getX(), (int) player.getY(), (int) player.getZ()};
		
		player.getPersistentDataContainer().set(JAILED, PersistentDataType.BOOLEAN, true);
		player.getPersistentDataContainer().set(POS_ON_JAILED, PersistentDataType.INTEGER_ARRAY, pos);
		
		buildSquare(location.add(0, 2, 0), 3);
		buildSquare(location.add(0, -1, 0), 3);
		buildHollowSquare(location.add(0, 0, 0), 3);
		buildHollowSquare(location.add(0, 1, 0), 3);
	}
	
	private void unjailPlayer(Player player) {
		int[] pos = player.getPersistentDataContainer().get(POS_ON_JAILED, PersistentDataType.INTEGER_ARRAY);
		Location location = new Location(player.getWorld(), pos[0], pos[1], pos[2]);
		
		player.getPersistentDataContainer().set(JAILED, PersistentDataType.BOOLEAN, false);
		buildSquare(location.add(0, 2, 0), 3, Material.STONE_BRICKS, Material.AIR);
		buildSquare(location.add(0, -1, 0), 3, Material.STONE_BRICKS, Material.AIR);
		buildHollowSquare(location.add(0, 0, 0), 3, Material.IRON_BARS, Material.AIR);
		buildHollowSquare(location.add(0, 1, 0), 3, Material.IRON_BARS, Material.AIR);
	}

	private void buildSquare(Location location, int size) {
		buildSquare(location, size, Material.AIR, Material.STONE_BRICKS);
	}
	
	private void buildSquare(Location location, int size, Material materialFrom, Material materialTo) {
		for (int r = -size; r < size; r++) {
			for (int c = -size; c < size; c++) {
				if (location.add(r, 0, c).getBlock().getType() == materialFrom) {
					location.add(r, 0, c).getBlock().setType(materialTo);
				}
			}
		}
	}
	
	private void buildHollowSquare(Location location, int size) {
		buildHollowSquare(location, size, Material.AIR, Material.STONE_BRICKS);
	}

	private void buildHollowSquare(Location location, int size, Material materialFrom, Material materialTo) {
		for (int r = -size; r < size; r++) {
			for (int c = -size; c < size; c++) {
				if (location.add(r, 0, c).getBlock().getType() == materialFrom && r == size || c == size) {
					location.add(r, 0, c).getBlock().setType(materialTo);
				}
			}
		}
	}
}