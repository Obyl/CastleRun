package me.obyl.castlerun.entity;

import java.util.HashMap;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Animation;
import me.obyl.castlerun.utils.Save;
import me.obyl.castlerun.utils.TileData;

public class Player{

    public final Animation[] animations = new Animation[4];
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