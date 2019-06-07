package me.obyl.castlerun.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.gson.Gson;

import me.obyl.castlerun.graphics.Animation;
import me.obyl.castlerun.utils.Save;
import me.obyl.castlerun.utils.TileData;

public class Player{

    public final Animation[] animations = new Animation[4];
    public int x, y;
    public int health, maxHealth;
    public int facing;
    public int keys;
    public boolean hammer, cannon, greatSword, bigKey;
    public HashMap<String, TileData[]> levelData;

    public Player(Gson gson){
        File saveFile = new File("save.json");
        Save saveData = null;
        if(saveFile.exists()){
            try{
                saveData = gson.fromJson(
                    new FileReader(saveFile),
                    Save.class);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }else{
            saveData = gson.fromJson(
                new BufferedReader(new InputStreamReader(Player.class.getResourceAsStream("/data/default_save.json"))), 
                Save.class);
        }

        this.x = saveData.x;
        this.y = saveData.y;
        this.facing = saveData.facing;
        this.keys = saveData.keys;
        this.hammer = saveData.hammer;
        this.cannon = saveData.cannon;
        this.greatSword = saveData.greatSword;
        this.bigKey = saveData.bigKey;
        this.levelData = saveData.levelData;
    }
}