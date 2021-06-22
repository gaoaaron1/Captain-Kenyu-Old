package com.aaron.game.entities;

import com.aaron.game.Items.ItemDef;
import com.aaron.game.Items.Mushroom;
import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Brick extends InteractiveTileObject{
    public Brick (PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformGame.BRICK_BIT);
    }
    @Override
    public void onFeetTouch(Mario mario) {
        Gdx.app.log("Brick", "Collision on feet");

        switch (PlayScreen.myStage) {
            case 1:
                getCell().setTile(null);
                getCell2().setTile(null);
                getCell3().setTile(null);
                getCell4().setTile(null);
                break;
            case 2:
                getCell().setTile(null);
                getCell2().setTile(null);
        }
        setCategoryFilter(PlatformGame.DESTROYED_BIT);
        PlatformGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / PlatformGame.PPM),
                Mushroom.class));
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Brick", "Collision through head");
    }
}
