package io.github.koobie2.mlchsutil.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.koobie2.mlchsutil.MlchsUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public class TimeListener implements Listener {
	public static final NamespacedKey TIME_ARRAY = new NamespacedKey(MlchsUtil.pluginInstance, "time-array");
	private static final int HOURS = 2;
	private static final int MINUTES = 1;
	private static final int SECONDS = 0;

	public TimeListener() {
		// TODO Auto-generated constructor stub
	}
	
	@EventHandler
	private static void onPlayerJoin (PlayerJoinEvent event) {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		Player player = event.getPlayer();
		
		if (!player.getPersistentDataContainer().has(TIME_ARRAY)) {
			player.getPersistentDataContainer().set(TIME_ARRAY, PersistentDataType.LONG_ARRAY, new long[] {0, 0, 0});
		}
		
		scheduler.runTaskTimer(MlchsUtil.pluginInstance, task -> {
			if (Bukkit.getPlayer(player.getName()) == null) {
				task.cancel();
			}

			long[] timeArray = player.getPersistentDataContainer().get(TIME_ARRAY, PersistentDataType.LONG_ARRAY);
			timeArray[SECONDS]++;
			if (timeArray[SECONDS] >= 60) {
				timeArray[MINUTES]++;
				timeArray[SECONDS] = 0;
			}
			if (timeArray[MINUTES] >= 60) {
				timeArray[HOURS]++;
				timeArray[MINUTES] = 0;
				if (timeArray[HOURS] % 100 == 0) {
					Bukkit.getServer().sendMessage(Component.text(String.format("player: %s has exceeded %d hours in playtime", player.getName(), timeArray[2]))
							.style(Style.style(TextDecoration.BOLD)));
				}
			}
			player.getPersistentDataContainer().set(TIME_ARRAY, PersistentDataType.LONG_ARRAY, timeArray);
		}, 20, 20);
	}
}