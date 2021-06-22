package com.aaron.game.entities;

import com.aaron.game.screens.PlayScreen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.aaron.game.PlatformGame;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected PlayScreen screen;

    //Object collisions
    protected Fixture fixture;
    public abstract void onFeetTouch(Mario mario);
    public abstract void onHeadHit(Mario mario);

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        //create coin
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PlatformGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / PlatformGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / PlatformGame.PPM, bounds.getHeight() / 2 / PlatformGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }


    //Collision filter
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * PlatformGame.PPM / 32),
                (int)(body.getPosition().y * PlatformGame.PPM / 32));
    }

    public TiledMapTileLayer.Cell getCell2() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * PlatformGame.PPM / 33),
                (int)(body.getPosition().y * PlatformGame.PPM / 32));
    }

    public TiledMapTileLayer.Cell getCell3() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * PlatformGame.PPM / 32),
                (int)(body.getPosition().y * PlatformGame.PPM / 33));
    }

    public TiledMapTileLayer.Cell getCell4() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * PlatformGame.PPM / 33),
                (int)(body.getPosition().y * PlatformGame.PPM / 33));
    }



}
