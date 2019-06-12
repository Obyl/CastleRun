package me.obyl.castlerun.graphics;

public class Animation{

    public Sprite[] sprites;
    public int current;
    public int counter;
    public int delay;

    public Animation(int delay, Sprite ... sprites){
        this.delay = delay;
        this.sprites = sprites;
    }

    public void tick(){
        if(counter < delay){
            counter++;
        }else{
            counter = 0;
            current++;
            if(current >= sprites.length)
                current = 0;
        }
    }

    public void render(Display display, float xPos, float yPos){
        display.drawSprite(sprites[current], xPos, yPos);
    }

}