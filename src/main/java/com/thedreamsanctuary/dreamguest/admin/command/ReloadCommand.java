package com.thedreamsanctuary.dreamguest.admin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.thedreamsanctuary.dreamguest.CommandHandler;
import com.thedreamsanctuary.dreamguest.Module;

public class ReloadCommand extends CommandHandler{

	public ReloadCommand(Module m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		this.getModule().getPlugin().reloadConfig();
		return true;
	}

}
