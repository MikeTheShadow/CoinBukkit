package com.miketheshadow.coinbukkit;

import com.miketheshadow.coinbukkit.util.Purse;
import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class OpenPurse {

    public static void openPurses(Purse purse, Player player, int amount) {

        int min = purse.getMinMoney();
        int max = purse.getMaxMoney();
        int laborReq = purse.getLaborCost();
        String color = purse.getColor();
        String purseName = purse.getName();
        assert color != null;
        String coloredText = CoinBukkit.getColor(color) + purseName;
        CustomUser user = UserAPI.getUser(player);
        if(!user.getPurses().containsKey(purseName)){
            player.sendMessage(ChatColor.RED + "You don't have any purses of this type!");
            return;
        }
        int playerAmount = user.getPurses().get(purseName);
        if(playerAmount == 0){
            player.sendMessage(ChatColor.RED + "You don't have any purses of this type!");
            return;
        }
        int labor = user.getLabor();
        if(amount > playerAmount) amount = playerAmount;
        if(amount == 1) {
            if(labor - laborReq < 0){
                player.sendMessage(ChatColor.RED + "You don't have enough labor to open this purse!\n" + labor + "\\" + laborReq);
                return;
            }
            user.setLabor(labor - laborReq);
            int total = (int)(Math.random()*((max-min)+1))+min;
            user.setBalance(user.getBalance() + total);
            user.getPurses().put(purseName,user.getPurses().get(purseName) - 1);
            //player.sendMessage("You opened a(n) " + coloredText + ChatColor.RESET + " coin purse and gained: " + total + " Cor");
            player.sendMessage("You opened a " + coloredText + ChatColor.RESET + " purse!");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + user.getName() + " " + total);
            user.addExperience("larceny",laborReq,player);
            player.sendMessage(ChatColor.YELLOW + "You currently have " + ChatColor.GRAY + "[" + ChatColor.GOLD + (user.getLabor()) + ChatColor.GRAY + "/" + ChatColor.GOLD + "2000" + ChatColor.GRAY + "]" + ChatColor.YELLOW + " labor!");

        }
        else{
            if(labor - (laborReq * amount) < 0) {
                player.sendMessage(ChatColor.RED + "You don't have enough labor to open this many purses!\n" + labor + "\\" + (laborReq * amount));
                return;
            }
            int total = 0;
            int count = 0;
            for(int i = 0; i < amount;i++){
                if(playerAmount == 0) break;
                playerAmount--;
                count++;
                total += (int)(Math.random()*((max-min)+1))+min;
            }
            user.setLabor(labor - (laborReq * amount));
            user.setBalance(user.getBalance() + total);
            user.getPurses().put(purseName,user.getPurses().get(purseName) - count);
            //player.sendMessage("You opened " + amount + " " + coloredText + ChatColor.RESET + " purses and gained: " + total + " Cor");
            player.sendMessage("You opened " + amount + " " + coloredText + ChatColor.RESET + " purses!");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco give " + user.getName() + " " + total);
            user.addExperience("larceny",laborReq * amount,player);

        }
    }
}
