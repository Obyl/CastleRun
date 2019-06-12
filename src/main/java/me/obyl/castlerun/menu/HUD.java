package me.obyl.castlerun.menu;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Sprite;

public class HUD{

    private Game game;
    private Sprite heartLeftEmpty, heartLeftFull, heartRightEmpty, heartRightFull;

    public HUD(Game game){
        this.game = game;

        heartLeftEmpty = new Sprite("/textures/player/heart_left_empty.png");
        heartLeftFull = new Sprite("/textures/player/heart_left_full.png");
        heartRightEmpty = new Sprite("/textures/player/heart_right_empty.png");
        heartRightFull = new Sprite("/textures/player/heart_right_full.png");
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
    }

}