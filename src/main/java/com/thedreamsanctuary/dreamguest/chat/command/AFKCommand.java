package com.thedreamsanctuary.dreamguest.chat.command;

import java.util.ArrayList;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.thedreamsanctuary.dreamguest.Module;
import com.thedreamsanctuary.dreamguest.chat.handlers.AfkHandler;
import com.thedreamsanctuary.dreamguest.CommandHandler;
import com.thedreamsanctuary.dreamguest.util.MessageFormatter;
import com.thedreamsanctuary.dreamguest.util.Text;

public class AFKCommand extends CommandHandler{

	public AFKCommand(Module m) {
		super(m);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Only players can execute that command");
			return true;
		}
		Player player = (Player) sender;
		String afkmessage = pl.getConfig().getString("default-afk-message");
		//toggle afk status of player
		if(AfkHandler.toggleAFK(player)){
			//check if afk message was passed as argument
			if(args.length > 0){
				afkmessage = "";
				for(int i = 0 ; i < args.length ; i++){
					afkmessage += args[i] + " ";
				}
			}else{
				//if no afk message was passed, check if random afk messages are enabled
				if(pl.getConfig().getBoolean("random-afk-messages")){
					//retrieve random message
					afkmessage = getRandomAFKMessage();
				}
			}
		}else{
			//if player is returning, print return message
			afkmessage = pl.getConfig().getString("default-return-message");
		}
		Bukkit.broadcastMessage(MessageFormatter.formatAFKMessage(pl.getConfig().getString("afk-format"), player, afkmessage));
		return true;
	}
	
	public String getRandomAFKMessage(){
		//parse ArrayList from text file
		ArrayList<String> messages = Text.parseFile("afk-messages");
		//if no random messages are present, use default message
		if(messages.size()==0){
			return pl.getConfig().getString("default-afk-message");
		}
		//else retrieve random afk message from arraylist and return it
		int randomIndex = RandomUtils.nextInt(messages.size());
		return messages.get(randomIndex);
	}
	
}
