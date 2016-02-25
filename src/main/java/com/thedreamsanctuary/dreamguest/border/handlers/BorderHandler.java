package com.thedreamsanctuary.dreamguest.border.handlers;

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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;




import com.google.gson.Gson;
import com.thedreamsanctuary.dreamguest.DreamGuest;
import com.thedreamsanctuary.dreamguest.border.Border;

public class BorderHandler {
	private static final File BORDER_FILE = new File("plugins/DreamGuest/borders.json");
	private static Set<Border> borders = new HashSet<Border>();
	private static Set<UUID> worlds = new HashSet<UUID>();
	private static DreamGuest plugin;
	
	public static void init(DreamGuest pl){
		plugin = pl;
		final File borderFolder = new File("plugins/DreamGuest/");
		if(!borderFolder.exists()){
			borderFolder.mkdirs();
		}
		readBorders(BORDER_FILE);
		saveBorders(getBorderFile());
	}
	
	public static void save(){
		saveBorders(getBorderFile());
	}
	
	public static boolean addBorder(Border b){
		for(Border border : borders){
			if(border.getName().equalsIgnoreCase(b.getName())){
				return false;
			}
		}
		worlds.add(b.getWorldID());
		borders.add(b);
		return false;
	}
	
	
	public static Border getBorder(String name){
		for (final Border b : borders)
        {
            if (b.getName().equalsIgnoreCase(name))
            {
                return b;
            }
        }
        return null;
	}
	
	private static void updateActiveWorlds() {
	        worlds.clear();
	        for (Border b : borders) {
	            worlds.add(b.getWorldID());
	        }
	    }
	
	public static boolean removeBorder(final Border oldBorder)
    {
        final boolean success = borders.remove(oldBorder);
        updateActiveWorlds();
        return success;
    }
	
	public static String[] getBorders() {
        final List<String> borderTxt = new ArrayList<String>();
        for (Border border : borders)
        {
            borderTxt.add(border.toColoredString());
        }
        Collections.sort(borderTxt);
        if(borderTxt.isEmpty()){
        	borderTxt.add(ChatColor.GRAY + "No zones active.");
        }
        return borderTxt.toArray(new String[0]);
    }
	
	public static boolean canMoveTo(final Player player, final Location startLoc, final Location endLoc){
		if (endLoc == null){
			plugin.getLogger().severe("A move event reported a null destination location!");
	    	return true;
	    }
	    if (endLoc.getWorld() == null){
	    	plugin.getLogger().severe("A move event reported a null destination world!");
	    	return true;
	    }
	    if(!worlds.contains(endLoc.getWorld().getUID())){
	    	return true;
	    }
	    
	    boolean canEnter = true;
	    boolean inZone = false;
	    
	    for(final Border b : borders){
	    	if(b.getWorldID().equals(endLoc.getWorld().getUID())){	    		
	    		if(b.isInside(endLoc)){
	    			inZone = true;
	    			if(b.isInside(startLoc)){
	    				if(canEnter){
	    					continue;
	    				}	
	    			}
	    			if(player.hasPermission(b.getPermission())){
	    				if(canEnter){
	    					player.sendMessage(ChatColor.GRAY + "Now crossing border of " + ChatColor.GREEN + b.getName().trim());
	    					continue;
	    				}
	    			}else{
	    				player.sendMessage(ChatColor.GRAY + "You can not cross the border of " + ChatColor.GREEN + b.getName().trim());
                    	canEnter = false;
                    	break;
	    			}
	    		}
	    	}
	    }
	    
	    if(inZone){
	    	return canEnter;
	    }
	    player.sendMessage(ChatColor.GRAY + "You can not access area outside of the the borders");
        return false;
	}
	
	public static void readBorders(final File borderFile)
    {
        final Gson gson = new Gson();
        if (borderFile.exists())
        {
            Scanner scan;
            try
            {
                scan = new Scanner(borderFile);
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
                    final Border border = gson.fromJson(scan.nextLine(), Border.class);
                    addBorder(border);
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

    public static void saveBorders(final File borderfile)
    {
    	Plugin plugin = Bukkit.getPluginManager().getPlugin("DreamGuest"); 
        final Gson gson = new Gson();
        if (!borderfile.exists())
        {
            try
            {
                borderfile.createNewFile();
            }
            catch (final IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
        else
        {
            if (!borderfile.delete())
            {
                plugin.getLogger().severe("Can not save config files");
            }
            try
            {
                borderfile.createNewFile();
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
            pw = new PrintWriter(borderfile);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return;
        }
        for (final Border b : borders)
        {
                pw.println(gson.toJson(b));
        }
        pw.close();
    }

    /**
     * Gets the file that hold saved zones.
     *
     * @return Zone saves file
     */
    public static File getBorderFile()
    {
        return BORDER_FILE;
    }
}
