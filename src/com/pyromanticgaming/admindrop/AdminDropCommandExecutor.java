package com.pyromanticgaming.admindrop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;




public class AdminDropCommandExecutor implements CommandExecutor {

	//Temp Placeholder
	public static boolean value = true;
	
	private AdminDrop admindrop;
	
	public AdminDropCommandExecutor(AdminDrop admindrop) {
		
		this.admindrop = admindrop;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("ndon") && args.length == 1) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if(player.hasPermission("nd.allow")) {
					//ADD SHIT HERE
				} else {
					sender.sendMessage("AdminDrop - UNAUTHORIZED USER DETECTED!");
					return false;
				}
			} else {
				sender.sendMessage("AdminDrop - Command must be entered by a player.");
				return false;
			}
		}
		if (args.length > 1) {
			sender.sendMessage("AdminDrop - Too many arguments!");
			return false;
		}
		return false;
	}
}
