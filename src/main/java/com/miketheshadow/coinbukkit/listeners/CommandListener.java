package com.miketheshadow.coinbukkit.listeners;

import com.miketheshadow.coinbukkit.CoinBukkit;
import com.miketheshadow.coinbukkit.CoinPurse;
import com.miketheshadow.complexproficiencies.ComplexProficiencies;
import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import com.miketheshadow.complexproficiencies.utils.UserDBHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandListener implements CommandExecutor
{
    private final CoinBukkit coinBukkit;

    public CommandListener(CoinBukkit coinBukkit) {
        this.coinBukkit = coinBukkit;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("openpurse")) {
            if (!(sender instanceof Player)) return false;
            if(args.length == 1){
                String rarity = args[0];
                CoinPurse.openPurses(rarity,(Player)sender,1);
                return true;
            }
            if(args.length == 2){
                String rarity = args[0];
                int amount = Integer.parseInt(args[1]);
                CoinPurse.openPurses(rarity,(Player)sender,amount);
                return true;
            }
            return false;
        }
        else if (cmd.getName().equalsIgnoreCase("purse")) {
            if (!(sender instanceof Player)) return false;
            Player player = (Player) sender;
            player.sendMessage(ChatColor.GOLD + "You currently have: " + ChatColor.GREEN + UserDBHandler.getPlayer(player).getBalance() + ChatColor.GRAY + " Cor");
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
                            .append(purse.getKey())
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
            if(args.length >= 2){
                if(CoinBukkit.typeYML.getString(args[1] + ".COST") == null){
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "PURSE TYPE DOESNT EXIST: " + args[1]);
                    return false;
                }
                Player player = Bukkit.getPlayer(args[0]);
                if(player == null)return false;
                CustomUser user = UserAPI.getUser(player);
                try {
                    int amount = user.getPurses().get(args[1]);
                    user.getPurses().put(args[1],amount + 1);
                }
                catch (Exception e) {
                    user.getPurses().put(args[1],1);
                }
                UserAPI.updateUser(user);
                if(args.length == 3){
                    double chance = Math.random();
                    double probability = Double.parseDouble(args[2]) / 100;
                    if(chance >= probability){
                        player.sendMessage(ChatColor.GOLD + "You got a " + ChatColor.RESET + args[1] + ChatColor.GOLD + " purse!");
                    }
                }
                else player.sendMessage(ChatColor.GOLD + "You got a " + ChatColor.RESET + args[1] + ChatColor.GOLD + " purse!");
                return true;
            }

            return true;
        }
        return false;
    }
}
