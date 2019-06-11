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
    public Sprite title, display, carat;
    public Button ok, cancel;
    private int trailingSpaces;

    public EnterNameBox(SaveMenu menu, int index) {
        this.menu = menu;
        this.index = index;
        name = "";

        title = Font.createSprite("Enter your name:", 250, 15, 0xCBD8F2);
        carat = Font.createSprite("_", 10, 15, 0xCBD8F2);
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
            display = Font.createSprite(name, 250, 15, 0xCBD8F2);
        };

        menu.game.addKeyListener(this);
    }

    public void tick(){
        ok.tick();
        cancel.tick();
    }

    public void render(){
        menu.game.display.drawSprite(title, 4, 60 + (index * 56));
        ok.render(menu.game.display);
        cancel.render(menu.game.display);

        if(display != null && !name.isEmpty()){
            menu.game.display.drawSprite(display, 4, 80 + (index * 56));
            if(name.length() < 15)
                menu.game.display.drawSprite(carat, 5 + display.width + trailingSpaces * 6, 80 + (index * 56));
        }else{
            menu.game.display.drawSprite(carat, 4 + trailingSpaces * 6, 80 + (index * 56));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!show)
            return;

        int character = e.getKeyChar();

        if(character == 32)
            trailingSpaces++;
        else
            trailingSpaces = 0;

        if(character >= 32 && character <= 126 && name.length() < 15){
            name += (char) character;
        }else if(character == 8){
            name = name.substring(0, name.length() - 1);
            for(int i = name.length(); i > 0; i--){
                if(name.substring(i - 1, i).equals(" "))
                    trailingSpaces++;
                else
                    break;
            }
        }

        display = Font.createSprite(name, 250, 15, 0xCBD8F2);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}