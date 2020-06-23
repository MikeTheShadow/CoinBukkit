package com.miketheshadow.coinbukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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

}
