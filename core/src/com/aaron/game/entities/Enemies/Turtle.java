package com.aaron.game.entities.Enemies;

import com.aaron.game.PlatformGame;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.aaron.game.screens.PlayScreen;

public class Turtle extends Enemy {
    public enum State {IDLE, WALKING, SHELL}
    public State currentState;
    public State previousState;

    //Animation
    private Animation idle;
    private Animation shell;

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;


    private boolean setToDestroy;
    private boolean destroyed;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        //walking frame
        frames.clear();
        for (int i = 0; i < 1; i ++)
           frames.add(new TextureRegion(screen.getAtlas().findRegion("panda"), i * 48, 0, 48, 48));
        idle = new Animation(0.4f, frames);

        walkAnimation = new Animation(0.4f, frames);
        currentState = previousState = State.WALKING;

        frames.clear();
        for (int i = 1; i < 5; i ++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("panda"), i * 48, 0, 48, 48));
        shell = new Animation(0.1f, frames);



        setBounds(getX(), getY(), 48 / PlatformGame.PPM, 48 / PlatformGame.PPM);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());  // position Mario
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / PlatformGame.PPM);

        //collide with specific objects filter
        fdef.filter.categoryBits = PlatformGame.ENEMY_BIT;
        fdef.filter.maskBits = PlatformGame.GROUND_BIT |
                PlatformGame.COIN_BIT |
                PlatformGame.BRICK_BIT |
                PlatformGame.ENEMY_BIT |
                PlatformGame.OBJECT_BIT |
                PlatformGame.MARIO_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(2 / PlatformGame.PPM);
        vertice[1] = new Vector2(5, 8).scl(2 / PlatformGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(2 / PlatformGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(2 / PlatformGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        //mario bounces
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = PlatformGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);


    }


    public TextureRegion getFrame(float dt) {
       // currentState = getState();
        TextureRegion region;

        switch (currentState) {
            case IDLE:
                region = (TextureRegion) idle.getKeyFrame(stateTime, true);
                break;
            case SHELL:
                region = (TextureRegion) shell.getKeyFrame(stateTime, true);
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        /*
        //if turtle is walking right and not left
        if (velocity.x > 0 && region.isFlipX() == false) {
            region.flip(true, false);
        }
        //if turtle is walking left
        if (velocity.x < 0 && region.isFlipX() == true) {
            region.flip(true, false);
        }
*/
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;


    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if (currentState == State.SHELL && stateTime > 5) {
            currentState = State.WALKING;
            //velocity.x = 0;
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 12 / PlatformGame.PPM);
        //b2body.setLinearVelocity(velocity);
    }

    @Override
    public void hitOnHead() {
        if (currentState != State.SHELL) {
            PlatformGame.manager.get("audio/sounds/screech.wav", Sound.class).play(0.5f);
            currentState = State.SHELL;
            //velocity.x = 0;
        }
    }
}
