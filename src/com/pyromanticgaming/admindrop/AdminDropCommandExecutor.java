package com.pyromanticgaming.admindrop;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;




public class AdminDropCommandExecutor implements CommandExecutor {
	
	private AdminDrop admindrop;
	
	public static Set<String> dropless = new HashSet<String>();
	
	
	public AdminDropCommandExecutor(AdminDrop admindrop) {
		
		this.admindrop = admindrop;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("nd")) {
			if (sender instanceof Player) {
				if(sender.hasPermission("AdminDrop.nd") || sender.isOp()) {
					if (args.length > 1) {
						sender.sendMessage("AdminDrop - Too many arguments!");
						return false;
					}
					DropCommand(sender);
					return true;
				} else {
					sender.sendMessage("AdminDrop - UNAUTHORIZED USER DETECTED!");
					return false;
				}
			} else {
				sender.sendMessage("AdminDrop - Command must be entered by a player.");
				return false;
			}
		}
		return false;
	}
	
	private void DropCommand(CommandSender sender) {
		Player player = (Player) sender;
		if(!dropless.contains(player.getName())) {
			DisableDrops(player);
		} else {
			EnableDrops(player);
		}
	}
	
	private void DisableDrops(Player player) {
		player.sendMessage(ChatColor.DARK_BLUE + "Your items are safe.");
		dropless.add(player.getName());
	}
	
	private void EnableDrops(Player player) {
		player.sendMessage(ChatColor.DARK_BLUE + "Your items are not safe.");
		dropless.remove(player.getName());
	}
}
