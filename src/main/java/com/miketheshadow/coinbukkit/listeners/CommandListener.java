package com.miketheshadow.coinbukkit.listeners;

import com.miketheshadow.coinbukkit.CoinBukkit;
import com.miketheshadow.coinbukkit.OpenPurse;
import com.miketheshadow.coinbukkit.util.Purse;
import com.miketheshadow.coinbukkit.util.PurseDBHandler;
import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CommandListener implements CommandExecutor
{
    private final CoinBukkit coinBukkit;

    public CommandListener(CoinBukkit coinBukkit) {
        this.coinBukkit = coinBukkit;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("openpurse")) {
            if (!(sender instanceof Player)) return false;
            if(args.length < 1) return false;
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
            Player player = (Player)sender;
            CustomUser customUser = UserAPI.getUser(player);
            HashMap<String,Integer> purseMap = customUser.getPurses();
            List<String> purseList = new ArrayList<>();
            for (Map.Entry<String,Integer> purse: purseMap.entrySet()) {
                if(purse.getValue() > 0){
                    purseList.add(purse.getKey());
                }
            }
            if(purseList.size() == 0) {
                player.sendMessage(ChatColor.RED + "You don't have any purses!");
                return true;
            }
            Inventory inventory = Bukkit.createInventory(player,54,"Purse Menu");
            for (String purse : purseList) {
                int cost = PurseDBHandler.getPurse(purse).getLaborCost();
                inventory.addItem(register(Material.BLUE_SHULKER_BOX,purse,purseMap.get(purse),cost));
            }
            player.openInventory(inventory);
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
                CustomUser user = UserAPI.getUser(player);
                if(user.getLevelXP()[0] - level > 10 && user.getLevelXP()[0] - level < -10) {

                }
                try {
                    PurseDBHandler.getPurseByMinCost(level).getName();
                } catch (Exception ignored) {
                    player.sendMessage(ChatColor.RED + "PURSE ERROR! Please report as such!" + ignored.getMessage());
                    return true;
                }
                Purse purse = PurseDBHandler.getPurseByMinCost(level);
                Random random = new Random();
                if(random.nextFloat() > purse.getChance()) {
                    player.sendMessage(ChatColor.GOLD + "You got a " + CoinBukkit.getColor(purse.getColor()) + purse.getName() + ChatColor.GOLD + " purse!");

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
            return true;
        }
        return false;
    }
    public static ItemStack register(Material material, String name,int amount,int laborCost) {
        List<String> list = new ArrayList<>();
        list.add("Total: " + amount);
        list.add("Cost: " + laborCost + " labor");
        list.add(ChatColor.GRAY + "Left click to open 1");
        list.add(ChatColor.GRAY + "Right click to open 5");
        list.add(ChatColor.GRAY + "Shift left click to open all");

        //create the item
        ItemStack item = new ItemStack(material);
        //add tags
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(list);
        item.setItemMeta(meta);

        return item;
    }
}
