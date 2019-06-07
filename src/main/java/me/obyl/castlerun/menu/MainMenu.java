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
    private Button optionsButton;
    private Button quitButton;
    private Button openEditor;

    private boolean transToSaves;

    public MainMenu(Game game){
        this.game = game;

        skyLayer = new Sprite("/textures/menu/title_sky.png");
        castleLayer = new Sprite("/textures/menu/title_castle.png");
        titleText = new Sprite("/textures/menu/title2.png");
        titleText.opacity = 0.0f;

        creditText = Font.createSprite("by Oliver Byl", 100, 30, 0xCBD8F2);
        creditText.opacity = 0.0f;

        playButton = new Button(162, 85, "Play", "normal");
        playButton.sprite.opacity = 0.0f;
        playButton.action = () -> {
            transToSaves = true;;
        };

        optionsButton = new Button(162, 115, "Options", "normal");
        optionsButton.sprite.opacity = 0.0f;

        quitButton = new Button(162, 145, "Quit", "normal");
        quitButton.sprite.opacity = 0.0f;
        quitButton.action = () -> {
            System.exit(0);
        };

        openEditor = new Button(2, 202, "Open Editor", "normal");
        openEditor.sprite.opacity = 0.0f;
        openEditor.action = LevelEditor::new;
    }

    public void tick(){
        if(yScroll < 229){
            yScroll += yScrollChange;
            yScrollChange += 0.01;
        }else if(titleText.opacity < 1.0f){
            titleText.opacity += 0.02;
            creditText.opacity += 0.02;
            playButton.sprite.opacity += 0.02;
            optionsButton.sprite.opacity += 0.02;
            quitButton.sprite.opacity += 0.02;
            openEditor.sprite.opacity += 0.02;
        }else{
            playButton.tick();
            optionsButton.tick();
            quitButton.tick();
            openEditor.tick();
        }

        if(transToSaves){
            if(creditText.opacity > 0.0f){
                creditText.opacity -= 0.02;
                playButton.sprite.opacity -= 0.02;
                optionsButton.sprite.opacity -= 0.02;
                quitButton.sprite.opacity -= 0.02;
                openEditor.sprite.opacity -= 0.02;
            }else{
                game.state = GameState.VIEW_SAVES;
                transToSaves = false;
            }
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
        optionsButton.render(game.display);
        quitButton.render(game.display);
        openEditor.render(game.display);
    }

}