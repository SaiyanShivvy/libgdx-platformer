package com.achars05.platfomergame.utils;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.screens.PlayScreen;
import com.achars05.platfomergame.sprites.Coin;
import com.achars05.platfomergame.sprites.Platforms;
import com.achars05.platfomergame.sprites.Skeleton;
import com.achars05.platfomergame.sprites.WorldObjects.GameExits;
import com.achars05.platfomergame.sprites.WorldObjects.Pits;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class B2WorldCreator {

    private Array<Skeleton> bones;

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

        // create exit
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new GameExits(screen, rect);
        }

        // create pits
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Pits(screen, rect);
        }


        // create skeletons
        bones = new Array<Skeleton>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bones.add(new Skeleton(screen, rect.getX() / MainGame.PPM, 0.1f + rect.getY() / MainGame.PPM));
        }

    }

    public Array<Skeleton> getBones() {
        return bones;
    }
}
