package com.thedreamsanctuary.dreamguest;


import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.thedreamsanctuary.dreamguest.command.admin.Ban;
import com.thedreamsanctuary.dreamguest.command.admin.BanReason;
import com.thedreamsanctuary.dreamguest.command.admin.Kick;
import com.thedreamsanctuary.dreamguest.command.admin.Unban;
import com.thedreamsanctuary.dreamguest.command.admin.Vanish;
import com.thedreamsanctuary.dreamguest.command.chat.AFK;
import com.thedreamsanctuary.dreamguest.command.chat.AddAFKMessage;
import com.thedreamsanctuary.dreamguest.command.chat.Who;
import com.thedreamsanctuary.dreamguest.handlers.VanishFakeQuitHandler;
import com.thedreamsanctuary.dreamguest.listeners.ConnectionEventListener;
import com.thedreamsanctuary.dreamguest.listeners.PlayerEventListener;
import com.thedreamsanctuary.dreamguest.metrics.MetricsLite;
import com.thedreamsanctuary.dreamguest.util.JSON;
import com.thedreamsanctuary.dreamguest.util.Text;

public class DreamGuest extends JavaPlugin{
	public static PermissionManager pex;
	public void onEnable(){
		this.saveDefaultConfig();
		//initialize PEX Manager
		DreamGuest.pex = PermissionsEx.getPermissionManager();
		if(!JSON.createFile("bans")){
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		if(this.getConfig().getBoolean("random-afk-messages")&&!Text.createFile("afk-messages")){
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.getCommand("who").setExecutor(new Who(this));
		this.getCommand("ban").setExecutor(new Ban(this));
		this.getCommand("unban").setExecutor(new Unban(this));
		this.getCommand("banreason").setExecutor(new BanReason(this));
		this.getCommand("kick").setExecutor(new Kick(this));
		this.getCommand("afk").setExecutor(new AFK(this));
		this.getCommand("addafkmessage").setExecutor(new AddAFKMessage(this));
		this.getCommand("vanish").setExecutor(new Vanish(this));
		this.getServer().getPluginManager().registerEvents(new ConnectionEventListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);
		if(getConfig().getBoolean("collect-metrics")){
			try {
		        MetricsLite metrics = new MetricsLite(this);
		        System.out.println(metrics.start());
		        System.out.println("[DreamGuest] Logging enabled");
		    } catch (IOException e) {
		        Bukkit.getLogger().log(Level.SEVERE, "Failed to link to metrics service, disabling metrics.");
		    }
		}	
	}
	
	public void onDisable(){
		
	}
	
	public PermissionManager getPermissionManager(){
		return pex;
	}
	
	public boolean isVanished(Player player){
		return VanishFakeQuitHandler.isVanished(player);
	}
	
	public boolean isFakeQuit(Player player){
		return VanishFakeQuitHandler.isFakeQuit(player);
	}

	public int getFakeQuitSize(){
		return VanishFakeQuitHandler.getFakeQuitSize();
	}
}
