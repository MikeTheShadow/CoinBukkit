package com.miketheshadow.coinbukkit;

import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.entity.Player;

public class CoinPurse {
    String rarity;
    int minValue;
    int maxValue;
    int laborReq;
    int amount;

    public CoinPurse(String rarity, int minValue, int maxValue, int laborReq) {
        this.rarity = rarity;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.laborReq = laborReq;
    }

    public int open(Player player) {
        if(laborCheck(player))return 0;
        return  (int)(Math.random()*((this.maxValue-this.minValue)+1))+this.minValue;
    }

    public int openAll(Player player) {
        if(laborCheck(player))return 0;
        int total = 0;
        while (amount != 0){
            amount--;
            total += (int)(Math.random()*((this.maxValue-this.minValue)+1))+this.minValue;
        }
        return total;
    }

    public boolean laborCheck(Player player){
        CustomUser user = UserAPI.getUser(player);
        int labor = user.getLabor();
        return labor - laborReq <= 0;
    }
}
