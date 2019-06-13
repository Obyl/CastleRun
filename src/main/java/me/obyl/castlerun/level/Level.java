package me.obyl.castlerun.level;

import com.google.gson.Gson;

import me.obyl.castlerun.Game;
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

    public void render(Display display, int playerX, int playerY){
        int x0 = (playerX >> 4) - 8;
        int y0 = (playerY >> 4) - 6;

        int x1 = x0 + 18;
        int y1 = y0 + 15;

        int xScroll = (Game.WIDTH >> 1) - 16 - playerX;
        int yScroll = (Game.HEIGHT >> 1) - 16 - playerY;

        for(int y = y0; y < y1; y++){
            for(int x = x0; x < x1; x++){
                if(y < 0 || x < 0 || y >= height || x >= width)
                    continue;

                int currentTile = x + y * width;
                display.drawSprite(tileset[sprites1[currentTile]], (x << 4) + xScroll, (y << 4) + yScroll);
                display.drawSprite(tileset[sprites2[currentTile]], (x << 4) + xScroll, (y << 4) + yScroll);
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