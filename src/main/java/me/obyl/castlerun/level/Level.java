package me.obyl.castlerun.level;

import com.google.gson.Gson;

import me.obyl.castlerun.graphics.Display;
import me.obyl.castlerun.graphics.Sprite;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Level {

    public static HashMap<String, Level> levels = new HashMap<>();
    public static Sprite[] tileset = new Sprite[1024];

    public String name;
    public int width, height;
    public int[] sprites1;
    public int[] sprites2;
    public boolean[] collision;
    public int[] interact;

    public Level(String name, int width, int height){
        this.name = name;
        levels.put(name, this);

        this.width = width;
        this.height = height;

        sprites1 = new int[width * height];
        sprites2 = new int[width * height];
        collision = new boolean[width * height];

        for(int i = 0; i < sprites1.length; i++)
            sprites1[i] = 1;
    }

    public void render(Display display){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                display.drawSprite(tileset[sprites1[x + y * width]], x << 4, y << 4);
                display.drawSprite(tileset[sprites2[x + y * width]], x << 4, y << 4);
            }
        }
    }

    public static void loadLevelFromFile(Gson gson, String filePath){
        Level level = gson.fromJson(
            new BufferedReader(new InputStreamReader(Level.class.getResourceAsStream(filePath))), 
            Level.class);

        levels.put(level.name, level);
    }
}