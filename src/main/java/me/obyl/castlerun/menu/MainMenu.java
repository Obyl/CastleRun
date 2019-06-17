package me.obyl.castlerun.menu;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.GameState;
import me.obyl.castlerun.graphics.Font;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.level.LevelEditor;
import me.obyl.castlerun.menu.utils.Button;

public class MainMenu {

    private float yScroll;
    private float yScrollChange = 0.3f;

    private Game game;
    public Sprite skyLayer;
    public Sprite castleLayer;
    public Sprite titleText;
    public Sprite creditText;

    private Button playButton;
    private Button aboutButton;
    private Button quitButton;
    private Button openEditor;

    private boolean transToSaves, transToAbout;

    public MainMenu(Game game){
        this.game = game;

        skyLayer = new Sprite("/textures/menu/title_sky.png");
        castleLayer = new Sprite("/textures/menu/title_castle.png");
        titleText = new Sprite("/textures/menu/title2.png");
        titleText.opacity = 0.0f;

        creditText = Font.createSprite("by Oliver Byl", 100, 30, 0xCBD8F2);

        playButton = new Button(162, 85, "Play", "normal");
        playButton.action = () -> {
            transToSaves = true;
        };

        aboutButton = new Button(162, 115, "About", "normal");
        aboutButton.action = () -> {
            transToAbout = true;
        };

        quitButton = new Button(162, 145, "Quit", "normal");
        quitButton.action = () -> {
            System.exit(0);
        };

        openEditor = new Button(2, 202, "Open Editor", "normal");
        openEditor.action = LevelEditor::new;

        hideAll();
    }

    public void hideAll(){
        creditText.opacity = 0.0f;
        playButton.sprite.opacity = 0.0f;
        aboutButton.sprite.opacity = 0.0f;
        quitButton.sprite.opacity = 0.0f;
        openEditor.sprite.opacity = 0.0f;
    }

    public void tick(){
        if(transToSaves || transToAbout){
            if(creditText.opacity > 0.0f){
                creditText.opacity -= 0.02;
                playButton.sprite.opacity -= 0.02;
                aboutButton.sprite.opacity -= 0.02;
                quitButton.sprite.opacity -= 0.02;
                openEditor.sprite.opacity -= 0.02;
            }else{
                if(transToSaves){
                    game.state = GameState.VIEW_SAVES;
                    transToSaves = false;
                }else if(transToAbout){
                    game.state = GameState.VIEW_ABOUT;
                    game.aboutMenu.hideAll();
                    transToAbout = false;
                }
            }
        }else if(yScroll < 229){
            yScroll += yScrollChange;
            yScrollChange += 0.01;
        }else if(creditText.opacity < 1.0f){
            titleText.opacity += 0.02;
            creditText.opacity += 0.02;
            playButton.sprite.opacity += 0.02;
            aboutButton.sprite.opacity += 0.02;
            quitButton.sprite.opacity += 0.02;
            openEditor.sprite.opacity += 0.02;
        }else{
            playButton.tick();
            aboutButton.tick();
            quitButton.tick();
            openEditor.tick();
        }
    }

    public void render(){
        if(yScroll < 229)
            game.display.drawSprite(game.loadingMenu.background, 0, 0 - yScroll);

        if(yScroll > 7){
            game.display.drawSprite(skyLayer, 0, 231 - yScroll);
            game.display.drawSprite(titleText, 0, 230 - yScroll);
        }

        if(yScroll > 23)
            game.display.drawSprite(castleLayer, 6, 249 - yScroll);

        game.display.drawSprite(creditText, 191, 441 - yScroll);

        playButton.render(game.display);
        aboutButton.render(game.display);
        quitButton.render(game.display);
        openEditor.render(game.display);
    }

}