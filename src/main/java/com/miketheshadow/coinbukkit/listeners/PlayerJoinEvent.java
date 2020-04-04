package com.miketheshadow.coinbukkit.listeners;

import com.miketheshadow.complexproficiencies.api.UserAPI;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener
{
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        event.getPlayer().sendMessage("Welcome! Kill cows get purses! Open purses using openpurse [pursename] [amount (optional)]");
        CustomUser user =  UserAPI.getUser(event.getPlayer());
        user.setLabor(1000);
        UserAPI.updateUser(user);
    }
}
