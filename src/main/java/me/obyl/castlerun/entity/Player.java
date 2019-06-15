package me.obyl.castlerun.entity;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Animation;
import me.obyl.castlerun.graphics.Display;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.level.Level;
import me.obyl.castlerun.utils.Keyboard;
import me.obyl.castlerun.utils.Mouse;
import me.obyl.castlerun.utils.Save;
import me.obyl.castlerun.utils.TileData;

public class Player{

    private final int centreX = Game.WIDTH >> 1;
    private final int centreY = Game.HEIGHT >> 1;

    public final Animation[] animations = new Animation[48];
    public Game game;
    public int saveIndex;
    public String name;
    public int x, y;
    public int health, maxHealth;
    public int facing;
    public int keys;
    public int equippedWeapon;
    public boolean hammer, cannon, greatSword, bigKey;
    public boolean master, chef, king;
    public HashMap<String, TileData[]> levelData;
    public boolean moving, attacking;

    public Player(Game game){
        this.game = game;

        Sprite source = new Sprite("/textures/player/player.png");
        // Idle animations:
        for(int w = 0; w < 4; w++)
            for(int d = 0; d < 4; d++)
                animations[(d + 4 * w) * 3] = new Animation(20, new Sprite(source, 32, 32, 0, d + 4 * w), new Sprite(source, 32, 32, 1, d + 4 * w));

        // Walking animations:
        for(int w = 0; w < 4; w++)
            for(int d = 0; d < 4; d++)
                animations[1 + (d + 4 * w) * 3] = new Animation(11, new Sprite(source, 32, 32, 2, d + 4 * w), new Sprite(source, 32, 32, 3, d + 4 * w));

        // Attack animations:
        for(int w = 0; w < 4; w++)
            for(int d = 0; d < 4; d++)
                animations[2 + (d + 4 * w) * 3] = new Animation(5, new Sprite(source, 32, 32, 4, d + 4 * w), new Sprite(source, 32, 32, 5, d + 4 * w),
                                                                   new Sprite(source, 32, 32, 6, d + 4 * w), new Sprite(source, 32, 32, 7, d + 4 * w));
    }

    public void tick(){
        hammer = cannon = greatSword = true;
        int xd = 0;
        int yd = 0;
        moving = false;

        if(Keyboard.isKeyPressed(KeyEvent.VK_W)){
            yd = -1;
            facing = 2;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_A)){
            xd = -1;
            facing = 3;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_S)){
            yd = 1;
            facing = 0;
        }
        if(Keyboard.isKeyPressed(KeyEvent.VK_D)){
            xd = 1;
            facing = 1;
        }

        if(xd != 0 || yd != 0){
            moving = true;

            Level inLevel = Level.levels.get(game.currentLevel);

            if(xd < 0){
                if(!inLevel.collidesAt((x + xd + 8) >> 4, (y + 16) >> 4) && !inLevel.collidesAt((x + xd + 8) >> 4, (y + 29) >> 4))
                    x--;
            }else if(xd > 0){
                if(!inLevel.collidesAt((x + xd + 23) >> 4, (y + 16) >> 4) && !inLevel.collidesAt((x + xd + 23) >> 4, (y + 29) >> 4))
                    x++;
            }
            if(yd < 0){
                if(!inLevel.collidesAt((x + 8) >> 4, (y + yd + 16) >> 4) && !inLevel.collidesAt((x + 23) >> 4, (y + yd + 16) >> 4))
                    y--;
            }else if(yd > 0){
                if(!inLevel.collidesAt((x + 8) >> 4, (y + yd + 29) >> 4) && !inLevel.collidesAt((x + 23) >> 4, (y + yd + 29) >> 4))
                    y++;
            }
        }

        int mod = 0;
        if(moving)
            mod = 1;
        else if(attacking)
            mod = 2;
        animations[mod + (facing + 4 * equippedWeapon) * 3].tick();

        int maxWeapon = 0;
        if(greatSword)
            maxWeapon = 3;
        else if(cannon)
            maxWeapon = 2;
        else if(hammer)
            maxWeapon = 1;

        if(Mouse.scroll < 0 && equippedWeapon > 0)
            equippedWeapon--;
        else if(Mouse.scroll > 0 && equippedWeapon < maxWeapon)
            equippedWeapon++;
    }

    public void render(Display display){
        int mod = 0;
        if(moving)
            mod = 1;
        else if(attacking)
            mod = 2;
        animations[mod + (facing + 4 * equippedWeapon) * 3].render(display, centreX - 16, centreY - 16);
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
        this.equippedWeapon = save.equippedWeapon;
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
        game.saveMenu.data[saveIndex].equippedWeapon = this.equippedWeapon;
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