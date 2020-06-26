package com.miketheshadow.coinbukkit.listeners;

import com.miketheshadow.coinbukkit.OpenPurse;
import com.miketheshadow.coinbukkit.util.Purse;
import com.miketheshadow.coinbukkit.util.PurseDBHandler;
import com.miketheshadow.complexproficiencies.utils.CustomUser;
import com.miketheshadow.complexproficiencies.utils.DBHandlers.UserDBHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryListener implements Listener {

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getClick() != ClickType.RIGHT && event.getClick() != ClickType.LEFT && event.getClick() != ClickType.SHIFT_LEFT) return;
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory() != player.getOpenInventory().getTopInventory()) return;
        if(event.getCurrentItem() == null || !event.getView().getTitle().equals("Purse Menu") || event.getCurrentItem().getType() == Material.AIR) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();

        List<String> lore = clickedItem.getItemMeta().getLore();
        int purseAmount = Integer.parseInt(lore.get(0).replace("Total: ",""));

        String purseName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        if(purseAmount == 0) {
            player.sendMessage(ChatColor.RED + "You don't have any more purses of that type!");
        } else {
            int amount = 1;
            if(event.getClick() == ClickType.RIGHT) {
                amount = 5;
            } else if(event.getClick() == ClickType.SHIFT_LEFT) {
                CustomUser user = UserDBHandler.getPlayer(player);
                amount = user.getPurses().get(purseName);
            }
            Purse purse = PurseDBHandler.getPurse(purseName);
            if(purse == null) {
                player.sendMessage(ChatColor.RED + "Purse error: purse of type " + purseName + " does not exist!");
                return;
            }
            boolean didOpen = OpenPurse.openPurses(purse,player,amount);
            if(didOpen) {
                ItemMeta meta = clickedItem.getItemMeta();
                lore.set(0,"Total: " + (purseAmount -amount));
                meta.setLore(lore);
                clickedItem.setItemMeta(meta);
            }
        }
    }
}
