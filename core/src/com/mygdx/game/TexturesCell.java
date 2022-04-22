package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class TexturesCell {
    public static float R = 0;
    public static float G = 0;
    public static float B = 0;

    private static final Texture deathTexture = new Texture("deathCell.png");
    public static Texture getDeathTexture() {
        return deathTexture;
    }
}
