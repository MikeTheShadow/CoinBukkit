package com.miketheshadow.coinbukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathListener implements Listener
{
    @EventHandler
    public static void onMobDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player)return;
        Player player = event.getEntity().getKiller();
        if(player == null)return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"addpurse " + player.getName() + " TEST " + "60");
        World world = event.getEntity().getWorld();
        Location location = event.getEntity().getLocation();
        world.spawnEntity(location, EntityType.COW);
    }
}
