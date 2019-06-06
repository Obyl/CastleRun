package me.obyl.castlerun.graphics;

import me.obyl.castlerun.Game;
import me.obyl.castlerun.utils.Cosine;
import me.obyl.castlerun.utils.Sine;

import java.awt.Color;

public class Display {

    private static final float SQRT_2 = (float) Math.sqrt(2);

    private Game game;

    public Display(Game game){
        this.game = game;
    }

    public void drawSprite(Sprite sprite, float xPos, float yPos){
        if(sprite.opacity <= 0.0f)
            return;

        // If the sprite is rotated or sheared both horizontally and vertically, the representation
        // of the image must be blown up a little to prevent aliasing.
        float scaleModifier = 1;
        if(sprite.rotation != 0 || (sprite.hShear != 0 && sprite.vShear != 0))
            scaleModifier = SQRT_2;

        // Calculate proper bounds of sprite on screen:
        //adjust for scroll here
        float properWidth = sprite.width * sprite.hScale * scaleModifier;
        float properHeight = sprite.height * sprite.vScale * scaleModifier;
        float centerX = properWidth / 2;
        float centerY = properHeight / 2;

        // Create sprite's identity matrix:
        float a = 1;
        float b = 0;
        float c = 0;
        float d = 1;

        // Alter identity matrix to fit transformations:
        if(sprite.hReflect)
            a = -1;
        if(sprite.vReflect)
            d = -1;
        if(sprite.hShear != 0)
            b = (sprite.hShear / properWidth) * d;
        if(sprite.vShear != 0){
            float properShear = sprite.vShear / properHeight;

            c = properShear * a;
            d += properShear * b;
        }
        if(sprite.rotation != 0){
            boolean negative = false;
            int properRot = (int) sprite.rotation;
            if(properRot < 0){
                negative = true;
                properRot *= -1;
            }
            properRot = properRot % 360;
            if(negative)
                properRot = 360 - properRot;

            float oldA = a;
            float oldB = b;
            float oldC = c;
            float oldD = d;
            float sin = Sine.get(properRot);
            float cos = Cosine.get(properRot);

            a = cos * oldA - sin * oldC;
            b = cos * oldB - sin * oldD;
            c = sin * oldA + cos * oldC;
            d = sin * oldB + cos * oldD;
        }

        float[] hsbBuffer = new float[3];

        float mixWeight = sprite.opacity;
        float oldWeight = 1.0f- sprite.opacity;

        // Loop through the area where the sprite is to be drawn
        // and draw it:
        for(int y = 0; y < properHeight; y++){
            for(int x = 0; x < properWidth; x++){
                // Set the center of the transformation to the center of the image.
                float startX = x - centerX;
                float startY = y - centerY;

                // ((transformed pixel + center) / modifier) + position
                int screenX = (int)(((startX * a + startY * b + centerX) / scaleModifier) + xPos);
                int screenY = (int)(((startX * c + startY * d + centerY) / scaleModifier) + yPos);

                // Only draw the pixel if it's on the screen (ArrayOutOfBoundsException)
                if(screenX < 0 || screenY < 0 || screenX >= Game.WIDTH || screenY >= Game.HEIGHT)
                    continue;

                // Get color from sprite's pixels.
                int color = sprite.pixels[
                            (int) (x / (sprite.hScale * scaleModifier)) +
                            (int) (y / (sprite.vScale * scaleModifier)) *
                            sprite.width
                            ];

                if(color == 0xff00ff)
                    continue;

                // Apply HSV modifications:
                if(sprite.hueMod != 0 || sprite.saturationMod != 0 || sprite.brightnessMod != 0){
                    Color.RGBtoHSB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff, hsbBuffer);

                    hsbBuffer[0] += sprite.hueMod;
                    if(hsbBuffer[0] < 0.0f)
                        hsbBuffer[0] = 0.0f;
                    else if(hsbBuffer[0] > 1.0f)
                        hsbBuffer[0] = 1.0f;

                    hsbBuffer[1] += sprite.saturationMod;
                    if(hsbBuffer[1] < 0.0f)
                        hsbBuffer[1] = 0.0f;
                    else if(hsbBuffer[1] > 1.0f)
                        hsbBuffer[1] = 1.0f;

                    hsbBuffer[2] += sprite.brightnessMod;
                    if(hsbBuffer[2] < 0.0f)
                        hsbBuffer[2] = 0.0f;
                    else if(hsbBuffer[2] > 1.0f)
                        hsbBuffer[2] = 1.0f;

                    color = Color.HSBtoRGB(hsbBuffer[0], hsbBuffer[1], hsbBuffer[2]);
                }

                // Apply opacity modification:
                if(sprite.opacity < 1){
                    int oldColor = game.pixels[screenX + screenY * Game.WIDTH];

                    int newRed = (int) (((color & 0xff0000) >> 16) * mixWeight + ((oldColor & 0xff0000) >> 16) * oldWeight);
                    int newGreen = (int) (((color & 0xff00) >> 8) * mixWeight + ((oldColor & 0xff00) >> 8) * oldWeight);
                    int newBlue = (int) ((color & 0xff) * mixWeight + (oldColor & 0xff) * oldWeight);

                    if(newRed < 0)
                        newRed = 0;
                    else if(newRed > 255)
                        newRed = 255;
                    if(newGreen < 0)
                        newGreen = 0;
                    else if(newGreen > 255)
                        newGreen = 255;
                    if(newBlue < 0)
                        newBlue = 0;
                    else if(newBlue > 255)
                        newBlue = 255;

                    color = (newRed << 16) | (newGreen << 8) | newBlue;
                }

                game.pixels[screenX + screenY * Game.WIDTH] = color;
            }
        }
    }
}