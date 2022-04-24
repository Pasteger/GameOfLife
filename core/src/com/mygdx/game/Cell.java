package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.mygdx.game.WarehouseOfConstants.*;

public class Cell {
    public int x;
    public int y;
    public boolean alive;
    public boolean thisCycle;
    public float[] color = new float[] {RED, GREEN, BLUE};
    public Sprite sprite;
    public int neighbors;

    Cell(){
        sprite = new Sprite (TEXTURE, x, y, SPACING, SPACING);
        neighbors = 0;
    }

    public void live(){
        if(alive){
            sprite.setColor(color[0],color[1],color[2],1);
        }
        else {
            sprite.setColor(1, 1 , 1,1);
        }
        color[0] = RED;
        color[1] = GREEN;
        color[2] = BLUE;
    }
}
