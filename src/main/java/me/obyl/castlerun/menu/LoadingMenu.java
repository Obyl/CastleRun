package me.obyl.castlerun.menu;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.GameState;
import me.obyl.castlerun.graphics.Sprite;

public class LoadingMenu {

    /*
    Included in count:
    - Event Listeners
    - Font
    - Sprites
    - Player
    - HUD

    Not yet included:
    - Menus
    - Levels
     */
    private static final int MAX_LOAD_COUNT = 1028;

    private Game game;
    public Sprite background;
    private Sprite loadBarBackground;
    private Sprite loadBar;
    private int loadCount;
    private int maxX;

    public LoadingMenu(Game game) {
        this.game = game;

        background = new Sprite("/textures/menu/loading_screen.png");
        loadBarBackground = new Sprite(204, 18, 0x587566);
        loadBar = new Sprite(200, 14, 0x3A453E);
    }

    public void addToLoadCount(){
        loadCount++;

        float percent = (float) loadCount / (float) MAX_LOAD_COUNT;
        int newMaxX = (int)(percent * loadBar.width);
        for(int x = maxX; x < newMaxX ; x++)
            for(int y = 0; y < loadBar.height; y++)
                loadBar.pixels[x + y * loadBar.width] = 0x86E06D;

        maxX = newMaxX;
    }

    public void tick(){
        if(loadCount < MAX_LOAD_COUNT)
            return;

        if(loadBar.opacity > 0){
            loadBarBackground.opacity -= 0.01;
            loadBar.opacity -= 0.01;
        }else{
            game.state = GameState.MAIN_MENU;
        }
    }

    public void render() {
        game.display.drawSprite(background, 0, 0);
        game.display.drawSprite(loadBarBackground, 26, 140);
        game.display.drawSprite(loadBar, 28, 142);
    }
}