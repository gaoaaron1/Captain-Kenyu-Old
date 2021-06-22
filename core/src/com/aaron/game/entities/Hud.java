package com.aaron.game.entities;

import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label fragmentLabel;
    private Label exitGameLabel;

    public String levelString;
    public static String numberOfFragments;

    public Hud(SpriteBatch sb) {
        worldTimer = 0;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(PlatformGame.WIDTH, PlatformGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb); //Stage is an empty box

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        switch (PlayScreen.myStage) {
            case 1:
                levelString = "1";
                numberOfFragments = "/3";
                break;
            case 2:
                levelString = "2";
                numberOfFragments = "/2";
                break;
        }
        BitmapFont textFont;
        textFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(textFont, Color.WHITE)); //Takes in numbers in countdown timer.
        scoreLabel = new Label(String.format(score + numberOfFragments), new Label.LabelStyle(textFont, Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(textFont, Color.GREEN));
        levelLabel = new Label(levelString, new Label.LabelStyle(textFont, Color.WHITE));
        worldLabel = new Label("STAGE", new Label.LabelStyle(textFont, Color.GREEN));
        fragmentLabel = new Label("FRAGMENTS", new Label.LabelStyle(textFont, Color.GREEN));
        //fragmentLabel = new Label("Exit Game", new Label.LabelStyle(textFont, Color.GREEN));

        //top rows
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(fragmentLabel).expandX().padTop(10);     //Add the labels to our table.

        //bottom rows
        table.row();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(scoreLabel).expandX();

        //Add table to stage
        stage.addActor(table);
    }

    //Timer update
    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            worldTimer++;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }

    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format(score + numberOfFragments));

        switch (PlayScreen.myStage) {
            case 1:
                if (score == 3)
                    PlatformGame.manager.get("audio/sounds/questcomplete.mp3", Sound.class).play(0.1f);
                    break;
            case 2:
                if (score == 2)
                    PlatformGame.manager.get("audio/sounds/questcomplete.mp3", Sound.class).play(0.1f);
                    break;
        }
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
