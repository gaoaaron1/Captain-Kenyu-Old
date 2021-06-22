package com.aaron.game.entities;

import com.aaron.game.PlatformGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.utils.Array;

public class Mario extends Sprite {

    //Constants for different actions that will be animated.
    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;

    //Animation
    public com.badlogic.gdx.graphics.g2d.Animation marioRun;
    public Animation marioJump;
    private float stateTimer; //keep track of state time.
    private boolean runningRight;


    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    Texture BackButton;
    Texture BackButtonClicked;

    public Mario(PlayScreen screen) {
        super(screen.getAtlas().findRegion("kenyu"));
        this.world = screen.getWorld();

        //Animation constructors
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;


        //Set up our frames for running.
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 6; i++)
            frames.add(new TextureRegion(getTexture(), i * 70, 65, 70, 70));
        marioRun = new Animation(0.2f, frames);
        frames.clear();

        //Set up our frames for jumping
        for (int i = 4; i < 8; i++)
            frames.add(new TextureRegion(getTexture(), i * 70, 65, 70, 70));
        marioJump = new Animation(0.1f, frames);

        marioStand = new TextureRegion(getTexture(), 1, 65, 70, 70);
        defineMario();
        setBounds(0, 0, 70 / PlatformGame.PPM, 70 / PlatformGame.PPM);
        setRegion(marioStand);
    }

    //Attach mario sprite to 2D body
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        //Animations
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch(currentState) {
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        //runs left
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void defineMario() {
        //Body collision body creation
        BodyDef bdef = new BodyDef();
        bdef.position.set(128 / PlatformGame.PPM, 128 / PlatformGame.PPM);  // posiiton Mario
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(25 / PlatformGame.PPM);

        //collide with specific objects filter
        fdef.filter.categoryBits = PlatformGame.MARIO_BIT;
        fdef.filter.maskBits = PlatformGame.GROUND_BIT |
                PlatformGame.COIN_BIT |
                PlatformGame.BRICK_BIT |
                PlatformGame.ENEMY_BIT |
                PlatformGame.OBJECT_BIT |
                PlatformGame.ENEMY_HEAD_BIT |
                PlatformGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


        //Feet collision body creation
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-15 / PlatformGame.PPM, -25 / PlatformGame.PPM), new Vector2(15 / PlatformGame.PPM, -25 / PlatformGame.PPM));
        fdef.filter.categoryBits = PlatformGame.MARIO_FEET_BIT;
        fdef.shape = feet;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }


public void jump() {
    if (currentState != State.JUMPING) {
        b2body.applyLinearImpulse(new Vector2(0, 5.5f), b2body.getWorldCenter(), true);
        currentState = State.JUMPING;
    }
}
}