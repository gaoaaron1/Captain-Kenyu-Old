package com.aaron.game.screens;

import com.aaron.game.PlatformGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen implements Screen {

    //Background texture
    private Texture background;

    private static final int START_BUTTON_WIDTH = 200;
    private static final int START_BUTTON_HEIGHT = 100;
    private static final int EXIT_BUTTON_WIDTH = 200;
    private static final int EXIT_BUTTON_HEIGHT = 100;
    private static final int START_BUTTON_Y = 250;
    private static final int EXIT_BUTTON_Y = 100;

    final PlatformGame game;

    //Texture time!
    Texture StartButton;
    Texture StartButtonClicked;
    Texture ExitButton;
    Texture ExitButtonClicked;

    //Music variable
    //Asset Manager for music
    public AssetManager manager;
    private Music music;

    //constructor
    public MainMenuScreen(final PlatformGame game) {

        this.game = game;

        //Background texture
        background = new Texture("background.png");

        //Access texture images
        StartButton = new Texture("StartButton.png");
        StartButtonClicked = new Texture("StartButtonClicked.png");
        ExitButton = new Texture("ExitButton.png");
        ExitButtonClicked = new Texture("ExitButtonClicked.png");

        final MainMenuScreen mainMenuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                //Exit Button
                int x = PlatformGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    music.dispose();
                    Gdx.app.exit();
                }

                //Play Button
                x = PlatformGame.WIDTH / 2 - START_BUTTON_WIDTH / 2;
                if (game.cam.getInputInGameWorld().x < x + START_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < START_BUTTON_Y + START_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > START_BUTTON_Y) {
                    //Go to game screen.
                    mainMenuScreen.dispose(); //clean up current screen
                    game.setScreen(new MainStageScreen(game));
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });

        setMusic();
    }

    public void setMusic() {
        //music file
        manager = new AssetManager();
        manager.load("audio/music/mariotheme.mp3", Music.class);
        manager.finishLoading();
        //Allows for music to be played constructor
        music = manager.get("audio/music/mariotheme.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.1f); //10% volume
        music.play();
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

        int x = PlatformGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;

        //Start Button
        if (game.cam.getInputInGameWorld().x < x + START_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < START_BUTTON_Y + START_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > START_BUTTON_Y) {
            game.batch.draw(StartButtonClicked, PlatformGame.WIDTH / 2 - START_BUTTON_WIDTH / 2, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        } else {
            game.batch.draw(StartButton, PlatformGame.WIDTH / 2 - START_BUTTON_WIDTH / 2, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        }

        //Exit Button
        if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
            game.batch.draw(ExitButtonClicked, PlatformGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

        } else {
            game.batch.draw(ExitButton, PlatformGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
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
        music.dispose();
    }
}
