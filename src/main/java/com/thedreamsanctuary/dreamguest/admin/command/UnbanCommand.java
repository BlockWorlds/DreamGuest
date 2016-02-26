package com.thedreamsanctuary.dreamguest.admin.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thedreamsanctuary.dreamguest.CommandHandler;
import com.thedreamsanctuary.dreamguest.Module;
import com.thedreamsanctuary.dreamguest.admin.Ban;
import com.thedreamsanctuary.dreamguest.admin.handlers.BanHandler;
import com.thedreamsanctuary.dreamguest.util.MessageFormatter;

public class UnbanCommand extends CommandHandler implements TabCompleter{

	public UnbanCommand(Module m) {
		super(m);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(cmd.getName().equalsIgnoreCase("unban") && args.length == 1){
			if(sender instanceof Player){
				return BanHandler.getBannedPlayers(args[0]);
			}
		}
		return new ArrayList<String>();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length != 1){
			return false;
		}
		String target = args[0];
		Ban b;
		//check if argument is UUID or name
		if(target.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")){
			b = BanHandler.getBan(UUID.fromString(target));
		}else{
			b = BanHandler.getBan(target);
		}
		if(b == null){
			sender.sendMessage(ChatColor.RED + "That player is not banned.");
			return true;
		}
		Bukkit.broadcastMessage(MessageFormatter.formatKickBanMessage(pl.getConfig().getString("admin-unban-message"), sender.getName(), b.getName(), ""));
		BanHandler.unbanPlayer(b.getUUID());
		return true;
	}
}
