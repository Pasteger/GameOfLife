package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Cell {
    int x;
    int y;
    boolean alive;
    boolean thisCycle;
    public Texture cellTexture;
    int neighbors;

    Cell(){
        cellTexture = TexturesCell.getDeathTexture();
        neighbors = 0;
    }

    public void live(){
        if(alive){ cellTexture = TexturesCell.getAliveTexture(); }
        else { cellTexture = TexturesCell.getDeathTexture(); }
    }
}
