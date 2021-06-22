package com.aaron.game.entities;

import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Coin extends InteractiveTileObject {

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformGame.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision through body");
    }

    @Override
    public void onFeetTouch(Mario mario) {
        Gdx.app.log("Coin", "Collision through feet");
    }
}
