package io.github.koobie2.mlchsutil;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.koobie2.mlchsutil.commands.JailCommand;
import io.github.koobie2.mlchsutil.commands.PlaytimeCommand;
import io.github.koobie2.mlchsutil.commands.PlaytimeTopCommand;
import io.github.koobie2.mlchsutil.commands.SetPlaytimeCommand;
import io.github.koobie2.mlchsutil.commands.UnjailCommand;
import io.github.koobie2.mlchsutil.listeners.TimeListener;

public class MlchsUtil extends JavaPlugin implements Listener {
	public static JavaPlugin pluginInstance;

	@Override
	public void onEnable() {
		pluginInstance = this;
		registerCommands();
		registerEventHandlers();
	}
	
	private void registerCommands() {
		getCommand("jail").setExecutor(new JailCommand());
		getCommand("unjail").setExecutor(new UnjailCommand());	
		getCommand("playtime").setExecutor(new PlaytimeCommand());	
		getCommand("setplaytime").setExecutor(new SetPlaytimeCommand());	
		getCommand("topplaytime").setExecutor(new PlaytimeTopCommand());	
	}
	
	private void registerEventHandlers() {
		Bukkit.getPluginManager().registerEvents(new JailCommand(), this);
		Bukkit.getPluginManager().registerEvents(new TimeListener(), this);
	}
}