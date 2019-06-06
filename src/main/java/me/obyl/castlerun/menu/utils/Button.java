package me.obyl.castlerun.menu.utils;

import me.obyl.castlerun.graphics.Display;
import me.obyl.castlerun.graphics.Font;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.utils.Mouse;

public class Button {

    private static final float BUTTON_WIDTH = 80;
    private static final float BUTTON_HEIGHT = 20;
    private static final int TEXT_COLOR = 0xCBD8F2;

    public float x, y;
    public ButtonAction action;
    public Sprite sprite;

    private boolean canClick = true;
    private Sprite normalSprite;
    private Sprite hoverSprite;
    private Sprite clickSprite;

    public Button(float x, float y, String text){
        this.x = x;
        this.y = y;

        normalSprite = new Sprite("/textures/menu/button_normal.png");
        hoverSprite = new Sprite("/textures/menu/button_hover.png");
        clickSprite = new Sprite("/textures/menu/button_click.png");

        Sprite textSprite = Font.createSprite(text, BUTTON_WIDTH, BUTTON_HEIGHT, TEXT_COLOR);

        float startX = 40 - (textSprite.width / 2);
        float startY = 10 - (textSprite.height / 2);

        if(textSprite.height < 12)
            startY--;

        for(int ys = 0; ys < textSprite.height; ys++){
            for(int xs = 0; xs < textSprite.width; xs++){
                int addColor = textSprite.pixels[xs + ys * textSprite.width];
                if(addColor == 0xff00ff)
                    continue;

                int addLoc = (int) ((startX + xs) + (startY + ys) * BUTTON_WIDTH);
                normalSprite.pixels[addLoc] = addColor;
                hoverSprite.pixels[addLoc] = addColor;
                clickSprite.pixels[addLoc] = addColor;
            }
        }

        sprite = normalSprite;
    }

    public void tick(){
        if(Mouse.x >= x && Mouse.y >= y && Mouse.x < x + BUTTON_WIDTH && Mouse.y < y + BUTTON_HEIGHT){
            sprite = hoverSprite;
            if(Mouse.isButtonPressed(Mouse.LEFT)){
                sprite = clickSprite;
                if(canClick && action != null){
                    action.onClick();
                    canClick = false;
                }
            }else{
                canClick = true;
            }
        }else{
            sprite = normalSprite;
        }
    }

    public void render(Display display){
        display.drawSprite(sprite, x, y);
    }

}