package me.obyl.castlerun.utils;

import java.util.HashMap;

public class Save{
    public boolean started;
    public String level;
    public int x, y;
    public int health, maxHealth;
    public int facing;
    public int keys;
    public boolean hammer, cannon, greatSword, bigKey;
    public HashMap<String, TileData[]> levelData;
}