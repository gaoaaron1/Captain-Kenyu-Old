package com.aaron.game.tools;

import com.aaron.game.entities.Enemies.Goomba;
import com.aaron.game.entities.Enemies.Turtle;
import com.aaron.game.entities.Enemies.Enemy;
import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.aaron.game.PlatformGame;
import com.aaron.game.entities.Brick;
import com.aaron.game.entities.Coin;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {

    //Let's place goombas on tile editor map.
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen) {

        //We will with world and tiles through method calling.
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //Create body and fixtures variable
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PlatformGame.PPM, (rect.getY() + rect.getHeight() / 2) / PlatformGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PlatformGame.PPM, rect.getHeight() / 2 / PlatformGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        //create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PlatformGame.PPM, (rect.getY() + rect.getHeight() / 2) / PlatformGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PlatformGame.PPM, rect.getHeight() / 2 / PlatformGame.PPM);
            fdef.filter.categoryBits = PlatformGame.OBJECT_BIT; //enemy collides with objects
            fdef.shape = shape;
            body.createFixture(fdef);
        }


        //create brick bodies/fixtures
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);

        }

        //create coin bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rect);
        }

        //create pandas from tile map editor.
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / PlatformGame.PPM, rect.getY() / PlatformGame.PPM));
        }
        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, rect.getX() / PlatformGame.PPM, rect.getY() / PlatformGame.PPM));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
