package com.miketheshadow.coinbukkit;

import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoinPurse {

    public static void openPurses(String rarity,Player player,int amount) {

        int min = CoinBukkit.typeYML.getInt(rarity + ".MIN");
        int max = CoinBukkit.typeYML.getInt(rarity + ".MAX");
        int laborReq = CoinBukkit.typeYML.getInt(rarity + ".COST");
        CustomUser user = UserAPI.getUser(player);
        if(!user.getPurses().containsKey(rarity)){
            player.sendMessage(ChatColor.RED + "You don't have any purses of this type!");
            return;
        }
        int playerAmount = user.getPurses().get(rarity);
        if(playerAmount == 0){
            player.sendMessage(ChatColor.RED + "You don't have any purses of this type!");
            return;
        }

        int labor = user.getLabor();
        if(amount == 1){
            if(labor - laborReq < 0){
                player.sendMessage(ChatColor.RED + "You don't have enough labor to open this purse!\n" + labor + "\\" + laborReq);
                return;
            }
            user.setLabor(labor - laborReq);
            int total = (int)(Math.random()*((max-min)+1))+min;
            user.setBalance(user.getBalance() + total);
            user.getPurses().put(rarity,user.getPurses().get(rarity) - 1);
            player.sendMessage("You opened a(n) " + rarity + " coin purse gained: " + total + " Cor");
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
            user.setLabor(labor - laborReq);
            user.setBalance(user.getBalance() + total);
            user.getPurses().put(rarity,user.getPurses().get(rarity) - count);
            player.sendMessage("You opened " + amount + " " + rarity + " purses and gained: " + total + " Cor");
        }
        UserAPI.updateUser(user);
    }

}
