package com.aaron.game;

import com.aaron.game.screens.MainMenuScreen;
import com.aaron.game.tools.GameCamera;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlatformGame extends Game {

    //Set a fixed size for launchers
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float PPM = 100; //pixels per meter

    public SpriteBatch batch;

    //Asset Manager for music
    public static AssetManager manager;

    //Game camera
    public GameCamera cam;

    //Collision Filters
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_FEET_BIT = 512;

    @Override
    public void create () {
        batch = new SpriteBatch();
        cam = new GameCamera(WIDTH, HEIGHT);

        //music file
		manager = new AssetManager();
        manager.load("audio/music/sandman.mp3", Music.class);
		manager.load("audio/music/mariotheme.mp3", Music.class);
        manager.load("audio/music/wonderfulchristmastime.mp3", Music.class);
        manager.load("audio/sounds/jump.wav", Sound.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/bump.wav", Sound.class);
		manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.load("audio/sounds/collect.wav", Sound.class);
        manager.load("audio/sounds/questcomplete.mp3", Sound.class);
        manager.load("audio/sounds/screech.wav", Sound.class);
        manager.load("audio/sounds/cry.wav", Sound.class);
		manager.finishLoading();


        this.setScreen(new MainMenuScreen(this ));
        //represents show() on main game screen
    }


    @Override
    public void render () {
        //render camera viewport
        batch.setProjectionMatrix(cam.combined());

        //render
        super.render();
        //System.out.println("" + Gdx.graphics.getDeltaTime());
        //this render goes to new render of main game screen.
    }

    @Override
    public void resize(int width, int height) {

        //Size for android
        cam.update(width, height);

        super.resize(width, height);
    }



    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }
}
