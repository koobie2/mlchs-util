package io.github.koobie2.mlchsutil;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.Command;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public class MlchsUtil extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(
                Commands.literal("new-command")
                    .executes(ctx -> {
                        ctx.getSource().getSender().sendPlainMessage("some message");
                        return Command.SINGLE_SUCCESS;
                    })
                    .build(),
                "some bukkit help description string",
                List.of("an-alias")
            );
        });
	}
}
