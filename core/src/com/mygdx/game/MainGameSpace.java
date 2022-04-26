package com.mygdx.game;

import static com.mygdx.game.WarehouseOfConstants.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Random;

public class MainGameSpace implements Screen {
    private final MyGdxGame game;
    private final OrthographicCamera camera;
    private final Random random;

    private final ArrayList<Cell> cells;

    private int countRenders;
    private int fps;
    private int lPressed;
    private int pPressed;
    private int qPressed;

    private boolean pause;
    private boolean mouseDown;

    private long startFPSTime;
    private long finishFPSTime;
    private long startAgeTime;
    private long finishAgeTime;

    private int limitationFrames;

    private String gameMode;

    private float STEP_WIDTH;
    private float STEP_HEIGHT;

    private boolean inputIsSet;
    private boolean isInput;
    private boolean preWidthIsInput;
    private StringBuilder preWidthString;
    private StringBuilder preHeightString;
    private int preWidth;
    private int preHeight;
    private float fontScale;

    public MainGameSpace(final MyGdxGame game){
        this.game = game;
        camera = new OrthographicCamera();

        cells = new ArrayList<>();

        preWidthString = new StringBuilder();
        preHeightString = new StringBuilder();

        random = new Random();

        limitationFrames = 0;

        gameMode = "Conway";

        setSettings();

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override public boolean keyDown(int i) {
                if (isInput && !preWidthIsInput) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                        preWidthIsInput = true;
                    } else {
                        preWidthString.append(Input.Keys.toString(i));
                    }
                }
                else if (isInput){
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                        inputIsSet = true;
                        preWidthIsInput = false;
                    }
                    else {
                        preHeightString.append(Input.Keys.toString(i));
                    }
                }
                return false;
            }
            @Override public boolean keyUp(int i) { return false; }
            @Override public boolean keyTyped(char c) { return false; }
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                for (Cell cell : cells){
                    if(
                            cell.x == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT) ||
                            cell.x + 1 == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT) ||
                            cell.x == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y + 1 == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT) ||
                            cell.x + 1 == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y + 1 == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT)
                    ){
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
                        if(!cell.thisCycle &&
                                (cell.x == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT) ||
                                        cell.x + 1 == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT) ||
                                        cell.x == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y + 1 == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT) ||
                                        cell.x + 1 == (int)(x / DISPLACEMENT_COEFFICIENT) && cell.y + 1 == (int)((WINDOW_SCALE - y) / DISPLACEMENT_COEFFICIENT)
                                )
                        ){
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

        startFPSTime = System.currentTimeMillis();
        startAgeTime = System.currentTimeMillis();
    }

    @Override
    public void render(float delta) {
        finishFPSTime = System.currentTimeMillis();
        finishAgeTime = System.currentTimeMillis();

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        setColorOnCell();

        game.font.draw(game.batch, "fps | gameMode | limitationFrames", 0, VIEWPORT);
        game.font.draw(game.batch, fps + " | " + gameMode + " | " + (float)limitationFrames/1000, 0, VIEWPORT - fontScale * 16);
        game.font.draw(game.batch, WIDTH + " | " + HEIGHT + "        " +
                preWidthString + " | " + preHeightString, 0, VIEWPORT - fontScale * 16 * 2
        );
        game.batch.end();
        countRenders++;

        if(isInput) {
            if (!inputIsSet) return;
            try {
                preWidth = Integer.parseInt(preWidthString.toString());
                preHeight = Integer.parseInt(preHeightString.toString());
            } catch (Exception exception) {
                isInput = false;
                inputIsSet = false;
                return;
            }
            WIDTH = preWidth;
            HEIGHT = preHeight;
            setSettings();
        }

        buttons();

        if (pause && finishAgeTime - startAgeTime >= limitationFrames) {
            live(gameMode);
            startAgeTime = finishAgeTime;
        }

        if(finishFPSTime - startFPSTime >= 1000){
            startFPSTime = finishFPSTime;
            fps = countRenders;
            countRenders = 0;
        }
    }

    private void buttons(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
            isInput = true;
            preWidthString = new StringBuilder();
            preHeightString = new StringBuilder();
            preWidth = 0;
            preHeight = 0;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            pause = !pause;
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            if(limitationFrames < 999)
                limitationFrames += 50;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            if(limitationFrames > 0)
                limitationFrames -= 50;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
            clear();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
            generateHangar();
        }   

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            for (Cell cell : cells){
                cell.alive = true;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            for (Cell cell : cells){
                cell.alive = random.nextBoolean();
            }
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
            generateHorizontalLine();
            generateVerticalLine();
            generateDiagonalUpLine();
            generateDiagonalDownLine();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            if (pPressed == 0){
                clear();
                generateDiagonalUpLine();
                pPressed++;
            }
            else if (pPressed == 1){
                clear();
                generateDiagonalDownLine();
                pPressed++;
            }
            else if (pPressed == 2){
                clear();
                generateDiagonalUpLine();
                generateDiagonalDownLine();
                pPressed++;
            }
            else {
                clear();
                pPressed = 0;
            }
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            if (qPressed == 0){
                gameMode = "EndOfDeath";
                qPressed++;
            }
            else if (qPressed == 1){
                gameMode = "Caves";
                qPressed++;
            }
            else if (qPressed == 2){
                gameMode = "Fractal";
                qPressed++;
            }
            else {
                gameMode = "Conway";
                qPressed = 0;
            }
        }
    }

    private void setColorOnCell(){
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 1; i < WIDTH+1; i++) {
                Cell tempCell = cells.get((j * WIDTH + i) - 1);
                tempCell.live();

                if (i <= WIDTH / 2) tempCell.color[0] += (STEP_WIDTH * i);
                else tempCell.color[0] += 0.5f + (STEP_WIDTH * ((float)WIDTH / 2 - i));

                if (j <= HEIGHT / 2) tempCell.color[0] += (STEP_HEIGHT * j);
                else tempCell.color[0] += 0.5f + (STEP_HEIGHT * ((float)HEIGHT / 2 - j));

                tempCell.color[2] = 1 - tempCell.color[0];

                tempCell.sprite.setPosition(tempCell.x, tempCell.y);
                tempCell.sprite.draw(game.batch);
            }
        }
    }

    private void setSettings(){
        VIEWPORT = (int) (Math.max(WIDTH, HEIGHT) * ((float) SPACING + 0.5));
        DISPLACEMENT_COEFFICIENT = (float) WINDOW_SCALE / VIEWPORT;
        cells.clear();
        for (int y = 0; y < HEIGHT * SPACING; y += SPACING) {
            for (int x = 0; x < WIDTH * SPACING; x += SPACING) {
                spawnCell(x, y);
            }
        }

        camera.setToOrtho(false, VIEWPORT, VIEWPORT);
        STEP_WIDTH = ((float) 1 / (float) (WIDTH / 2)) / 2;
        STEP_HEIGHT = ((float) 1 / (float) (HEIGHT / 2)) / 2;

        fontScale = (float)Math.max(WIDTH, HEIGHT)/100;
        game.font.getData().setScale(fontScale);

        isInput = false;
        inputIsSet = false;
    }

    private void spawnCell(int x, int y){
        Cell cell = new Cell();
        cell.x = x;
        cell.y = y;
        cells.add(cell);
    }

    private void live(String gameMode){
        switch (gameMode) {
            case "Conway":
                countingNeighbors();
                for (Cell cell : cells) {
                    if (!cell.alive & cell.neighbors == 3) {
                        cell.alive = true;
                    } else if (cell.alive & cell.neighbors < 2) {
                        cell.alive = false;
                    } else if (cell.alive & cell.neighbors > 3) {
                        cell.alive = false;
                    }
                    cell.neighbors = 0;
                }
                break;
            case "EndOfDeath":
                countingNeighbors();
                for (Cell cell : cells) {
                    if (!cell.alive & cell.neighbors == 3) {
                        cell.alive = true;
                    }
                    cell.neighbors = 0;
                }
                break;
            case "Caves":
                countingNeighbors();
                for (Cell cell : cells) {
                    if (!cell.alive && cell.neighbors > 4) {
                        cell.alive = true;
                    } else if (cell.alive && cell.neighbors < 3) {
                        cell.alive = false;
                    }
                    cell.neighbors = 0;
                }
                break;
            case "Fractal":
                countingNeighbors();
                for (Cell cell : cells) {
                    if (!cell.alive && cell.neighbors == 1) {
                        cell.alive = true;
                    }
                    cell.neighbors = 0;
                }
                break;
        }
    }

    private void countingNeighbors(){
        Cell cell;
        int size = cells.size() - 1;
        int width = WIDTH - 1;

        for(int id = 0; id < size + 1; id++){
            cell = cells.get(id);
            //Поиск в сережине поля
            if((id > width) &&
                    (id < size - width - 1) &&
                    (id % (width + 1) != 0) &&
                    ((id - width) % (width + 1) != 0)) {
                if(cells.get(id + 1).alive) cell.neighbors++;
                if(cells.get(id - 1).alive) cell.neighbors++;

                if(cells.get(id + width).alive) cell.neighbors++;
                if(cells.get(id + width + 1).alive) cell.neighbors++;
                if(cells.get(id + width + 2).alive) cell.neighbors++;

                if(cells.get(id - width).alive) cell.neighbors++;
                if(cells.get(id - width - 1).alive) cell.neighbors++;
                if(cells.get(id - width - 2).alive) cell.neighbors++;
            }
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

    private void generateDiagonalUpLine(){
        int id = 0;
        int length = Math.min(WIDTH, HEIGHT);
        for(int i = 0; i < length; i++){
            cells.get(id).alive = true;
            id += length + 1;
        }
    }

    private void generateDiagonalDownLine(){
        int length = Math.min(WIDTH, HEIGHT);
        int id = length - 1;
        for(int i = 0; i < length; i++){
            cells.get(id).alive = true;
            id += length - 1;
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
        cells.clear();
    }
}
