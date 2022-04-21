package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class TexturesCell {
    private static final Texture aliveTexture = new Texture("aliveCell.png");
    private static final Texture deathTexture = new Texture("deathCell.png");;

    public static Texture getAliveTexture() {
        return aliveTexture;
    }

    public static Texture getDeathTexture() {
        return deathTexture;
    }
}
