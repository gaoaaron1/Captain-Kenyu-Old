package com.aaron.game.screens;

import com.aaron.game.PlatformGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainStageScreen implements Screen {

    //Background texture
    private Texture background;

    private static final int STAGE1_BUTTON_WIDTH = 100;
    private static final int STAGE1_BUTTON_HEIGHT = 100;

    private static final int STAGE2_BUTTON_WIDTH = 100;
    private static final int STAGE2_BUTTON_HEIGHT = 100;

    private static final int BACK_BUTTON_WIDTH = 200;
    private static final int BACK_BUTTON_HEIGHT = 100;

    private static final int STAGE1_BUTTON_X = 275;
    private static final int STAGE1_BUTTON_Y = 325;

    private static final int STAGE2_BUTTON_X = 400;
    private static final int STAGE2_BUTTON_Y = 325;

    private static final int BACK_BUTTON_Y = 100;

    final PlatformGame game;

    //Texture time!
    Texture Stage1Button;
    Texture Stage1ButtonClicked;
    Texture Stage2Button;
    Texture Stage2ButtonClicked;
    Texture BackButton;
    Texture BackButtonClicked;

    //constructor
    public MainStageScreen(final PlatformGame game) {

        this.game = game;

        //Background texture
        background = new Texture("background.png");

        //Access texture images
        Stage1Button = new Texture("Stage1Button.png");
        Stage1ButtonClicked = new Texture("Stage1ButtonClicked.png");
        Stage2Button = new Texture("Stage2Button.png");
        Stage2ButtonClicked = new Texture("Stage2ButtonClicked.png");
        BackButton = new Texture("BackButton.png");
        BackButtonClicked = new Texture("BackButtonClicked.png");


        final MainStageScreen mainStageScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                //Back Button
                int x = PlatformGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x + BACK_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > BACK_BUTTON_Y) {
                    mainStageScreen.dispose();
                    game.setScreen(new MainMenuScreen(game));
                }

                //Stage1 Button
                x = PlatformGame.WIDTH / 2 - STAGE1_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < STAGE1_BUTTON_X + STAGE1_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > STAGE1_BUTTON_X && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < STAGE1_BUTTON_Y + STAGE1_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > STAGE1_BUTTON_Y) {
                    //Go to game screen.
                    mainStageScreen.dispose(); //clean up current screen
                    PlayScreen.myStage = 1;
                    game.setScreen(new PlayScreen(game));
                }

                //Stage2 Button
                x = PlatformGame.WIDTH / 2 - STAGE2_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < STAGE2_BUTTON_X + STAGE2_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > STAGE2_BUTTON_X && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < STAGE2_BUTTON_Y + STAGE2_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > STAGE2_BUTTON_Y) {
                    //Go to game screen.
                    mainStageScreen.dispose(); //clean up current screen
                    PlayScreen.myStage = 2;
                    game.setScreen(new PlayScreen(game));
                }




                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, PlatformGame.WIDTH, PlatformGame.HEIGHT);

        int x = PlatformGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2;

        //Stage 1 Button
        if (game.cam.getInputInGameWorld().x < STAGE1_BUTTON_X + STAGE1_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > STAGE1_BUTTON_X && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < STAGE1_BUTTON_Y + STAGE1_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > STAGE1_BUTTON_Y) {
            game.batch.draw(Stage1ButtonClicked,  STAGE1_BUTTON_X, STAGE1_BUTTON_Y, STAGE1_BUTTON_WIDTH, STAGE1_BUTTON_HEIGHT);
        } else {
            game.batch.draw(Stage1Button, STAGE1_BUTTON_X, STAGE1_BUTTON_Y, STAGE1_BUTTON_WIDTH, STAGE1_BUTTON_HEIGHT);
        }

        //Stage 2 Button
        if (game.cam.getInputInGameWorld().x < STAGE2_BUTTON_X + STAGE2_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > STAGE2_BUTTON_X && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < STAGE2_BUTTON_Y + STAGE2_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > STAGE2_BUTTON_Y) {
            game.batch.draw(Stage2ButtonClicked, STAGE2_BUTTON_X, STAGE2_BUTTON_Y, STAGE2_BUTTON_WIDTH, STAGE2_BUTTON_HEIGHT);
        } else {
            game.batch.draw(Stage2Button, STAGE2_BUTTON_X, STAGE2_BUTTON_Y, STAGE2_BUTTON_WIDTH, STAGE2_BUTTON_HEIGHT);
        }

        //Back Button
        if (game.cam.getInputInGameWorld().x < x + BACK_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > BACK_BUTTON_Y) {
            game.batch.draw(BackButtonClicked, PlatformGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);

        } else {
            game.batch.draw(BackButton, PlatformGame.WIDTH / 2 - BACK_BUTTON_WIDTH / 2, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        }



        game.batch.end();
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
        background.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
