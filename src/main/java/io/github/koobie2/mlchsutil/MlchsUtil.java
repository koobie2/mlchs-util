package io.github.koobie2.mlchsutil;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.koobie2.mlchsutil.commands.JailCommand;
import io.github.koobie2.mlchsutil.commands.UnjailCommand;

public class MlchsUtil extends JavaPlugin implements Listener {
	public static JavaPlugin pluginInstance;

	@Override
	public void onEnable() {
		pluginInstance = this;
		getCommand("jail").setExecutor(new JailCommand());
		getCommand("unjail").setExecutor(new UnjailCommand());
		
		Bukkit.getPluginManager().registerEvents(new JailCommand(), this);
	}
}