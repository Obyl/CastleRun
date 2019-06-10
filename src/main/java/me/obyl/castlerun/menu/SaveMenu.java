package me.obyl.castlerun.menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.graphics.Font;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.menu.utils.Button;
import me.obyl.castlerun.utils.Save;

public class SaveMenu {

    private ArrayList<Sprite> allSprites = new ArrayList<>();
    private Sprite background;
    private Button new0, new1, new2;
    private Button play0, play1, play2;
    private Button trash0, trash1, trash2;
    private Sprite name0, name1, name2;
    private Sprite heartLeftEmpty, heartLeftFull, heartRightEmpty, heartRightFull;
    private Sprite bossDark, bossLight;

    private Game game;
    public Save[] data;

    public SaveMenu(Game game, Gson gson){
        this.game = game;

        Save[] loadedData = null;
        Type parseType = new TypeToken<Save[]>(){}.getType();
        try{
            loadedData = gson.fromJson(
                new FileReader(new File("saves.json")),
                parseType);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        data = new Save[3];
        for(int i = 0; i < 3; i++){
            try{
                data[loadedData[i].index] = loadedData[i];
            }catch(ArrayIndexOutOfBoundsException e){}
        }

        background = new Sprite("/textures/menu/saves_background.png");
        allSprites.add(background);
        new0 = new Button(228, 71, "", "new");
        allSprites.add(new0.sprite);
        new1 = new Button(228, 126, "", "new");
        allSprites.add(new1.sprite);
        new2 = new Button(228, 181, "", "new");
        allSprites.add(new2.sprite);

        if(data[0] != null){
            name0 = Font.createSprite(data[0].name, 250, 14, 0xCBD8F2);
            allSprites.add(name0);
            play0 = new Button(228, 60, "", "play");
            allSprites.add(play0.sprite);
            trash0 = new Button(228, 85, "", "trash");
            allSprites.add(trash0.sprite);
        }
        if(data[1] != null){
            name1 = Font.createSprite(data[1].name, 250, 14, 0xCBD8F2);
            allSprites.add(name1);
            play1 = new Button(228, 115, "", "play");
            allSprites.add(play1.sprite);
            trash1 = new Button(228, 140, "", "trash");
            allSprites.add(trash1.sprite);
        }
        if(data[2] != null){
            name2 = Font.createSprite(data[2].name, 250, 14, 0xCBD8F2);
            allSprites.add(name2);
            play2 = new Button(228, 170, "", "play");
            allSprites.add(play2.sprite);
            trash2 = new Button(228, 195, "", "trash");
            allSprites.add(trash2.sprite);
        }

        heartLeftEmpty = new Sprite("/textures/player/heart_left_empty.png");
        allSprites.add(heartLeftEmpty);
        heartLeftFull = new Sprite("/textures/player/heart_left_full.png");
        allSprites.add(heartLeftFull);
        heartRightEmpty = new Sprite("/textures/player/heart_right_empty.png");
        allSprites.add(heartRightEmpty);
        heartRightFull = new Sprite("/textures/player/heart_right_full.png");
        allSprites.add(heartRightFull);

        bossDark = new Sprite("/textures/menu/boss_dark.png");
        allSprites.add(bossDark);
        bossLight = new Sprite("/textures/menu/boss_light.png");
        allSprites.add(bossLight);

        for(Sprite s : allSprites)
            s.opacity = 0.0f;
    }

    public void tick(){
        if(background.opacity < 1.0f){
            for(Sprite s : allSprites)
                s.opacity += 0.02f;
        }else{
            if(data[0] == null){
                new0.tick();
            }else{
                play0.tick();
                trash0.tick();
            }
            if(data[1] == null){
                new1.tick();
            }else{
                play1.tick();
                trash1.tick();
            }
            if(data[2] == null){
                new2.tick();
            }else{
                play2.tick();
                trash2.tick();
            }
        }
    }

    public void render(){
        game.display.drawSprite(game.mainMenu.skyLayer, 0, 0);
        game.display.drawSprite(game.mainMenu.castleLayer, 6, 18);
        game.display.drawSprite(game.mainMenu.titleText, 0, -1);

        game.display.drawSprite(background, 1, 56);
        game.display.drawSprite(background, 1, 112);
        game.display.drawSprite(background, 1, 168);

        if(data[0] == null){
            new0.render(game.display);
        }else{
            game.display.drawSprite(name0, 4, 60);
            play0.render(game.display);
            trash0.render(game.display);

            int health = data[0].health;
            for(int h = 0; h < data[0].maxHealth; h++){
                if(h % 2 == 0){
                    if(h < health){
                        game.display.drawSprite(heartLeftFull, 4 + (20 * (h/2)), 72);
                    }else{
                        game.display.drawSprite(heartLeftEmpty, 4 + (20 * (h/2)), 72);
                    }
                }else{
                    if(h < health){
                        game.display.drawSprite(heartRightFull, 13 + (20 * (h/2)), 72);
                    }else{
                        game.display.drawSprite(heartRightEmpty, 13 + (20 * (h/2)), 72);
                    }
                }
            }

            if(data[0].master)
                game.display.drawSprite(bossLight, 6, 92);
            else
                game.display.drawSprite(bossDark, 6, 92);
            if(data[0].chef)
                game.display.drawSprite(bossLight, 26, 92);
            else
                game.display.drawSprite(bossDark, 26, 92);
            if(data[0].king)
                game.display.drawSprite(bossLight, 46, 92);
            else
                game.display.drawSprite(bossDark, 46, 92);
        }
        if(data[1] == null){
            new1.render(game.display);
        }else{
            game.display.drawSprite(name1, 4, 116);
            play1.render(game.display);
            trash1.render(game.display);

            int health = data[1].health;
            for(int h = 0; h < data[1].maxHealth; h++){
                if(h % 2 == 0){
                    if(h < health){
                        game.display.drawSprite(heartLeftFull, 4 + (20 * (h/2)), 127);
                    }else{
                        game.display.drawSprite(heartLeftEmpty, 4 + (20 * (h/2)), 127);
                    }
                }else{
                    if(h < health){
                        game.display.drawSprite(heartRightFull, 13 + (20 * (h/2)), 127);
                    }else{
                        game.display.drawSprite(heartRightEmpty, 13 + (20 * (h/2)), 127);
                    }
                }
            }

            if(data[1].master)
                game.display.drawSprite(bossLight, 6, 148);
            else
                game.display.drawSprite(bossDark, 6, 148);
            if(data[1].chef)
                game.display.drawSprite(bossLight, 26, 148);
            else
                game.display.drawSprite(bossDark, 26, 148);
            if(data[1].king)
                game.display.drawSprite(bossLight, 46, 148);
            else
                game.display.drawSprite(bossDark, 46, 148);
        }
        if(data[2] == null){
            new2.render(game.display);
        }else{
            game.display.drawSprite(name2, 4, 172);
            play2.render(game.display);
            trash2.render(game.display);

            int health = data[2].health;
            for(int h = 0; h < data[2].maxHealth; h++){
                if(h % 2 == 0){
                    if(h < health){
                        game.display.drawSprite(heartLeftFull, 4 + (20 * (h/2)), 182);
                    }else{
                        game.display.drawSprite(heartLeftEmpty, 4 + (20 * (h/2)), 182);
                    }
                }else{
                    if(h < health){
                        game.display.drawSprite(heartRightFull, 13 + (20 * (h/2)), 182);
                    }else{
                        game.display.drawSprite(heartRightEmpty, 13 + (20 * (h/2)), 182);
                    }
                }
            }

            if(data[2].master)
                game.display.drawSprite(bossLight, 6, 204);
            else
                game.display.drawSprite(bossDark, 6, 204);
            if(data[2].chef)
                game.display.drawSprite(bossLight, 26, 204);
            else
                game.display.drawSprite(bossDark, 26, 204);
            if(data[2].king)
                game.display.drawSprite(bossLight, 46, 204);
            else
                game.display.drawSprite(bossDark, 46, 204);
        }
    }
}