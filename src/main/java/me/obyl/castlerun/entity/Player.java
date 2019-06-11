package me.obyl.castlerun.entity;

import java.util.HashMap;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Animation;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.utils.Save;
import me.obyl.castlerun.utils.TileData;

public class Player{

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

    public Player(Game game){
        this.game = game;

        Sprite source = new Sprite("/textures/player/person_32.png");
        animations[0] = new Animation(-1, new Sprite(source, 32, 32, 0, 0), new Sprite(source, 32, 32, 1, 0));
        animations[1] = new Animation(-1, new Sprite(source, 32, 32, 0, 1), new Sprite(source, 32, 32, 1, 1));
        animations[2] = new Animation(-1, new Sprite(source, 32, 32, 0, 2), new Sprite(source, 32, 32, 1, 2));
        animations[3] = new Animation(-1, new Sprite(source, 32, 32, 0, 3), new Sprite(source, 32, 32, 1, 3));
        animations[4] = new Animation(-1, new Sprite(source, 32, 32, 2, 0), new Sprite(source, 32, 32, 3, 0), new Sprite(source, 32, 32, 4, 0));
        animations[5] = new Animation(-1, new Sprite(source, 32, 32, 2, 1), new Sprite(source, 32, 32, 3, 1), new Sprite(source, 32, 32, 4, 1));
        animations[6] = new Animation(-1, new Sprite(source, 32, 32, 2, 2), new Sprite(source, 32, 32, 3, 2), new Sprite(source, 32, 32, 4, 2));
        animations[7] = new Animation(-1, new Sprite(source, 32, 32, 2, 3), new Sprite(source, 32, 32, 3, 3), new Sprite(source, 32, 32, 4, 3));
    }

    public void loadSave(Save save){
        this.saveIndex = save.index;
        this.name = save.name;
        this.x = save.x;
        this.y = save.y;
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