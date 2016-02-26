package com.thedreamsanctuary.dreamguest.admin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thedreamsanctuary.dreamguest.Module;
import com.thedreamsanctuary.dreamguest.admin.handlers.VanishFakeQuitHandler;
import com.thedreamsanctuary.dreamguest.CommandHandler;

public class VanishCommand extends CommandHandler{
	static final ChatColor textcolor = ChatColor.AQUA;
	static final String vanishstring = textcolor + "You have vanished!";
	static final String appearstring = textcolor + "You have reappeared!";
	static final String vanishotherstring = textcolor + "You have vanished the player "+ChatColor.GOLD+"${n}" + textcolor + "!";
	static final String appearotherstring = textcolor + "You have made the player "+ChatColor.GOLD+"${n}" + textcolor + " reappear!";
	static final String othervanishedstring = textcolor + "You have been vanished by the player "+ChatColor.GOLD+"${n}" + textcolor + "!";
	static final String otherappearedstring = textcolor + "You have been unvanished by the player "+ChatColor.GOLD+"${n}" + textcolor + "!";
	public VanishCommand(Module m) {
		super(m);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage("Only players may execute this command.");
			return true;
		}
		if(args.length > 1){
			return false;
		}
		//if no argument given, target is the player itself
		if(args.length == 0){
			Player player = (Player) sender;
			//toggle Vanishing
			if(VanishFakeQuitHandler.isVanished(player)){
				VanishFakeQuitHandler.unvanishPlayer(player);
				player.sendMessage(appearstring);
			}else{
				VanishFakeQuitHandler.vanishPlayer(player);
				player.sendMessage(vanishstring);
			}
			return true;
		}
		//try to find target player
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(ChatColor.RED + "Player could not be found.");
			return true;
		}
		//toggle targeted players vanish status
		if(VanishFakeQuitHandler.isVanished(player)){
			VanishFakeQuitHandler.unvanishPlayer(player);
			sender.sendMessage(appearotherstring.replace("${n}", player.getDisplayName()));
			player.sendMessage(otherappearedstring.replace("${n}", sender.getName()));
		}else{
			VanishFakeQuitHandler.vanishPlayer(player);
			sender.sendMessage(vanishotherstring.replace("${n}", player.getDisplayName()));
			player.sendMessage(othervanishedstring.replace("${n}", sender.getName()));
		}
		return true;
	}
	
	
}
