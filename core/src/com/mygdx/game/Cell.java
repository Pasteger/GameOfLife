package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.mygdx.game.TexturesCell.*;

public class Cell {
    int x;
    int y;
    public boolean alive;
    boolean thisCycle;
    float[] color = new float[] {R, G, B};
    public Sprite sprite;
    int neighbors;

    Cell(){
        sprite = new Sprite (TexturesCell.getDeathTexture(), x, y, 2, 2);
        neighbors = 0;
    }

    public void live(){
        if(alive){
            sprite.setColor(color[0],color[1],color[2],1);
        }
        else {
            sprite.setColor(1, 1 , 1,1);
        }
        color[0] = R;
        color[1] = G;
        color[2] = B;
    }
}
