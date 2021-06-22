package com.aaron.game.entities.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.aaron.game.PlatformGame;
import com.aaron.game.screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    public static boolean rage = false;
    private boolean setToDestroy;
    private boolean destroyed;

    public Animation explosion;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        //create frame
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 1; i ++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("panda"), i * 48, 0, 48, 48));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 48 / PlatformGame.PPM, 48 / PlatformGame.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt) {
        stateTime += dt;

        //destroy goomba
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("panda"), 48, 0, 48, 48));
            stateTime = 0;
        }
        else if (!destroyed) {
            if (rage == true) {
                frames = new Array<TextureRegion>();
                for (int i = 3; i < 5; i++)
                    frames.add(new TextureRegion(screen.getAtlas().findRegion("panda"), i * 48, 0, 48, 48));
                explosion = new Animation(0.5f, frames);
                setRegion((TextureRegion) explosion.getKeyFrame(stateTime, true));
            }
            else if (rage == false) {
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 12 / PlatformGame.PPM);
                setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
            }
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());   //Position enemy
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


    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }


    @Override
    public void hitOnHead() {
        PlatformGame.manager.get("audio/sounds/cry.wav", Sound.class).play(0.1f);
        System.out.println("Fuck!");
    }
}
