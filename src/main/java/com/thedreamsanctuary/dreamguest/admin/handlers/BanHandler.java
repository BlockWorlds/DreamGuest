package com.thedreamsanctuary.dreamguest.admin.handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;
import com.thedreamsanctuary.dreamguest.DreamGuest;
import com.thedreamsanctuary.dreamguest.admin.Ban;

public class BanHandler {
	private static final File BANS_FILE = new File("plugins/DreamGuest/bans.json");
	private static Set<Ban> bans = new HashSet<Ban>();
	private static DreamGuest plugin;
	public static void init(DreamGuest pl){
		plugin = pl;
		final File banFolder = new File("plugins/DreamGuest/");
		if(!banFolder.exists()){
			banFolder.mkdirs();
		}
		readBans(getBanFile());
		save();
	}
	
	public static void save(){
		saveBans(getBanFile());
	}
	
	public static boolean addBan(Ban b){
		for(Ban ban : bans){
			if(b.getUUID().equals(ban.getUUID())){
				return false;
			}
		}
		boolean success = bans.add(b);
		return success;
	}
	
	
	public static Ban getBan(UUID playerUUID){
		for (final Ban b : bans)
        {
            if (b.getUUID().equals(playerUUID))
            {
                return b;
            }
        }
        return null;
	}
	
	public static Ban getBan(String name){
		for(final Ban b : bans){
			if(b.getName().equalsIgnoreCase(name)){
				return b;
			}
		}
		return null;
	}
	
	public static boolean removeBan(final Ban oldBan)
    {
        final boolean success = bans.remove(oldBan);
        return success;
    }

	public static void readBans(final File banFile)
    {
        final Gson gson = new Gson();
        if (banFile.exists())
        {
            Scanner scan;
            try
            {
                scan = new Scanner(banFile);
            }
            catch (final Exception e)
            {
                plugin.getLogger().severe("Can not open config files");
                e.printStackTrace();
                return;
            }
            try
            {
                while (scan.hasNextLine())
                {
                    final Ban ban = gson.fromJson(scan.nextLine(), Ban.class);
                    addBan(ban);
                }
            }
            catch (final Exception e)
            {
                plugin.getLogger().severe("Can not read config files");
                e.printStackTrace();
                return;
            }
            finally
            {
                scan.close();
            }
        }
        else
        {

        }
    }

    public static void saveBans(final File banfile)
    {
        final Gson gson = new Gson();
        if (!banfile.exists())
        {
            try
            {
                banfile.createNewFile();
            }
            catch (final IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
        else
        {
            if (!banfile.delete())
            {
                plugin.getLogger().severe("Can not save config files");
            }
            try
            {
                banfile.createNewFile();
            }
            catch (final IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
        PrintWriter pw;
        try
        {
            pw = new PrintWriter(banfile);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return;
        }
        for (final Ban b : bans)
        {
                pw.println(gson.toJson(b));
        }
        pw.close();
    }
	
	 public static File getBanFile()
	    {
	        return BANS_FILE;
	    }
	/**
	 * check whether a player is banned or not
	 * @param playerUUID UUID of the player to check
	 * @return true if player is banned, false if not
	 */
	public static boolean isPlayerBanned(UUID playerUUID){
		if(getBan(playerUUID)!=null){
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
		Ban b = getBan(playerUUID);
		if(b==null){
			return "";
		}
		return b.getReason();
	}
	
	/**
	 * unbans a player
	 * @param playerUUID the UUID of the player to ban
	 * @return NOT_BANNED if player is not banned, ERROR if a parsing error occured, SUCCESS if player was unbanned successfully
	 */
	public static boolean unbanPlayer(UUID playerUUID){
		//check if player is banned, if he isn't, return NOT_BANNED
		if(!isPlayerBanned(playerUUID)){
			return false;
		}
		removeBan(getBan(playerUUID));
		save();
		return true;
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
		return getBan(playerUUID).getName();
	}
	
	/**
	 * get a list of all banned players' names
	 * @return a List<String> containing all the banned playernames
	 */
	public static List<String> getBannedPlayers(String start){
		List<Ban> banned = new ArrayList<Ban>();
		for(Ban b : bans){
			String banName = b.getName();
			if(banName.toLowerCase().startsWith(start.toLowerCase())){
				banned.add(b);
			}
		}
		Collections.sort(banned);
		List<String> bannedNames = new ArrayList<String>();
		for(Ban b : banned){
			bannedNames.add(b.getName());
		}
		return bannedNames;
	}
	
}
