package com.achars05.platfomergame.sprites;

import com.achars05.platfomergame.screens.PlayScreen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Platforms extends InteractiveTileObject {
    public Platforms (PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
    }
}
