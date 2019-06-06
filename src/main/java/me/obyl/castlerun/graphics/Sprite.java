package me.obyl.castlerun.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite{

    public int width, height;
    public int[] pixels;

    public float hScale = 1.0f;
    public float vScale = 1.0f;
    public float hShear = 0.0f;
    public float vShear = 0.0f;
    public float rotation = 0.0f;
    public boolean hReflect = false;
    public boolean vReflect = false;
    public float hueMod = 0.0f;
    public float saturationMod = 0.0f;
    public float brightnessMod = 0.0f;
    public float opacity = 1.0f;

    public Sprite(int width, int height, int color){
        this.width = width;
        this.height = height;
        
        pixels = new int[width * height];
        for(int i = 0; i < pixels.length; i++)
            pixels[i] = color;
    }

    public Sprite(String source){
        BufferedImage image;
        try{
            image = ImageIO.read(Sprite.class.getResourceAsStream(source));
            
            width = image.getWidth();
            height = image.getHeight();
            pixels = image.getRGB(0, 0, width, height, null, 0, width);

            for(int i = 0; i < pixels.length; i++)
                pixels[i] = pixels[i] & 0xffffff;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Sprite(Sprite source, int width, int height, int sourceX, int sourceY){
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        int x0 = (width * sourceX);
        int y0 = (height * sourceY);
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
                pixels[x + y * width] = source.pixels[(x0 + x) + (y0 + y) * source.width];
    }
}