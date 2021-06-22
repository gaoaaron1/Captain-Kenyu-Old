package com.aaron.game.screens;

import com.aaron.game.Items.Item;
import com.aaron.game.Items.ItemDef;
import com.aaron.game.Items.Mushroom;
import com.aaron.game.entities.Enemies.Enemy;
import com.aaron.game.tools.WorldContactListener;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.aaron.game.PlatformGame;
import com.aaron.game.entities.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.aaron.game.entities.Mario;
import com.aaron.game.tools.B2WorldCreator;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen extends InputAdapter implements Screen {

    //Button features
    private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton buttonJump;
    private TextButton buttonExit;
    private BitmapFont white, black;

    private static PlatformGame game;
    //Reference to our Game, used to set screens.
    //private PlatformGame game;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Sprite for characters
    private TextureAtlas atlas;


    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator; //used for goombas

    //sprites
    private Mario player;
    //private BackButton back;

    //Music variable
    //Asset Manager for music
    public AssetManager manager;
    private Music music;

    //Items
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    //Button
    Texture jumpButton;
    Texture jumpButtonClicked;
    private static final int JUMP_BUTTON_WIDTH = 70;
    private static final int JUMP_BUTTON_HEIGHT = 70;

    //Stage
    public static int myStage;


    public PlayScreen(final PlatformGame game){
        this.game = game;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewPort to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(PlatformGame.WIDTH * 0.50f / PlatformGame.PPM, PlatformGame.HEIGHT * 0.50f / PlatformGame.PPM, gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();

        //music file
        manager = new AssetManager();
        manager.load("audio/music/sandman.mp3", Music.class);
        manager.load("audio/music/wonderfulchristmastime.mp3", Music.class);
        manager.finishLoading();


        switch (myStage) {
            case 1:
                map = maploader.load("stage1.tmx");
                music = manager.get("audio/music/sandman.mp3", Music.class);
                music.setLooping(true);
                music.setVolume(0.7f); //10% volume
                music.play();
                break;
            case 2:
                map = maploader.load("stagetest.tmx");
                music = manager.get("audio/music/wonderfulchristmastime.mp3", Music.class);
                music.setLooping(true);
                music.setVolume(0.6f); //10% volume
                music.play();
                break;
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / PlatformGame.PPM);

        //Set up our sprite constructor
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        //initially set our gamecam to be centered correctly at the start of map.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        //Map tool generator
        creator = new B2WorldCreator(this);

        //create mario in our world.
        player = new Mario(this);
        //back = new BackButton(world, this);

        //Allows collision with the tile map world.
        world.setContactListener(new WorldContactListener());

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();



        //Access texture images
        jumpButton = new Texture("jumpButton.png");
        jumpButtonClicked = new Texture("jumpButtonClicked.png");

        final PlayScreen playScreen = this;

        /*
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // Back Button
                int x = PlatformGame.WIDTH / 2 - 70 / 2;
                if (game.cam.getInputInGameWorld().x < x + 70 && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < back.b2body.getPosition().y + 70 && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > back.b2body.getPosition().y) {
                    playScreen.dispose();
                    game.setScreen(new MainMenuScreen(game));
                }
                return super.touchUp(screenX, screenY, pointer, button);
            }
        });*/
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }


    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
        jumpHandler();
    }

    public void handleInput(float dt) { //delta time
        PlatformGame soundObject = new PlatformGame();
        if (isUp())
            player.jump();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            soundObject.manager.get("audio/sounds/jump.wav", Sound.class).play();

        if (isRight())
            player.b2body.applyLinearImpulse(new Vector2(0.05f, 0), player.b2body.getWorldCenter(), true);
        if (isLeft())
            player.b2body.applyLinearImpulse(new Vector2(-0.05f, 0), player.b2body.getWorldCenter(), true);

    }

    private boolean isRight() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && game.cam.getInputInGameWorld().x >= PlatformGame.WIDTH / 4 && game.cam.getInputInGameWorld().x < PlatformGame.WIDTH / 2);
    }

    private boolean isLeft() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && game.cam.getInputInGameWorld().x < PlatformGame.WIDTH / 4);
    }

    private boolean isUp() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }




    public void update(float dt) {
        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation (60 times per second)
        world.step(1/60f, 6, 1);

        //Player update(dt)
        player.update(dt);

        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 5)
                enemy.b2body.setActive(true);
        }

        for (Item item : items) {
            item.update(dt);
        }

        hud.update(dt);
        //back.update(dt);

        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;


        gamecam.update(); //update camera
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);

        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);

        for (Item item : items)
            item.draw(game.batch);

        //back.draw(game.batch);

        int x = PlatformGame.WIDTH / 2 - 70 / 2;

        /*
        //Jump Button
        if (game.cam.getInputInGameWorld().x < x + 70 && game.cam.getInputInGameWorld().x > x && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y < back.b2body.getPosition().y + 70 && PlatformGame.HEIGHT - game.cam.getInputInGameWorld().y > back.b2body.getPosition().y) {
            game.batch.draw(jumpButtonClicked, PlatformGame.WIDTH / 2 - 70/ 2, back.b2body.getPosition().y, 70, 70);
        } else {
            game.batch.draw(jumpButton, PlatformGame.WIDTH / 2 - 70 / 2, back.b2body.getPosition().y, 70, 70);
        }
*/

        game.batch.draw(jumpButtonClicked, player.b2body.getPosition().x, player.b2body.getPosition().y, 100, 100);
        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        table.setClip(true);
        table.setSize(PlatformGame.WIDTH, PlatformGame.HEIGHT);
        table.setPosition(gamePort.getWorldWidth(), gamePort.getWorldHeight());
    }

    //Optimization methods
    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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


    public void jumpHandler() {
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setFillParent(true);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        white = new BitmapFont(Gdx.files.internal("fonts/score.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("fonts/score.fnt"), false);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("jump_button");
        //textButtonStyle.down = skin.getDrawable("jump_button_clicked");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = 1;
        textButtonStyle.font = black;
        textButtonStyle.fontColor = Color.YELLOW;



        buttonJump = new TextButton("JUMP", textButtonStyle);
        buttonJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                PlatformGame soundObject = new PlatformGame();
                soundObject.manager.get("audio/sounds/jump.wav", Sound.class).play();
                System.out.println("sup");
                player.jump();
            }
        });


        buttonExit = new TextButton("EXIT", textButtonStyle);
        //buttonExit.setPosition(PlatformGame.WIDTH / 2, PlatformGame.HEIGHT / 2);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                PlatformGame.manager.get("audio/sounds/jump.wav", Sound.class).play();
                System.out.println("sup");
                dispose();
                game.setScreen(new MainStageScreen(game));
            }
        });

        buttonExit.pad(50);
        buttonJump.pad(50);

        table.bottom();
        //Top row
        table.add(buttonExit).expandX().bottom().left();
        //Bottom row
        table.add(buttonJump).expandX().bottom().right();
        table.debug(); //draws lines
        stage.addActor(table);
    }


    public void endGame() {
        dispose();
        game.setScreen(new MainStageScreen(game));
    }


    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        stage.dispose();
        atlas.dispose();
        white.dispose();
        black.dispose();
        music.dispose();
    }
}
