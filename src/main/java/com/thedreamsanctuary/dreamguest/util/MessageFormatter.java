package com.thedreamsanctuary.dreamguest.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class MessageFormatter {
	private static String playerplaceholder = "${n}";
	private static String playercountplaceholder = "${no}";
	private static String playercountmin1placeholder = "${no-1}";
	private static String adminplaceholder = "${admin}";
	private static String reasonplaceholder = "${reason}";
	private static String messageplaceholder = "${message}";
	
	public static String formatJoinLeaveMessage(String format, Player player){
		format = applyColours(format);
		return format.replace(playerplaceholder, player.getDisplayName()).replace(playercountplaceholder, "" + Bukkit.getOnlinePlayers().size()).replace(playercountmin1placeholder, "" + (Bukkit.getOnlinePlayers().size()-1));
	}
	
	public static String formatKickBanMessage(String format, String sender, String target, String reason){
		format = applyColours(format);
		format = format.replace(playerplaceholder, target);
		format = format.replace(adminplaceholder, sender);
		format = format.replace(reasonplaceholder, reason);
		return format;
	}
	
	public static String formatAFKMessage(String format, Player player, String message){
		format = applyColours(format);
		return format.replace(playerplaceholder, player.getDisplayName()).replace(messageplaceholder, message);
	}
	
	public static String formatPlayerNotBannedMessage(String format, String player){
		format = applyColours(format);
		return format.replace(playerplaceholder, player);
	}
	
	public static String applyColours(String s){
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}
}
