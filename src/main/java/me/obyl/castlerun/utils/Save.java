package me.obyl.castlerun.utils;

import java.util.HashMap;

public class Save{
    public int index;
    public String name;
    public String level = "area2";
    public int x = 32, y = 32;
    public int health = 6, maxHealth = 6;
    public int facing;
    public int keys;
    public int equippedWeapon;
    public boolean hammer, cannon, greatSword, bigKey;
    public boolean master, chef, king;
    public HashMap<String, TileData[]> levelData;
}