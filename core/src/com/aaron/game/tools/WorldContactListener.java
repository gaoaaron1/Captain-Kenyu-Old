package com.aaron.game.tools;

import com.aaron.game.Items.Item;
import com.aaron.game.PlatformGame;
import com.aaron.game.entities.Enemies.Enemy;
import com.aaron.game.entities.Mario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.aaron.game.entities.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    //contact listener gets called when two fixtures in box2D collides.

    //two fixtures begin colliding
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //collision definition OR both fixtures
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case PlatformGame.MARIO_FEET_BIT | PlatformGame.BRICK_BIT:
            case PlatformGame.MARIO_FEET_BIT | PlatformGame.COIN_BIT:
                if(fixA.getFilterData().categoryBits == PlatformGame.MARIO_FEET_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onFeetTouch((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onFeetTouch((Mario) fixB.getUserData());
                break;
            case PlatformGame.ENEMY_HEAD_BIT | PlatformGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == PlatformGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else if (fixB.getFilterData().categoryBits == PlatformGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case PlatformGame.ITEM_BIT | PlatformGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == PlatformGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
        }
    }

    //two fixtures disconnect
    @Override
    public void endContact(Contact contact) {
        //PlayScreen.isJumping = true;
        Gdx.app.log("jumping", "");
    }

    //once something is collided, we change characteristics
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    //what happens as a result of the collision.
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
