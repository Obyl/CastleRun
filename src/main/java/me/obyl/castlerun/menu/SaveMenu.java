package me.obyl.castlerun.menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.utils.Save;

public class SaveMenu {

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
    }

    public void tick(){

    }

    public void render(){
        game.display.drawSprite(game.mainMenu.skyLayer, 0, 0);
        game.display.drawSprite(game.mainMenu.castleLayer, 6, 18);
        game.display.drawSprite(game.mainMenu.titleText, 0, -1);
    }
}