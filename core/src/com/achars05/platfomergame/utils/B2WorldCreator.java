package com.achars05.platfomergame.utils;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.screens.PlayScreen;
import com.achars05.platfomergame.sprites.Coin;
import com.achars05.platfomergame.sprites.Platforms;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class B2WorldCreator {

    public B2WorldCreator (PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape pShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // create ground body/fixture
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);
            body = world.createBody(bodyDef);

            pShape.setAsBox((rect.getWidth() / 2) / MainGame.PPM,(rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = pShape;
            body.createFixture(fixtureDef);
        }

        // create platform body/fixture
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Platforms(screen, rect);
        }
        // create coins body/fixture
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen, rect);
        }

    }
}
