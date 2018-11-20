package com.achars05.platfomergame.sprites.WorldObjects;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.screens.PlayScreen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameExits {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public GameExits (PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape pShape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX() + bounds.getWidth() / 2) / MainGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / MainGame.PPM);
        body = world.createBody(bodyDef);

        pShape.setAsBox((bounds.getWidth() / 2 ) / MainGame.PPM,(bounds.getHeight() / 2) / MainGame.PPM);
        fixtureDef.shape = pShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = MainGame.EXIT_BIT;
        fixtureDef.filter.maskBits = MainGame.PLAYER_BIT;
        fixture = body.createFixture(fixtureDef);

        pShape.dispose();
    }

}
