package me.obyl.castlerun.menu;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Sprite;

public class HUD{

    private Game game;
    private Sprite heartLeftEmpty, heartLeftFull, heartRightEmpty, heartRightFull;
    private Sprite itemBackground, itemHighlight;
    private Sprite longSword, hammer, cannon, greatSword;

    public HUD(Game game){
        this.game = game;

        heartLeftEmpty = new Sprite("/textures/player/heart_left_empty.png");
        heartLeftFull = new Sprite("/textures/player/heart_left_full.png");
        heartRightEmpty = new Sprite("/textures/player/heart_right_empty.png");
        heartRightFull = new Sprite("/textures/player/heart_right_full.png");

        itemBackground = new Sprite("/textures/player/item_background.png");
        itemHighlight = new Sprite("/textures/player/item_highlight.png");

        longSword = new Sprite("/textures/player/longsword.png");
        hammer = new Sprite("/textures/player/hammer.png");
        cannon = new Sprite("/textures/player/cannon.png");
        greatSword = new Sprite("/textures/player/greatsword.png");
    }

    public void render(){
        int startX = 255 - ((game.player.maxHealth / 2) * 20);
        for(int h = 0; h < game.player.maxHealth; h++){
            if(h % 2 == 0){
                if(h < game.player.health)
                    game.display.drawSprite(heartLeftFull, startX + (20 * (h/2)), 2);
                else
                    game.display.drawSprite(heartLeftEmpty, startX + (20 * (h/2)), 2);
            }else{
                if(h < game.player.health)
                    game.display.drawSprite(heartRightFull, (startX + 9) + (20 * (h/2)), 2);
                else
                    game.display.drawSprite(heartRightEmpty, (startX + 9) + (20 * (h/2)), 2);
            }
        }

        game.display.drawSprite(itemBackground, 224, 25);
        game.display.drawSprite(longSword, 226, 27);
        if(game.player.hammer){
            game.display.drawSprite(itemBackground, 224, 57);
            game.display.drawSprite(hammer, 226, 59);
        }
        if(game.player.cannon){
            game.display.drawSprite(itemBackground, 224, 89);
            game.display.drawSprite(cannon, 226, 91);
        }
        if(game.player.greatSword){
            game.display.drawSprite(itemBackground, 224, 121);
            game.display.drawSprite(greatSword, 226, 123);
        }
        game.display.drawSprite(itemHighlight, 223, 24 + (32 * game.player.equippedWeapon));
    }

}