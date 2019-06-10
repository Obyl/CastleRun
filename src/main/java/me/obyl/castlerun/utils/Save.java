package me.obyl.castlerun.utils;

import java.util.HashMap;

public class Save{
    public int index;
    public String name;
    public String level;
    public int x, y;
    public int health, maxHealth;
    public int facing;
    public int keys;
    public boolean hammer, cannon, greatSword, bigKey;
    public boolean master, chef, king;
    public HashMap<String, TileData[]> levelData;
}