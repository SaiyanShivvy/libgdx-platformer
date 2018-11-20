package com.achars05.platfomergame.sprites;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.screens.PlayScreen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Coin extends InteractiveTileObject {

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
    }
}
