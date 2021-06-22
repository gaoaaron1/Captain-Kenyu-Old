package com.aaron.game.Items;

import com.aaron.game.entities.Hud;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;
import com.aaron.game.entities.Mario;

public class Mushroom extends Item{
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("fragment"), 0, 0, 32, 32);
        velocity = new Vector2(0f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());  // position Mario
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / PlatformGame.PPM);
        fdef.filter.categoryBits = PlatformGame.ITEM_BIT;
        fdef.filter.maskBits = PlatformGame.MARIO_BIT |
                PlatformGame.OBJECT_BIT |
                PlatformGame.GROUND_BIT |
                PlatformGame.COIN_BIT |
                PlatformGame.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Mario mario) {
        System.out.println("it works!");
        PlatformGame.manager.get("audio/sounds/collect.wav", Sound.class).play(0.5f);
        Hud.addScore(1);
        destroy();
    }



    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
