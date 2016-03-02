package com.thedreamsanctuary.dreamguest;

import org.bukkit.command.CommandExecutor;

public abstract class CommandHandler implements CommandExecutor{
	protected final Module m;
	protected final DreamGuest pl;
	public CommandHandler(Module m){
		this.m = m;
		this.pl = m.getPlugin();
	}
	protected Module getModule(){
		return this.m;
	}
}
