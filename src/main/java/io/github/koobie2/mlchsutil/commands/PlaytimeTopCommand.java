package io.github.koobie2.mlchsutil.commands;

import java.util.Arrays;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import io.github.koobie2.mlchsutil.listeners.TimeListener;




public class PlaytimeTopCommand implements CommandExecutor {
	
	private static class PlayerTimeComparator implements Comparator<OfflinePlayer> {

		@Override
		public int compare(OfflinePlayer player1, OfflinePlayer player2) {
			long[] player1Time = player1.getPersistentDataContainer().get(TimeListener.TIME_ARRAY, PersistentDataType.LONG_ARRAY);
			long[] player2Time = player2.getPersistentDataContainer().get(TimeListener.TIME_ARRAY, PersistentDataType.LONG_ARRAY);
			if (player1Time[0] > player2Time[0] && player1Time[1] > player2Time[1] && player1Time[2] > player2Time[2]) {
				return 1;
			} else if (player1Time[0] < player2Time[0] && player1Time[1] < player2Time[1] && player1Time[2] < player2Time[2]) {
				return -1;
			}
			return 0;
		}
		
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
		if (command.getName().equalsIgnoreCase("topplaytime")) {
			OfflinePlayer[] allPlayers = Bukkit.getOfflinePlayers();
			Arrays.sort(allPlayers, new PlayerTimeComparator());
			int boardLength = allPlayers.length;
			if (args.length == 1 && Integer.parseInt(args[0]) < allPlayers.length) {
				boardLength = Integer.parseInt(args[0]);
			}
			sender.sendMessage("susser board");
			sender.sendMessage("---------------------------------------------");
			for (int i = 0; i < boardLength; i++) {
				long[] playerTime = allPlayers[i].getPersistentDataContainer().get(TimeListener.TIME_ARRAY, PersistentDataType.LONG_ARRAY);
				sender.sendMessage(String.format("%d %s %10s %14s %18s", i+1, allPlayers[i].getName(), 
						"hour:" + playerTime[2],
						"min:" + playerTime[1],
						"sec:" + playerTime[0]));
			}
			return true;
		}
		return false;
	}
}
