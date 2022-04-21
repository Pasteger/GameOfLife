package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;

public class MainGameSpace implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final ArrayList<Cell> cells = new ArrayList<>();

    private int countRenders;
    private int fps;
    private int lPressed;

    private boolean q;
    private boolean mouseDown;

    private long startCurrentTime;

    private final int HEIGHT = 80;
    private final int WIDTH = 80;

    public MainGameSpace(final MyGdxGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 150, 150);

        for (int y = 0; y < HEIGHT; y++){
            for (int x = 0; x < WIDTH; x++){
                spawnCell(x, y);
            }
        }

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override public boolean keyDown(int i) { return false; }
            @Override public boolean keyUp(int i) { return false; }
            @Override public boolean keyTyped(char c) { return false; }
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                for (Cell cell : cells){
                    if(cell.x == (x / 4) && cell.y == ((600 - y) / 4)){
                        cell.alive = !cell.alive;
                        cell.thisCycle = true;
                        break;
                    }
                }
                mouseDown = true;
                return false;
            }
            @Override public boolean touchUp(int i, int i1, int i2, int i3) {
                for (Cell cell : cells) cell.thisCycle = false;
                mouseDown = false; return false; }
            @Override public boolean touchDragged(int x, int y, int i2) {
                if (mouseDown){
                    for (Cell cell : cells){
                        if(cell.x == (x / 4) && cell.y == ((600 - y) / 4) && !cell.thisCycle){
                            cell.alive = !cell.alive;
                            cell.thisCycle = true;
                            return true;
                        }
                    }
                }
                return false; }
            @Override public boolean mouseMoved(int x, int y) { return false; }
            @Override public boolean scrolled(float v, float v1) { return false; }
        });

        startCurrentTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 5, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        for (Cell cell : cells) {
            cell.live();
            game.batch.draw(cell.cellTexture, cell.x, cell.y);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            q = !q;
        }
        if (q) live();

        if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
            clear();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
            generateHangar();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
            if (lPressed == 0){
                clear();
                generateHorizontalLine();
                lPressed++;
            }
            else if (lPressed == 1){
                clear();
                generateVerticalLine();
                lPressed++;
            }
            else if (lPressed == 2){
                clear();
                generateHorizontalLine();
                generateVerticalLine();
                lPressed++;
            }
            else {
                clear();
                lPressed = 0;
            }
        }

        game.font.draw(game.batch, "" + fps, 40, 110);
        game.batch.end();
        countRenders++;

        long finishCurrentTime = System.currentTimeMillis();
        if(finishCurrentTime - startCurrentTime >= 1000){
            startCurrentTime = finishCurrentTime;
            fps = countRenders;
            countRenders = 0;
        }
    }

    private void spawnCell(int x, int y){
        Cell cell = new Cell();
        cell.x = x;
        cell.y = y;
        cells.add(cell);
    }

    private void live(){
        for (Cell cell : cells){
            for (Cell cell1 : cells) {
                if (cell1.alive) {
                    if (
                            (cell.x - 1 == cell1.x && cell.y == cell1.y) ||
                                    (cell.x - 1 == cell1.x && cell.y + 1 == cell1.y) ||
                                    (cell.x == cell1.x && cell.y + 1 == cell1.y) ||
                                    (cell.x + 1 == cell1.x && cell.y + 1 == cell1.y) ||
                                    (cell.x + 1 == cell1.x && cell.y == cell1.y) ||
                                    (cell.x + 1 == cell1.x && cell.y - 1 == cell1.y) ||
                                    (cell.x == cell1.x && cell.y - 1 == cell1.y) ||
                                    (cell.x - 1 == cell1.x && cell.y - 1 == cell1.y)
                    ) {
                        cell.neighbors++;
                    }
                }
            }
        }
        for (Cell cell : cells){
            if(!cell.alive & cell.neighbors == 3){
                cell.alive=true;
            }
            else if(cell.alive & cell.neighbors < 2){
                cell.alive=false;
            }
            else if(cell.alive & cell.neighbors > 3){
                cell.alive=false;
            }
            cell.neighbors=0;
        }
    }

    private void clear(){
        for (Cell cell : cells){
            cell.alive = false;
        }
    }

    private void generateHangar() {
        clear();

        boolean threeForHeight;
        boolean threeForWidth;

        try {threeForHeight = String.valueOf((float) HEIGHT / 3).split("\\.")[1].contains("3");}
        catch (Exception exception){threeForHeight = false;}

        try {threeForWidth = String.valueOf((float) WIDTH / 3).split("\\.")[1].contains("3");}
        catch (Exception exception){threeForWidth = false;}

        for (int i = 0; i < HEIGHT; i++){
            if((i + 1) % 3 != 0) {
                if (threeForHeight && i == HEIGHT - 1){
                    return;
                }
                for (int j = 0; j < WIDTH; j++) {
                    if ((j + 1) % 3 != 0) {
                        cells.get(j + WIDTH * i).alive = !threeForWidth || j != WIDTH - 1;
                    }
                }
            }
        }
    }

    private void generateHorizontalLine(){
        for (int i = 0; i < WIDTH; i++){
            cells.get(i + WIDTH * (HEIGHT / 2)).alive = true;
        }
    }
    private void generateVerticalLine(){
        for (int i = 0; i < HEIGHT; i++){
            cells.get(WIDTH * i + WIDTH / 2).alive = true;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
        game.batch.dispose();
        game.font.dispose();
        for (Cell cell : cells){
            cell.cellTexture.dispose();
        }
    }

}
