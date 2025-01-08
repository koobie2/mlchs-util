package io.github.koobie2.mlchsutil.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;

import io.github.koobie2.mlchsutil.MlchsUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Ticks;

public class JailCommand implements Listener, CommandExecutor {
	protected static final NamespacedKey JAILED = new NamespacedKey(MlchsUtil.pluginInstance, "jailed");
	protected static final NamespacedKey JAILED_BLOCKS_POSITIONS = new NamespacedKey(MlchsUtil.pluginInstance, "jailed_blocks_positions");
	private Player player;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("jail") && sender.isOp()) {
			if (args.length >= 1) {
				player = Bukkit.getPlayer(args[0]);
			} else {
				if (sender instanceof Player) {
					player = (Player) sender;
				} else {
					return false;
				}
			}
			if (args.length >= 2) {
				BukkitScheduler scheduler = MlchsUtil.pluginInstance.getServer().getScheduler();
				final Player finalPlayer = player;
				scheduler.runTaskLater(MlchsUtil.pluginInstance, () -> {
					UnjailCommand.unjailPlayer(finalPlayer);
				}, Long.parseLong(args[1])*Ticks.TICKS_PER_SECOND);
			}
			if (player.getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN) == null || player.getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN) == false) {
				jailPlayer(player);
			} else {
				player.sendMessage("That player is already jailed.");
			}
			return true;
		}
		return false;
	}

	private void jailPlayer(Player player) {
		Location location = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ());
		player.getPersistentDataContainer().set(JAILED, PersistentDataType.BOOLEAN, true);
		buildJail(location);
	}
	
	private void buildJail(Location location) {
		buildSquare(location.offset(0, 2, 0).toLocation(player.getWorld()), 1, false, Material.AIR, Material.STONE_BRICKS);
		buildSquare(location.offset(0, -1, 0).toLocation(player.getWorld()), 1, false, Material.AIR, Material.STONE_BRICKS);
		buildSquare(location.offset(0, 0, 0).toLocation(player.getWorld()), 1, true, Material.AIR, Material.IRON_BARS);
		buildSquare(location.offset(0, 1, 0).toLocation(player.getWorld()), 1, true, Material.AIR, Material.IRON_BARS);	
	}

	private void buildSquare(Location location, int size, boolean hollow, Material materialFrom, Material materialTo) {
		ArrayList<int[]> blockLocationArrays = new ArrayList<>();
		for (int r = -size; r <= size; r++) {
			for (int c = -size; c <= size; c++) {
				if (location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().getType() == materialFrom) {
					if (hollow) {
						if (r == size || c == size || r == -size || c == -size) {
							location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().setType(materialTo);
							blockLocationArrays.add(new int[] {location.blockX()+r, location.blockY(), location.blockZ()+c});	
						}
						continue;
					}
					location.offset(r, 0, c).toLocation(location.getWorld()).getBlock().setType(materialTo);
					blockLocationArrays.add(new int[] {location.blockX()+r, location.blockY(), location.blockZ()+c});	
				}
			}
		}
		addToJailedBlocksPositionsOrElseCreate(blockLocationArrays);
	}
	
	private void addToJailedBlocksPositionsOrElseCreate(List<int[]> list) {
		if (player.getPersistentDataContainer().has(JAILED_BLOCKS_POSITIONS, PersistentDataType.LIST.integerArrays())) {
			addToJailedBlocksPositions(list);
		} else {
			player.getPersistentDataContainer().set(JAILED_BLOCKS_POSITIONS, PersistentDataType.LIST.integerArrays(), list);
		}
	}
	
	private void addToJailedBlocksPositions(List<int[]> list) {
		ArrayList<int[]> clonedList = new ArrayList<>(player.getPersistentDataContainer().get(
				JAILED_BLOCKS_POSITIONS, 
				PersistentDataType.LIST.integerArrays()));
		clonedList.addAll(list);
		player.getPersistentDataContainer().set(
				JAILED_BLOCKS_POSITIONS, 
				PersistentDataType.LIST.integerArrays(), 
				clonedList);	
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		if (!event.getPlayer().getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN)) {
			return;
		}
		event.getPlayer().sendActionBar(Component.text("theres no breaking blocks in jail"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN)) {
			return;
		}
		event.getPlayer().sendActionBar(Component.text("theres no placing blocks in jail"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onProjectileThrown(PlayerLaunchProjectileEvent event) {
		if (!event.getPlayer().getPersistentDataContainer().get(JAILED, PersistentDataType.BOOLEAN)) {
			return;
		}
		event.getPlayer().sendActionBar(Component.text("theres no throwing things in jail"));
		event.setCancelled(true);
	}
}