package com.miketheshadow.coinbukkit.util;

import org.bson.Document;

public class Purse {

    private String name;
    private int minLevel;
    private String color;
    private double chance;
    private int minMoney, maxMoney;
    private int laborCost;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMinLevel() { return minLevel; }
    public void setMinLevel(int minLevel) { this.minLevel = minLevel; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public double getChance() { return chance; }
    public void setChance(double chance) { this.chance = chance; }
    public int getMinMoney() { return minMoney; }
    public void setMinMoney(int minMoney) { this.minMoney = minMoney; }
    public int getMaxMoney() { return maxMoney; }
    public void setMaxMoney(int maxMoney) { this.maxMoney = maxMoney; }
    public int getLaborCost() { return laborCost; }
    public void setLaborCost(int laborCost) { this.laborCost = laborCost; }


    public Purse(String name, int minLevel, String color, double chance, int minMoney, int maxMoney, int laborCost) {
        chance = (100 - chance) / 100;
        this.name = name;
        this.minLevel = minLevel;
        this.color = color;
        this.chance = chance;
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
        this.laborCost = laborCost;
    }

    //from document
    public Purse(Document document) {
        this.name = document.getString("name");;
        this.minLevel = document.getInteger("minLevel");;
        this.color = document.getString("color");;
        this.chance = document.getDouble("chance");;
        this.minMoney = document.getInteger("minMoney");;
        this.maxMoney = document.getInteger("maxMoney");;
        this.laborCost = document.getInteger("laborCost");;
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("name",name);
        document.append("minLevel",minLevel);
        document.append("color",color);
        document.append("chance",chance);
        document.append("minMoney",minMoney);
        document.append("maxMoney",maxMoney);
        document.append("laborCost",laborCost);
        return document;
    }

}
