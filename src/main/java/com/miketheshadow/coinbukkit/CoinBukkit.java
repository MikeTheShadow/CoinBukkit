package com.miketheshadow.coinbukkit;

import com.miketheshadow.coinbukkit.listeners.CommandListener;
import com.miketheshadow.coinbukkit.listeners.MobDeathListener;
import com.miketheshadow.complexproficiencies.listener.CustomCommandListener;
import com.miketheshadow.complexproficiencies.listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CoinBukkit extends JavaPlugin {

    public static FileConfiguration typeYML;

    //Create a singleton here.
    public static CoinBukkit INSTANCE;

    public CoinBukkit getInstance() {
        if(INSTANCE == null) { INSTANCE = this; }
        return INSTANCE;
    }

    public void onEnable() {

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new MobDeathListener(), this);

        this.getCommand("purse").setExecutor(new CommandListener(this));
        this.getCommand("purselist").setExecutor(new CommandListener(this));
        this.getCommand("openpurse").setExecutor(new CommandListener(this));
        this.getCommand("addpurse").setExecutor(new CommandListener(this));
        loadTypes();
    }

    public void onDisable(){
        saveConfig();
    }

    public void loadTypes(){
        File typeFile = new File(getDataFolder(),"Purses.yml");
        if(!getDataFolder().exists())getDataFolder().mkdir();
        if(!typeFile.exists()) {
            try {
                typeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            typeYML = YamlConfiguration.loadConfiguration(typeFile);
            typeYML.set("TEST","");
            typeYML.set("TEST.RARITY","§6LEGENDARY");
            typeYML.set("TEST.MIN",10);
            typeYML.set("TEST.MAX",12);
            typeYML.set("TEST.COST",50);
            saveConfig();
        }
        typeYML = YamlConfiguration.loadConfiguration(typeFile);

    }
    public void saveConfig() {
        File typeFile = new File(getDataFolder(),"Purses.yml");
        try {
            typeYML.save(typeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
