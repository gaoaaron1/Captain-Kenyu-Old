package com.aaron.game.entities;

import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class BackButton extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    Texture BackButton;
    Texture BackButtonClicked;

    public BackButton(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("kenyu"));
        this.world = world;

        marioStand = new TextureRegion(getTexture(), 1, 65, 70, 70);
        defineMario();
        setBounds(0, 0, 70 / PlatformGame.PPM, 70 / PlatformGame.PPM);
        setRegion(marioStand);
    }

    //Attach mario sprite to 2D body
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }


    public void defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(828 / PlatformGame.PPM, 128 / PlatformGame.PPM);  // posiiton Mario
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(25 / PlatformGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
