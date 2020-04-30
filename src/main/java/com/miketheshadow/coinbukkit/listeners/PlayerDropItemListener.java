package com.miketheshadow.coinbukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.List;

public class PlayerDropItemListener implements Listener {

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if(event.getAction() == InventoryAction.DROP_ALL_SLOT
            || event.getAction() == InventoryAction.DROP_ALL_CURSOR
            || event.getAction() == InventoryAction.DROP_ONE_CURSOR
            || event.getAction() == InventoryAction.DROP_ONE_SLOT) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityWacked(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        Bukkit.broadcastMessage(event.getEntity().getType().toString());
        Bukkit.broadcastMessage("Entity name: " + event.getEntity().getName());
        Bukkit.broadcastMessage("ID:" + event.getEntity().getEntityId());
        Bukkit.broadcastMessage("Type name: " + event.getEntity().getType().name());
        e.getWorld().spawnEntity(e.getLocation(), EntityType.fromId(25));
    }
}
