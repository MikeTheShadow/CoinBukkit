package com.miketheshadow.coinbukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class CoinBukkit extends JavaPlugin {

    //Create a singleton here.
    public static CoinBukkit INSTANCE;

    public CoinBukkit getInstance() {
        if(INSTANCE == null) { INSTANCE = this; }
        return INSTANCE;
    }

    public void onEnable() {

    }

    public void onDisable(){

    }


}
