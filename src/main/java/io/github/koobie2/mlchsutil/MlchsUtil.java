package io.github.koobie2.mlchsutil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.koobie2.mlchsutil.commands.JailCommand;
import io.github.koobie2.mlchsutil.commands.UnjailCommand;


public class MlchsUtil extends JavaPlugin implements Listener {
	public static JavaPlugin pluginInstance;

	@Override
	public void onEnable() {
		pluginInstance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (new JailCommand().command(sender, command, label, args)) {
			return true;
		} else if (new UnjailCommand().command(sender, command, label, args)) {
			return true;
		} else {
			return false;
		}
	}
}