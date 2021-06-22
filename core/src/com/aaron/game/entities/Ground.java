package com.aaron.game.entities;

import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class Ground extends InteractiveTileObject{

    public Ground(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(PlatformGame.GROUND_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
    }

    @Override
    public void onFeetTouch(Mario mario) {
        Gdx.app.log("Ground", "Collision");
    }
}
