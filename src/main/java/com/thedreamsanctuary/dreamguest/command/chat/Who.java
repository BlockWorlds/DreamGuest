package com.thedreamsanctuary.dreamguest.command.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;

import com.thedreamsanctuary.dreamguest.DreamGuest;
import com.thedreamsanctuary.dreamguest.command.CommandHandler;

public class Who extends CommandHandler{
	private final PermissionManager pex;
	private final Permission perm;
	public Who(DreamGuest pl) {
		super(pl);
		this.pex = pl.getPermissionManager();
		perm = pl.getPerm();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean canSeeFQ = sender.hasPermission("dreamguest.vanish");
		TreeMap<PermissionGroup, List<Player>> groupMap = createGroupMap(canSeeFQ);
		groupMap = new TreeMap<PermissionGroup, List<Player>>(groupMap);
		sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
		sender.sendMessage(getLegend(canSeeFQ));
		
		for(Entry<PermissionGroup, List<Player>> entry : groupMap.descendingMap().entrySet()){
			final PermissionGroup group = entry.getKey();
			final String groupName = group.getName();
			final StringBuilder groupStrBuilder = new StringBuilder();
			groupStrBuilder.append(ChatColor.DARK_GRAY).append("[").append(this.getColor(group)).append(this.getGroupChar(groupName)).append(ChatColor.DARK_GRAY).append("] ");
			boolean previous = false;
			boolean comma = false;
			for(Player player : entry.getValue()){
				comma = false;
				if(pl.isFakeQuit(player)){
					if(!canSeeFQ){
						continue;
					}
					if(previous && !comma){
						groupStrBuilder.append(ChatColor.GOLD).append(", ");
						comma = true;
					}
					groupStrBuilder.append(ChatColor.DARK_GRAY).append("[").append(ChatColor.AQUA).append("FQ").append(ChatColor.DARK_GRAY).append("] ");
				}
				if(previous && !comma){
					groupStrBuilder.append(ChatColor.GOLD).append(", ");
					comma = true;
				}
				if(pl.isAFK(player)){
					groupStrBuilder.append(ChatColor.GRAY).append(player.getDisplayName());
				}else{
					groupStrBuilder.append(ChatColor.WHITE).append(player.getDisplayName());
				}
				previous = true;
			}
			sender.sendMessage(groupStrBuilder.toString());
		}
		sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
		return true;
	}
	
	private Map<PermissionGroup, List<Player>> sortGroupMap(Map<PermissionGroup, List<Player>> groupMap){
		Map<PermissionGroup, List<Player>> sortedMap = new HashMap<PermissionGroup, List<Player>>();
		Set<PermissionGroup> keys = groupMap.keySet();
		PermissionGroup[] array = new PermissionGroup[keys.size()];
		int i = 0;
		for(PermissionGroup key : keys){
			array[i] = key;
			i++;
		}
		Arrays.sort(array);
		for(PermissionGroup key : array){
			System.out.println(key);
			sortedMap.put(key, groupMap.get(key));
		}
		return sortedMap;
	}
	private String getLegend(boolean canSeeFQ){
		final TreeMap<PermissionGroup, Integer> groupCount = new TreeMap<PermissionGroup, Integer>();
		List<PermissionGroup> groups = pex.getGroupList();
		for(PermissionGroup group : groups){
			String groupName = group.getName();
			groupCount.put(group, 0);
			
			for(Player player : Bukkit.getOnlinePlayers()){
				if(pl.isFakeQuit(player)){
					if(!canSeeFQ){
						continue;
					}
				}
				if(pex.getUser(player).getGroupNames()[0].equals(groupName)){
					groupCount.put(group, groupCount.get(group)+1);
				}
			}
		}
		final StringBuilder stringBuilder = new StringBuilder();
		for(PermissionGroup group : groupCount.descendingKeySet()){
			final int groupSize = groupCount.get(group);
			final char groupChar = this.getGroupChar(group.getName());
			stringBuilder.append(ChatColor.DARK_GRAY).append("[").append(this.getColor(group)).append(groupChar).append(":").append(groupSize).append(ChatColor.DARK_GRAY).append("] ");
			
		}
		final int onlinePlayers = canSeeFQ ? Bukkit.getOnlinePlayers().size() : Bukkit.getOnlinePlayers().size() - pl.getFakeQuitSize();
		stringBuilder.append(ChatColor.DARK_GRAY).append("(").append(ChatColor.WHITE).append("O:").append(onlinePlayers).append(ChatColor.DARK_GRAY).append(")");
		return stringBuilder.toString();
	}
	
	private char getGroupChar(final String groupName){
		return groupName.toUpperCase().charAt(0);
	}
	
	private String getColor(PermissionGroup group){
		String option = group.getOption("dreamguest.who.colour");
		String colour = ChatColor.translateAlternateColorCodes('&', ((option==null)?"&f":option));
		return colour;
	}
	
	private TreeMap<PermissionGroup, List<Player>> createGroupMap(final boolean canSeeFQ){
		final TreeMap<PermissionGroup, List<Player>> groupPlayerMap = new TreeMap<PermissionGroup, List<Player>>();
		
		for(Player player : Bukkit.getOnlinePlayers()){
			final PermissionGroup group = pex.getUser(player).getGroups()[0];
			if(!groupPlayerMap.containsKey(group)){
				final List<Player> newGroupList = new ArrayList<>();
				if(pl.isFakeQuit(player)){
					if(!canSeeFQ){
						continue;
					}
				}
				newGroupList.add(player);
				groupPlayerMap.put(group, newGroupList);
			}else{
				if(pl.isFakeQuit(player)){
					if(!canSeeFQ){
						continue;
					}
				}
				groupPlayerMap.get(group).add(player);
			}
		}
		return groupPlayerMap;
	}

}
