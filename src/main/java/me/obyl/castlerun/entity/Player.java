package me.obyl.castlerun.entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.gson.Gson;

import me.obyl.castlerun.graphics.Animation;
import me.obyl.castlerun.utils.Save;
import me.obyl.castlerun.utils.TileData;

public class Player{

    public final Animation[] animations = new Animation[4];
    public String name;
    public int x, y;
    public int health, maxHealth;
    public int facing;
    public int keys;
    public boolean hammer, cannon, greatSword, bigKey;
    public HashMap<String, TileData[]> levelData;

    public Player(Gson gson){
        Save save = gson.fromJson(
            new BufferedReader(new InputStreamReader(Player.class.getResourceAsStream("/data/default_save.json"))), 
            Save.class);
        loadSave(save);
    }

    public void loadSave(Save save){
        this.name = save.name;
        this.x = save.x;
        this.y = save.y;
        this.facing = save.facing;
        this.keys = save.keys;
        this.hammer = save.hammer;
        this.cannon = save.cannon;
        this.greatSword = save.greatSword;
        this.bigKey = save.bigKey;
        this.levelData = save.levelData;
    }
}