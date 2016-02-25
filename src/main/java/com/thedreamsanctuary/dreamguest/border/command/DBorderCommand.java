package com.thedreamsanctuary.dreamguest.border.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thedreamsanctuary.dreamguest.Module;
import com.thedreamsanctuary.dreamguest.border.Border;
import com.thedreamsanctuary.dreamguest.border.handlers.BorderHandler;
import com.thedreamsanctuary.dreamguest.CommandHandler;

public class DBorderCommand extends CommandHandler implements TabCompleter{
	private HashMap<String, Argument> arguments = new HashMap<String, Argument>();
	private final String usage = "/dborder <create|delete|edit|list> [arguments] ";
	public DBorderCommand(Module m) {
		super(m);
		arguments.put("create", new Create());
		arguments.put("delete", new Delete());
		arguments.put("edit", new Edit());
		arguments.put("list", new List());
	}

	public void printUsage(Player player, String usage){
		player.sendMessage(ChatColor.RED + "Usage: " + usage);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Must be ingame to use!");
			return true;
		}
		Player player = (Player) sender;
		if(args.length < 1||!arguments.containsKey(args[0].toLowerCase())){
			printUsage(player, usage);
			return true;
		}
		Argument arg = arguments.get(args[0].toLowerCase());
		args = Arrays.copyOfRange(args, 1, args.length);
		if(args.length!=arg.getArgLength()){
			printUsage(player, arg.getUsage());
			return true;
		}
		arg.run(player, args);
		return true;
	}

	@Override
	public java.util.List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		java.util.List<String> output = new ArrayList<String>();
		output.addAll(arguments.keySet());
		return output;
	}

}
abstract class Argument{
	abstract void run(Player player, String[] args);
	abstract int getArgLength();
	abstract String getUsage();
}
class Create extends Argument{
	final int arguments = 5;
	final String usage = "/dborder create <NAME> <X1> <Z1> <X2> <Z2>";
	
	@Override
	void run(Player player, String[] args) {
		final int x1, z1, x2, z2;
        try
        {
            x1 = Integer.parseInt(args[1]);
            z1 = Integer.parseInt(args[2]);
            x2 = Integer.parseInt(args[3]);
            z2 = Integer.parseInt(args[4]);
        }
        catch (final Exception e)
        {
            player.sendMessage(ChatColor.GREEN + "Incorrect parameters " + ChatColor.GRAY + this.getUsage());
            return;
        }
        Border newBorder = new Border(args[0], x1, z1, x2, z2, player.getWorld().getUID());
        BorderHandler.addBorder(newBorder);
        BorderHandler.save();
        player.sendMessage(ChatColor.GRAY + "Zone successfully created!");
        Bukkit.getLogger().info("Player created border " + newBorder);
	}

	@Override
	int getArgLength() {
		return arguments;
	}

	@Override
	String getUsage() {
		return usage;
	}
	
}
class Delete extends Argument{
	final int arguments = 1;
	final String usage = "/dborder delete <NAME>";
	
	@Override
	void run(Player player, String[] args) {
		final Border oldBorder = BorderHandler.getBorder(args[0]);
		if(oldBorder != null){
			BorderHandler.removeBorder(oldBorder);
			player.sendMessage(ChatColor.GRAY + "Zone sucessfully removed");
			BorderHandler.save();
			Bukkit.getLogger().info("Player removed border " + oldBorder);
		}else{
			player.sendMessage(ChatColor.GREEN + "Incorrect parameters " + ChatColor.GRAY + this.getUsage());
		}
		
	}

	@Override
	int getArgLength() {
		return arguments;
	}

	@Override
	String getUsage() {
		return usage;
	}
	
}
class Edit extends Argument{
	final int arguments = 5;
	final String usage = "/dborder edit <NAME> <X1> <Z1> <X2> <Z2>";
	
	@Override
	void run(Player player, String[] args) {
		final Border oldBorder = BorderHandler.getBorder(args[0]);
		if(oldBorder != null){
			final String borderName = oldBorder.getName();
			final UUID worldID = oldBorder.getWorldID();
			final int x1, z1, x2, z2;
			try
            {
                x1 = Integer.parseInt(args[1]);
                z1 = Integer.parseInt(args[2]);
                x2 = Integer.parseInt(args[3]);
                z2 = Integer.parseInt(args[4]);
            }
            catch (final Exception e)
            {
                player.sendMessage(ChatColor.GREEN + "Incorrect parameters " + ChatColor.GRAY + "/vBorder <create:remove:edit> [name] x z x z");
                return;
            }
			BorderHandler.removeBorder(oldBorder);
			BorderHandler.addBorder(new Border(borderName, x1, z1, x2, z2, worldID));
			BorderHandler.save();
			player.sendMessage(ChatColor.GRAY + "Zone sucessfully edited");
			Bukkit.getLogger().info("Player edited zone " + oldBorder.getName() + ", it is now " + BorderHandler.getBorder(oldBorder.getName()));
		}else{
			player.sendMessage(ChatColor.RED + "No border named \"" + args[0] + "\" exists!");
		}
		
	}

	@Override
	int getArgLength() {
		return arguments;
	}

	@Override
	String getUsage() {
		return usage;
	}
	
}

class List extends Argument{
	final int arguments = 0;
	final String usage = "/dborder list";
	
	@Override
	void run(Player player, String[] args) {
		for(String borderInfo : BorderHandler.getBorders()) 
	    {
	        player.sendMessage(borderInfo);
	    }
	}

	@Override
	int getArgLength() {
		return arguments;
	}

	@Override
	String getUsage() {
		return usage;
	}
}
