package com.thedreamsanctuary.dreamguest.chat.command;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.thedreamsanctuary.dreamguest.CommandHandler;
import com.thedreamsanctuary.dreamguest.Module;
import com.thedreamsanctuary.dreamguest.util.Text;

public class AddAFKMessageCommand extends CommandHandler{

	public AddAFKMessageCommand(Module m) {
		super(m);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 1){
			return false;
		}
		//only execute if random afk messages are enabled
		if(!pl.getConfig().getBoolean("random-afk-messages")){
			sender.sendMessage(ChatColor.RED + "Sorry, random afk messages have to be enabled to use this feature.");
			return true;
		}
		String message = "";
		for(int i = 0 ; i < args.length ; i++){
			message += args[i] + " ";
		}
		//parse given message
		ArrayList<String> afkMessages = Text.parseFile("afk-messages");
		//check if afk message exists in message list
		if(afkMessages.contains(message)){
			sender.sendMessage(ChatColor.DARK_AQUA + "That AFK message already exists!");
			return true;
		}
		afkMessages.add(message);
		//write updated AFK message ArrayList to file
		try {
			Text.writeTextToFile("afk-messages", afkMessages);
		} catch (IOException e) {
			sender.sendMessage(ChatColor.RED + "There was an error writing to file. Please contact an Administrator.");
			e.printStackTrace();
			return true;
		}
		sender.sendMessage(ChatColor.DARK_AQUA + "Your AFK message has been added.");
		return true;
	}

}
