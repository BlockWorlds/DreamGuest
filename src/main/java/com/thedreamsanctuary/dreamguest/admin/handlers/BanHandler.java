package com.thedreamsanctuary.dreamguest.admin.handlers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;

import com.thedreamsanctuary.dreamguest.util.BanResult;
import com.thedreamsanctuary.dreamguest.util.JSON;

public class BanHandler {
	/**
	 * check whether a player is banned or not
	 * @param playerUUID UUID of the player to check
	 * @return true if player is banned, false if not
	 */
	public static boolean isPlayerBanned(UUID playerUUID){
		//parse bans.json into JSONObject
		JSONObject banlist = JSON.parseFile("bans");
		//check if banlist contains player UUID
		if(banlist.containsKey(playerUUID.toString())){
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieve a player's ban reason
	 * @param playerUUID the UUID of the player to check
	 * @return the banreason specified in bans.json
	 */
	public static String getPlayerBanreason(UUID playerUUID){
		JSONObject banlist = JSON.parseFile("bans");
		JSONObject entry = (JSONObject) banlist.get(playerUUID.toString());
		return entry.get("reason").toString();
	}
	
	/**
	 * add a player to the banlist
	 * @param playerUUID the UUID of the player to ban
	 * @param reason the reason of banning
	 * @return ALREADY_BANNED if player is already banned, ERROR if a parsing error occurred, SUCCESS if player has been successfully banned
	 */
	public static BanResult addPlayer(UUID playerUUID, String reason){
		//if player is already banned, return as ALREADY_BANNED;
		if(isPlayerBanned(playerUUID)){
			return BanResult.ALREADY_BANNED;
		}
		//get player by UUID
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
		String name = player.getName();
		//create a new JSONObject to hold the ban
		JSONObject obj = new JSONObject();
		//put name and reason into the object
		obj.put("name", name);
		obj.put("reason", reason);
		//read banlist
		JSONObject banlist = JSON.parseFile("bans");
		//if banlist could not be parsed, return ERROR
		if(banlist == null){
			return BanResult.ERROR;
		}
		//put the ban object (name and reason) into the banlist at the key UUID
		banlist.put(playerUUID, obj);
		//write updated banlist to file
		JSON.writeObjectToFile("bans", banlist);
		return BanResult.SUCCESS;
	}
	/**
	 * unbans a player
	 * @param playerUUID the UUID of the player to ban
	 * @return NOT_BANNED if player is not banned, ERROR if a parsing error occured, SUCCESS if player was unbanned successfully
	 */
	public static BanResult unbanPlayer(UUID playerUUID){
		//check if player is banned, if he isn't, return NOT_BANNED
		if(!isPlayerBanned(playerUUID)){
			return BanResult.NOT_BANNED;
		}
		//parse ban file
		JSONObject banlist = JSON.parseFile("bans");
		//if ban file could not be parsed, return ERROR
		if(banlist == null){
			return BanResult.ERROR;
		}
		//remove entry at the position of the player UUID
		banlist.remove(playerUUID.toString());
		//write updated ban list to file
		JSON.writeObjectToFile("bans", banlist);
		return BanResult.SUCCESS;
	}
	/**
	 * get a banned player's name
 	 * @param playerUUID UUID of the targeted player
	 * @return the name the player had at the time of ban
	 */
	public static String getBannedPlayerName(UUID playerUUID){
		//check if player is banned, if not, return an empty String
		if(!isPlayerBanned(playerUUID)){
			return "";
		}
		//parse ban list
		JSONObject banlist = JSON.parseFile("bans");
		//if a parsing error occued, return an empty String
		if(banlist == null){
			return "";
		}
		//get the JSONObject at the key of the player's UUID
		JSONObject entry = (JSONObject) banlist.get(playerUUID.toString());
		//return the name entry of the ban object
		return entry.get("name").toString();
	}
	
}
