package com.achars05.platfomergame.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Platforms extends InteractiveTileObject {
    public Platforms (World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
    }
}
