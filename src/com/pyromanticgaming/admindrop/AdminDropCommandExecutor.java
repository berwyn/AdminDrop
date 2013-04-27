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
	public static Set<String> protectPlayerList = new HashSet<String>();
	
	public AdminDropCommandExecutor(AdminDrop admindrop) {
		
		this.admindrop = admindrop;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("ad")) {
			if(sender instanceof Player) {
				Player otherPlayer = null; //Declared here to allow use later in the nested if statements
				if((args.length > 0) && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("ta")) {
					playerList.clear(); //To ensure that it is the most up to date list of online players
					for(Player player1: admindrop.getServer().getOnlinePlayers()) {
						playerList.add(player1.getName()); //Adds everyone online to the playerList hashmap
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
					otherPlayer = Bukkit.getPlayer(args[0]); //Snags the target players identifier, NOT NAME, important to note that
					ModifyOtherCommand(otherPlayer, sender);
					return true;
				} else
				if((args.length == 1) && args[0].equalsIgnoreCase("ta") && (sender.hasPermission("AdminDrop.ta") || sender.isOp())) {
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
				if((args.length == 2) && args[0].equalsIgnoreCase("status") && (!playerList.isEmpty() && !playerList.contains(args[1])) && (sender.hasPermission("AdminDrop.so") || sender.isOp())) {
					sender.sendMessage(ChatColor.DARK_BLUE + args[1] + " is either not logged in or name was typed incorrectly.");
					return true;
				}
				else
				if((args.length > 2)) {
					sender.sendMessage("AdminDrop - Too many arguments!");
					InfoArea(sender);
					return true;
				} else
				if((args.length == 1) && args[0].equalsIgnoreCase("help") && (sender.hasPermission("AdminDrop.*") || sender.isOp())) {
					sender.sendMessage("AdminDrop Help Information.");
					InfoArea(sender);
					return true;
				} else
				if((args.length == 1 || args.length == 2) && (sender.hasPermission("AdminDrop.*") || sender.isOp())) {
					sender.sendMessage("AdminDrop - Not a valid argument!");
					InfoArea(sender);
					return true;
				}else {
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
	
	private void StatusOtherCommand(Player otherPlayer, CommandSender sender) { //Checks status of target player
		if(!dropless.contains(otherPlayer.getName()) && !otherPlayer.hasPermission("AdminDrop.dd")) {
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items not are safe.");
		} else {
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items are safe.");
		}
		
	}

	private void InfoArea(CommandSender sender) { //Stores the informational data on command syntax to send to the user
		sender.sendMessage("/ad - Toggles on/off");
		sender.sendMessage("/ad [player] - Toggles other on/off");
		sender.sendMessage("/ad list - Lists users with Toggle on");
		sender.sendMessage("/ad ta - Toggles throwing items");
		sender.sendMessage("/ad status - Gets current status");
		sender.sendMessage("/ad help - Displays commands");
		sender.sendMessage("/ad status [player] - Gets players current status");
	}
	
	private void StatusCommand(CommandSender sender) { //Checks status of the player sending the command
		if(!dropless.contains(sender.getName())) {
			sender.sendMessage(ChatColor.DARK_BLUE + "Your items not are safe.");
		} else {
			sender.sendMessage(ChatColor.DARK_BLUE + "Your items are safe.");
		}
		
	}

	private void ListCommand(CommandSender sender) {
		for(Player onlineplayer: admindrop.getServer().getOnlinePlayers()) {
			if(onlineplayer.hasPermission("AdminDrop.dd")) {
				protectPlayerList.add(onlineplayer.getName());
				dropless.addAll(protectPlayerList);
			}
		}
		if(!dropless.isEmpty()) { //If the hashmap dropless is not empty it will display the players on the list
			String listdropless = dropless.toString(); //hashmaps and Strings do not mix without this
			sender.sendMessage(ChatColor.DARK_BLUE + listdropless);
			dropless.removeAll(protectPlayerList);
			protectPlayerList.clear();
		} else
		if(dropless.isEmpty()){
			sender.sendMessage(ChatColor.DARK_BLUE + "List is empty");
		}
		
	}

	private void ModifyOtherCommand(Player otherPlayer, CommandSender sender) { //Function to check targets name and send it to be toggled
		if(!dropless.contains(otherPlayer.getName())) {
			DisableDrops(otherPlayer);
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items are safe.");
			if(AdminDrop.DDLogLevel == 3); {
				admindrop.getLogger().info(sender.getName() + " Has enabled Death Drops for " + otherPlayer.getName() + "!");
			}
		} else {
			EnableDrops(otherPlayer);
			sender.sendMessage(ChatColor.DARK_BLUE + otherPlayer.getDisplayName() + ChatColor.DARK_BLUE + "'s items are not safe.");
			if(AdminDrop.DDLogLevel == 3); {
				admindrop.getLogger().info(sender.getName() + " Has disabled Death Drops for " + otherPlayer.getName() + "!");
			}
		}
		
	}

	private void NoThrowCommand(CommandSender sender) {
	Player player = (Player) sender;
	if(!throwless.contains(player.getName())) {
		DisableThrows(player);
	}
	else {
		EnableThrows(player);
	}

	}
	
	private void DisableThrows(Player player) {
		player.sendMessage("Throwing away your items has been disabled.");
		throwless.add(player.getName());
	}
	
	private void EnableThrows(Player player) {
		player.sendMessage("Throwing away your items has been enabled.");
		throwless.remove(player.getName());
	}

	private void DropCommand(CommandSender sender) {
		Player player = (Player) sender; //Reason for the conversion here is to use Player in a later function instead of CommandSender
		if(!dropless.contains(player.getName())) { //If the players name is not in the hashmap it will stop drops on death
			DisableDrops(player);
			if(AdminDrop.DDLogLevel == 2 || AdminDrop.DDLogLevel == 3); {
				admindrop.getLogger().info(player.getName() + " Has enabled Death Drops!");
			}
		} else { // Otherwise it will run this function to allow drops to take place again on death
			EnableDrops(player);
			if(AdminDrop.DDLogLevel == 2 || AdminDrop.DDLogLevel == 3); {
				admindrop.getLogger().info(player.getName() + " Has disabled Death Drops!");
			}
		}
	}
	
	private void DisableDrops(Player player) { //Stops drops on death by adding the players name to a hashmap
		player.sendMessage(ChatColor.DARK_BLUE + "Your items are safe.");
		dropless.add(player.getName());
		
	}
	
	private void EnableDrops(Player player) { // Enables drops on death by removing the players name from the hashmap
		player.sendMessage(ChatColor.DARK_BLUE + "Your items are not safe.");
		dropless.remove(player.getName());
	}
}
