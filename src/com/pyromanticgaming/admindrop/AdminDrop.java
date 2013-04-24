package com.pyromanticgaming.admindrop;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminDrop extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("AdminDrop has been enabled.");
		for(Player player: getServer().getOnlinePlayers()) {
			if(player.hasPermission("nd.allow")) {
				player.sendMessage("AdminDrop has Reloaded.");
			}
		}
		getCommand("basic").setExecutor(new AdminDropCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {
		getLogger().info("AdminDrop has been disabled.");
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
	Entity ent = e.getEntity();
	Player player = (Player) ent;
	if(!(ent instanceof Player)) {
		return;
	} else {
		if(/*PLACEHOLDER*/AdminDropCommandExecutor.value) {
			
		}
	}
	 
	}
}
