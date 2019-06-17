package me.obyl.castlerun.menu;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.GameState;
import me.obyl.castlerun.graphics.Font;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.menu.utils.Button;

public class AboutMenu{

    public Game game;
    public Button back;
    public Sprite controlsMove, controlsScroll, controlsAtk, haveFun;

    private boolean transBack;

    public AboutMenu(Game game){
        this.game = game;

        back = new Button(2, 202, "Back", "normal");
        back.action = () -> {
            transBack = true;
        };

        controlsMove = Font.createSprite("Move: WASD", 256, 15, 0xCBD8F2);
        controlsScroll = Font.createSprite("Select weapon: Scroll wheel", 256, 15, 0xCBD8F2);
        controlsAtk = Font.createSprite("Attack (longsword only): Left mouse button", 256, 15, 0xCBD8F2);
        haveFun = Font.createSprite("Have fun! -Oliver", 256, 15, 0xCBD8F2);

        hideAll();
    }

    public void hideAll(){
        back.sprite.opacity = 0.0f;
        controlsMove.opacity = 0.0f;
        controlsScroll.opacity = 0.0f;
        controlsAtk.opacity = 0.0f;
        haveFun.opacity = 0.0f;
    }

    public void tick(){
        if(transBack){
            if(back.sprite.opacity > 0.0f){
                back.sprite.opacity -= 0.02f;
                controlsMove.opacity -= 0.02f;
                controlsScroll.opacity -= 0.02f;
                controlsAtk.opacity -= 0.02f;
                haveFun.opacity -= 0.02f;
            }else{
                transBack = false;
                game.mainMenu.hideAll();
                game.state = GameState.MAIN_MENU;
            }
        }else if(back.sprite.opacity < 1.0f){
            back.sprite.opacity += 0.02f;
            controlsMove.opacity += 0.02f;
            controlsScroll.opacity += 0.02f;
            controlsAtk.opacity += 0.02f;
            haveFun.opacity += 0.02f;
        }else{
            back.tick();
        }
    }

    public void render(){
        game.display.drawSprite(game.mainMenu.skyLayer, 0, 0);
        game.display.drawSprite(game.mainMenu.castleLayer, 6, 18);
        game.display.drawSprite(game.mainMenu.titleText, 0, -1);

        back.render(game.display);

        game.display.drawSprite(controlsMove, 24, 60);
        game.display.drawSprite(controlsScroll, 24, 80);
        game.display.drawSprite(controlsAtk, 24, 100);
        game.display.drawSprite(haveFun, 24, 140);
    }
}