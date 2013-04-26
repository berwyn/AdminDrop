package com.pyromanticgaming.admindrop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminDrop extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getLogger().info("AdminDrop has been enabled.");
		
		getServer().getPluginManager().registerEvents(this, this);
		
		
		getCommand("ad").setExecutor(new AdminDropCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {
		for(Player player: getServer().getOnlinePlayers()) {
			if(AdminDropCommandExecutor.dropless.contains(player.getName())) {
				player.sendMessage(ChatColor.DARK_BLUE + "Your items are not safe.");
				player.sendMessage("AdminDrop is reloading.");
			}
		}
		
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
	if((e instanceof Player) && AdminDropCommandExecutor.dropless.contains(p.getName())) {
		event.getDrops().clear();
	} else
	if((e instanceof Player) && p.hasPermission("AdminDrop.dd")) {
		event.getDrops().clear();
	}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItemEvent(PlayerDropItemEvent event){
	Player p = event.getPlayer();
	if ((p instanceof Player) && AdminDropCommandExecutor.throwless.contains(p.getName())) {
		event.setCancelled(true);
		p.sendMessage("Throwing away your items has been disabled.");
	}
	
	}

	
}
