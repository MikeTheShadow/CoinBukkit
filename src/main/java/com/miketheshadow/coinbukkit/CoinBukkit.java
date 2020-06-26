package com.miketheshadow.coinbukkit;

import com.miketheshadow.coinbukkit.listeners.CommandListener;
import com.miketheshadow.coinbukkit.listeners.InventoryListener;
import com.miketheshadow.coinbukkit.listeners.PlayerDropItemListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CoinBukkit extends JavaPlugin {
    //Create a singleton here.
    public static CoinBukkit INSTANCE;
    public CoinBukkit getInstance() {
        if(INSTANCE == null) { INSTANCE = this; }
        return INSTANCE;
    }

    public void onEnable() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerDropItemListener(),this);
        pluginManager.registerEvents(new InventoryListener(),this);
        this.getCommand("purse").setExecutor(new CommandListener(this));
        this.getCommand("purselist").setExecutor(new CommandListener(this));
        this.getCommand("addpurse").setExecutor(new CommandListener(this));
        this.getCommand("createpurse").setExecutor(new CommandListener(this));
        this.getCommand("removepurse").setExecutor(new CommandListener(this));
    }

    public void onDisable(){
        saveConfig();
    }

    public static String getColor(String color){ return ChatColor.translateAlternateColorCodes('&', color);}
}
