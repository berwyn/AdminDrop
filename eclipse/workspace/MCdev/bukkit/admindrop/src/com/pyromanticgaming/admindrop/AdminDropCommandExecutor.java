package com.pyromanticgaming.admindrop;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class AdminDropCommandExecutor implements CommandExecutor {
	
	private AdminDrop admindrop;
	
	public static Set<String> dropless = new HashSet<String>();
	public static Set<String> throwless = new HashSet<String>();
	public static Set<String> playerList = new HashSet<String>();
	
	public AdminDropCommandExecutor(AdminDrop admindrop) {
		
		this.admindrop = admindrop;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("ad")) {
			if(sender instanceof Player) {
				Player otherPlayer = null;
				if((args.length > 0) && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("nt")) {
					playerList.clear();
					for(Player player1: admindrop.getServer().getOnlinePlayers()) {
						playerList.add(player1.getName());
					}
				}
				if((args.length == 0) && (sender.hasPermission("AdminDrop.ad") || sender.isOp())) {
					DropCommand(sender);
					return true;
				} else
				if((args.length == 1) && args[0].equalsIgnoreCase("list") && (sender.hasPermission("AdminDrop.list") || sender.isOp())) {
					ListCommand(sender);
					return true;
				} else
				if((args.length == 1) && (!playerList.isEmpty() && playerList.contains(args[0])) && (sender.hasPermission("AdminDrop.mo") || sender.isOp())) {
					otherPlayer = Bukkit.getPlayer(args[0]);
					ModifyOtherCommand(otherPlayer, sender);
					return true;
				} else
				if((args.length == 1) && args[0].equalsIgnoreCase("nt") && (sender.hasPermission("AdminDrop.nt") || sender.isOp())) {
					NoThrowCommand(sender);
					return true;
				} else
				if((args.length == 1) && args[0].equalsIgnoreCase("status") && (sender.hasPermission("AdminDrop.s") || sender.isOp())) {
					StatusCommand(sender);
					return true;
				} else
				if((args.length == 2) && args[0].equalsIgnoreCase("status") && (!playerList.isEmpty() && playerList.contains(args[1])) && (sender.hasPermission("AdminDrop.so") || sender.isOp())) {
					otherPlayer = Bukkit.getPlayer(args[1]);
					StatusOtherCommand(otherPlayer, sender);
					return true;
				} else
				if((args.length > 2)) {
					sender.sendMessage("AdminDrop - Too many arguments!");
					
					sender.sendMessage("AdminDrop - Not a valid argument!");
					sender.sendMessage("/ad - Toggles on/off");
					sender.sendMessage("/ad [player] - Toggles other on/off");
					sender.sendMessage("/ad list - Lists users with Toggle on");
					sender.sendMessage("/ad nt - Toggles throwing items");
					sender.sendMessage("/ad status - Gets current status");
					sender.sendMessage("/ad status [player] - Gets players current status");
					return true;
				} else
				if((args.length == 1 || args.length == 2) && (sender.hasPermission("AdminDrop.*") || sender.isOp())){
					sender.sendMessage("AdminDrop - Not a valid argument!");
					
					sender.sendMessage("/ad - Toggles on/off");
					sender.sendMessage("/ad [player] - Toggles other on/off");
					sender.sendMessage("/ad list - Lists users with Toggle on");
					sender.sendMessage("/ad nt - Toggles throwing items");
					sender.sendMessage("/ad status - Gets current status");
					sender.sendMessage("/ad status [player] - Gets players current status");
					return true;
				} else {
					sender.sendMessage("AdminDrop - You do not have permission for that.");
					return true;
				}
			} else {
				sender.sendMessage("AdminDrop - Command must be entered by a player.");
				return false;
			}
		}
		return false;
	}
	
	private void StatusOtherCommand(Player otherPlayer, CommandSender sender) {
		if(!dropless.contains(otherPlayer.getName())) {
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items not are safe.");
		} else {
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items are safe.");
		}
		
	}

	private void StatusCommand(CommandSender sender) {
		if(!dropless.contains(sender.getName())) {
			sender.sendMessage(ChatColor.DARK_BLUE + "Your items not are safe.");
		} else {
			sender.sendMessage(ChatColor.DARK_BLUE + "Your items are safe.");
		}
		
	}

	private void ListCommand(CommandSender sender) {
		if(!dropless.isEmpty()) {
			String listdropless = dropless.toString();
			sender.sendMessage(ChatColor.DARK_BLUE + listdropless);
		} else
		if(dropless.isEmpty()){
			sender.sendMessage(ChatColor.DARK_BLUE + "List is empty");
		}
		
	}

	private void ModifyOtherCommand(Player otherPlayer, CommandSender sender) {
		if(!dropless.contains(otherPlayer.getName())) {
			DisableDrops(otherPlayer);
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items are safe.");
		} else {
			EnableDrops(otherPlayer);
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items are not safe.");
		}
		
	}

	private void NoThrowCommand(CommandSender sender) {
		Player player = (Player) sender;
		if(!throwless.contains(player.getName())){
			DisableThrow(player);
			
		} 
		else {
			EnableThrow(player);
		}
	}
	
	private void DisableThrow(Player player) {
		player.sendMessage(ChatColor.DARK_BLUE + "Your items cannot be dropped.");
		throwless.add(player.getName());
	}
	
	private void EnableThrow(Player player) {
		player.sendMessage(ChatColor.DARK_BLUE + "Your items can now be dropped.");
		throwless.remove(player.getName());
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
