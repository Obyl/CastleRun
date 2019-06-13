package me.obyl.castlerun.entity;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Animation;
import me.obyl.castlerun.graphics.Display;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.level.Level;
import me.obyl.castlerun.utils.Keyboard;
import me.obyl.castlerun.utils.Save;
import me.obyl.castlerun.utils.TileData;

public class Player{

    private final int centreX = Game.WIDTH >> 1;
    private final int centreY = Game.HEIGHT >> 1;

    public final Animation[] animations = new Animation[8];
    public Game game;
    public int saveIndex;
    public String name;
    public int x, y;
    public int health, maxHealth;
    public int facing;
    public int keys;
    public boolean hammer, cannon, greatSword, bigKey;
    public boolean master, chef, king;
    public HashMap<String, TileData[]> levelData;
    public boolean moving;

    public Player(Game game){
        this.game = game;

        Sprite source = new Sprite("/textures/player/person_32.png");
        animations[0] = new Animation(10, new Sprite(source, 32, 32, 0, 0), new Sprite(source, 32, 32, 1, 0));
        animations[1] = new Animation(10, new Sprite(source, 32, 32, 0, 1), new Sprite(source, 32, 32, 1, 1));
        animations[2] = new Animation(10, new Sprite(source, 32, 32, 0, 2), new Sprite(source, 32, 32, 1, 2));
        animations[3] = new Animation(10, new Sprite(source, 32, 32, 0, 3), new Sprite(source, 32, 32, 1, 3));
        animations[4] = new Animation(6, new Sprite(source, 32, 32, 2, 0), new Sprite(source, 32, 32, 3, 0), new Sprite(source, 32, 32, 4, 0));
        animations[5] = new Animation(6, new Sprite(source, 32, 32, 2, 1), new Sprite(source, 32, 32, 3, 1), new Sprite(source, 32, 32, 4, 1));
        animations[6] = new Animation(6, new Sprite(source, 32, 32, 2, 2), new Sprite(source, 32, 32, 3, 2), new Sprite(source, 32, 32, 4, 2));
        animations[7] = new Animation(6, new Sprite(source, 32, 32, 2, 3), new Sprite(source, 32, 32, 3, 3), new Sprite(source, 32, 32, 4, 3));
    }

    public void tick(){
        moving = false;
        if(Keyboard.isKeyPressed(KeyEvent.VK_W)){
            if((y + 17) > 0)
                y--;
            facing = 2;
            moving = true;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_A)){
            if((x + 11) > 0)
                x--;
            facing = 3;
            moving = true;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_S)){
            if(y + 32 < Level.levels.get(game.currentLevel).height << 4)
                y++;
            facing = 0;
            moving = true;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_D)){
            if(x + 22 < Level.levels.get(game.currentLevel).width << 4)
                x++;
            facing = 1;
            moving = true;
        }
        animations[facing + (moving ? 4 : 0)].tick();
    }

    public void render(Display display){
        animations[facing + (moving ? 4 : 0)].render(display, centreX - 16, centreY - 16);
    }

    public void loadSave(Save save){
        this.saveIndex = save.index;
        this.name = save.name;
        this.x = save.x;
        this.y = save.y;
        this.health = save.health;
        this.maxHealth = save.maxHealth;
        this.facing = save.facing;
        this.keys = save.keys;
        this.hammer = save.hammer;
        this.cannon = save.cannon;
        this.greatSword = save.greatSword;
        this.bigKey = save.bigKey;
        this.master = save.master;
        this.chef = save.chef;
        this.king = save.king;
        this.levelData = save.levelData;
    }

    public void updateSave(){
        game.saveMenu.data[saveIndex].x = this.x;
        game.saveMenu.data[saveIndex].y = this.y;
        game.saveMenu.data[saveIndex].health = this.health;
        game.saveMenu.data[saveIndex].maxHealth = this.maxHealth;
        game.saveMenu.data[saveIndex].facing = this.facing;
        game.saveMenu.data[saveIndex].keys = this.keys;
        game.saveMenu.data[saveIndex].hammer = this.hammer;
        game.saveMenu.data[saveIndex].cannon = this.cannon;
        game.saveMenu.data[saveIndex].greatSword = this.greatSword;
        game.saveMenu.data[saveIndex].bigKey = this.bigKey;
        game.saveMenu.data[saveIndex].master = this.master;
        game.saveMenu.data[saveIndex].chef = this.chef;
        game.saveMenu.data[saveIndex].king = this.king;
        game.saveMenu.data[saveIndex].levelData = this.levelData;
    }
}