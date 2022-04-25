package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class WarehouseOfConstants {
    public static final Texture TEXTURE = new Texture("deathCell.png");

    public static float RED = 0;
    public static float GREEN = 0;
    public static float BLUE = 0;

    public static final int WINDOW_SCALE = 600;

    public static int WIDTH = 59;
    public static int HEIGHT = 59;

    public static final int SPACING = 2;

    public static int VIEWPORT = (int)(Math.max(WIDTH, HEIGHT) * ((float)SPACING + 0.5));

    public static float DISPLACEMENT_COEFFICIENT = (float) WINDOW_SCALE / VIEWPORT;
}
