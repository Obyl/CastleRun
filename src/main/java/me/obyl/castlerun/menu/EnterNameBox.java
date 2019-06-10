package me.obyl.castlerun.menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import me.obyl.castlerun.GameState;
import me.obyl.castlerun.graphics.Font;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.menu.utils.Button;
import me.obyl.castlerun.utils.Save;

public class EnterNameBox implements KeyListener {

    private SaveMenu menu;
    private int index;
    public String name;
    public boolean show;
    public Sprite title, display;
    public Button ok, cancel;

    public EnterNameBox(SaveMenu menu, int index) {
        this.menu = menu;
        this.index = index;

        title = Font.createSprite("Enter your name:", 250, 15, 0xCBD8F2);
        ok = new Button(171, 62 + (index * 56), "OK", "normal");
        ok.action = () -> {
            menu.data[index] = new Save();
            menu.data[index].name = name;
            menu.data[index].index = index;
            menu.game.player.loadSave(menu.data[index]);
            menu.game.state = GameState.PLAYING;
        };
        cancel = new Button(171, 85 + (index * 56), "Cancel", "normal");
        cancel.action = () -> {
            show = false;
            name = "";
        };
    }

    public void tick(){
        ok.tick();
        cancel.tick();
    }

    public void render(){
        menu.game.display.drawSprite(title, 4, 60 + (index * 56));
        ok.render(menu.game.display);
        cancel.render(menu.game.display);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}