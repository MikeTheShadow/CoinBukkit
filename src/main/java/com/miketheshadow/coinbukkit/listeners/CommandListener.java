package com.miketheshadow.coinbukkit.listeners;

import com.miketheshadow.coinbukkit.CoinBukkit;
import com.miketheshadow.coinbukkit.OpenPurse;
import com.miketheshadow.coinbukkit.util.Purse;
import com.miketheshadow.coinbukkit.util.PurseDBHandler;
import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommandListener implements CommandExecutor
{
    private final CoinBukkit coinBukkit;

    public CommandListener(CoinBukkit coinBukkit) {
        this.coinBukkit = coinBukkit;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("openpurse")) {
            if (!(sender instanceof Player)) return false;
            Purse purse = (PurseDBHandler.getPurse(args[0]));
            if(purse == null) {
                Player player = (Player)sender;
                player.sendMessage(ChatColor.RED + "Purse " + args[0] + " does not exist!");
                return true;
            }
            if(args.length == 1){
                OpenPurse.openPurses(purse,(Player)sender,1);
                return true;
            }
            if(args.length == 2){
                int amount = Integer.parseInt(args[1]);
                OpenPurse.openPurses(purse,(Player)sender,amount);
                return true;
            }
            return false;
        }
        else if (cmd.getName().equalsIgnoreCase("purse")) {
            if (!(sender instanceof Player)) return false;
            Player player = (Player) sender;
            //player.sendMessage(ChatColor.GOLD + "You currently have: " + ChatColor.GREEN + UserDBHandler.getPlayer(player).getBalance() + ChatColor.GRAY + " Cor");
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("purselist")) {
            if (!(sender instanceof Player)) return false;
            Player player = (Player) sender;
            CustomUser customUser = UserAPI.getUser(player);
            HashMap<String,Integer> purseMap = customUser.getPurses();
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.DARK_GRAY).append("--------------------------\n");
            for (Map.Entry<String,Integer> purse: purseMap.entrySet()) {
                if(purse.getValue() > 0){
                    builder.append(ChatColor.RESET)
                            .append(CoinBukkit.getColor(PurseDBHandler.getPurse(purse.getKey()).getColor()))
                            .append(purse.getKey())
                            .append(ChatColor.RESET)
                            .append(" : ")
                            .append(ChatColor.DARK_PURPLE)
                            .append(purse.getValue())
                            .append("\n");
                }
            }
            builder.append(ChatColor.DARK_GRAY).append("--------------------------");
            if(!builder.toString().equals("")) { player.sendMessage(ChatColor.GOLD + "You currently have: \n" + builder.toString()); }
            else player.sendMessage(ChatColor.RED + "You currently have no purses!");;
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("addpurse")) {
            if(args.length == 3 && args[0].equalsIgnoreCase("level")) {
                int level = Integer.parseInt(args[1]);
                Player player = Bukkit.getPlayer(args[2]);
                try {
                    PurseDBHandler.getPurseByMinCost(level).getName();
                } catch (Exception ignored) {
                    player.sendMessage(ChatColor.RED + "PURSE ERROR! Please report as such!");
                    return true;
                }
                Purse purse = PurseDBHandler.getPurseByMinCost(level);
                Random random = new Random();
                if(random.nextFloat() > purse.getChance()) {
                    player.sendMessage(ChatColor.GOLD + "You got a " + CoinBukkit.getColor(purse.getColor()) + purse.getName() + ChatColor.GOLD + " purse!");
                    CustomUser user = UserAPI.getUser(player);
                    try {
                        int amount = user.getPurses().get(purse.getName());
                        user.getPurses().put(purse.getName(),amount + 1);
                    }
                    catch (Exception e) {
                        user.getPurses().put(purse.getName(),1);
                    }
                    UserAPI.updateUser(user);
                    return true;
                }
                else {
                    return true;
                }

            }

            return false;
        } else if (cmd.getName().equalsIgnoreCase("createpurse")) {
            if(args.length != 7) return false;
            try {
                String name = args[0];
                int maxLevel = Integer.parseInt(args[1]);
                String colorCode = args[2];
                double chance = Double.parseDouble(args[3]);
                int minMoney = Integer.parseInt(args[4]);
                int maxMoney = Integer.parseInt(args[5]);
                int cost = Integer.parseInt(args[6]);
                PurseDBHandler.addPurse(new Purse(name,maxLevel,colorCode,chance,minMoney,maxMoney,cost));
                sender.sendMessage(ChatColor.GREEN + "Added new purse " + name +"!");
            } catch (Exception ignored) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments!");
                return false;
            }
            return true;
        } else if(cmd.getName().equalsIgnoreCase("removepurse")) {
            if(args.length != 1) return false;
            if(PurseDBHandler.removePurse(args[0])) {
                sender.sendMessage(ChatColor.RED + "Removed!");
            } else {
                sender.sendMessage(ChatColor.RED + "Purse doesn't exist with name: " + args[0]);
            }
        }
        return false;
    }
}
