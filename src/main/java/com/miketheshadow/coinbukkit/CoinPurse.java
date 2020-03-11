package com.miketheshadow.coinbukkit;

import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoinPurse {
    String rarity;
    int minValue;
    int maxValue;
    int laborReq;

    public CoinPurse(String rarity, int minValue, int maxValue, int laborReq) {
        this.rarity = rarity;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.laborReq = laborReq;
    }

    public void open(Player player,int amount) {
        CustomUser user = UserAPI.getUser(player);
        int labor = user.getLabor();
        if(amount == 1){
            if(labor - laborReq >= 0){
                player.sendMessage(ChatColor.RED + "You don't have enough labor to open this purse!");
                return;
            }
            user.setLabor(labor - laborReq);
            int total = roll();
            user.setBalance(user.getBalance() + total);
            player.sendMessage("You gained: " + total + " Cor");
        }
        else{
            if(labor - (laborReq * amount) >= 0){
                player.sendMessage(ChatColor.RED + "You don't have enough labor to open this many purses!");
                return;
            }
            int total = 0;
            for(int i = 0; i < amount;i++){
                total += roll();
            }
            user.setLabor(labor - laborReq);
            user.setBalance(user.getBalance() + total);
            player.sendMessage("You gained: " + total + " Cor");
        }
    }
    public int roll(){
        return (int)(Math.random()*((this.maxValue-this.minValue)+1))+this.minValue;
    }
}
