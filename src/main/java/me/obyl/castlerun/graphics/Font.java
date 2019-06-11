package me.obyl.castlerun.graphics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Font {

    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "1234567890!@#$%^&*()[]{}\\|" +
            ",.<>/?;:'\"-=_+`~";
    private static final int[] CHAR_WIDTHS = new int[CHARS.length()];
    private static final int[] CHAR_HEIGHTS = new int[CHARS.length()];
    private static final int[] CHAR_X_OFFSETS = new int[CHARS.length()];
    private static final int[] CHAR_Y_OFFSETS = new int[CHARS.length()];
    private static final int MAX_CHAR_WIDTH = 5;
    private static final int MAX_CHAR_HEIGHT = 9;

    private static Sprite source;

    public static void load(Gson gson){
        source = new Sprite("/textures/font.png");

        Type parseType = new TypeToken<HashMap<String, int[]>>(){}.getType();
        HashMap<String, int[]> charData = gson.fromJson(
            new BufferedReader(new InputStreamReader(Font.class.getResourceAsStream("/data/chars.json"))),
            parseType);

        for(int c = 0; c < CHARS.length(); c++){
            String character = CHARS.substring(c, c + 1);
            int[] data = charData.get(character);
            if(data == null){
                if(c / 26 == 1)
                    data = charData.get("default_lower");
                else
                    data = charData.get("default_upper");
            }

            CHAR_WIDTHS[c] = data[0];
            CHAR_HEIGHTS[c] = data[1];
            CHAR_X_OFFSETS[c] = data[2];
            CHAR_Y_OFFSETS[c] = data[3];
        }
    }

    public static Sprite createSprite(String text, float boundsWidth, float boundsHeight, int color){
        if(boundsWidth < MAX_CHAR_WIDTH || boundsHeight < MAX_CHAR_HEIGHT)
            return null;

        Sprite sprite = new Sprite((int) boundsWidth, (int) boundsHeight, 0xff00ff);

        int trimmedWidth = 0;
        int trimmedHeight = 0;

        int xAnchor = 0;
        int yAnchor = 0;
        StringTokenizer tokenizer = new StringTokenizer(text, " ");
        int index = 0;
        while(tokenizer.hasMoreTokens()){
            String word = tokenizer.nextToken();

            if(xAnchor + (word.length() * MAX_CHAR_WIDTH) >= boundsWidth){
                yAnchor += MAX_CHAR_HEIGHT * 1.5;
                xAnchor = 0;
            }

            while(text.substring(index, index + 1).equals(" ")){
                xAnchor += MAX_CHAR_WIDTH;
                index++;
            }
            index += word.length();

            for(char c : word.toCharArray()){
                int character = CHARS.indexOf(c);

                int xStart = xAnchor + CHAR_X_OFFSETS[character];
                int yStart = yAnchor + CHAR_Y_OFFSETS[character];
                if(xStart < 0){
                    xAnchor -= xStart;
                    xStart = 0;
                }
                if(yStart < 0){
                    yAnchor -= yStart;
                    yStart = 0;
                }

                for(int y = 0; y < CHAR_HEIGHTS[character]; y++){
                    for(int x = 0; x < CHAR_WIDTHS[character]; x++){
                        int imageX = xStart + x;
                        int imageY = yStart + y;

                        if(imageX > trimmedWidth)
                            trimmedWidth = imageX;
                        if(imageY > trimmedHeight)
                            trimmedHeight = imageY;

                        int xSheet = x + (MAX_CHAR_WIDTH * (character % 26));
                        int ySheet = y + (MAX_CHAR_HEIGHT * (character / 26));

                        if(source.pixels[xSheet + ySheet * source.width] != 0xff00ff)
                            sprite.pixels[imageX + imageY * (int)boundsWidth] = color;
                    }
                }

                xAnchor += CHAR_WIDTHS[character] + CHAR_X_OFFSETS[character] + 1;
            }
        }

        trimmedWidth += 1;
        trimmedHeight += 1;

        if(trimmedWidth == boundsWidth && trimmedHeight == boundsHeight)
            return sprite;

        int[] trimmedPixels = new int[trimmedWidth * trimmedHeight];
        for(int y = 0; y < trimmedHeight; y++)
            for(int x = 0; x < trimmedWidth; x++)
                trimmedPixels[x + y * trimmedWidth] = sprite.pixels[x + y * (int)boundsWidth];

        sprite.pixels = trimmedPixels;
        sprite.width = trimmedWidth;
        sprite.height = trimmedHeight;
        return sprite;
    }
}