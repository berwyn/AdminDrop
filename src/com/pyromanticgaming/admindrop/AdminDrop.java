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

	public static int DDLogLevel;
	
	@Override
	public void onEnable() {
		getLogger().info("AdminDrop has been enabled.");
		
		getServer().getPluginManager().registerEvents(this, this);
		
	    this.saveDefaultConfig();
	    DDLogLevel = this.getConfig().getInt("DDLogLevel");
	    
		
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
		if(DDLogLevel == 1 || DDLogLevel == 2 || DDLogLevel == 3); {
			getLogger().info(p.getName() + " Has died with Death Drops enabled!");
		}
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
