package com.pyromanticgaming.admindrop;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminDrop extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getLogger().info("AdminDrop has been enabled.");
		for(Player player: getServer().getOnlinePlayers()) {
			if(player.hasPermission("AdminDrop.nd") || player.isOp()) {
				player.sendMessage("AdminDrop has Reloaded.");
			}
		}
		
		getServer().getPluginManager().registerEvents(this, this);
		
		
		getCommand("nd").setExecutor(new AdminDropCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {
		getLogger().info("AdminDrop has been disabled.");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		AdminDropCommandExecutor.dropless.remove(player.getName());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
	Entity e = event.getEntity();
	Player p = (Player) e;
	getLogger().info(p.getName() + " was here");
	if((e instanceof Player) && AdminDropCommandExecutor.dropless.contains(p.getName())) {
		event.getDrops().clear();
	}
	}

	
}
