package com.achars05.platfomergame.sprites;

import com.achars05.platfomergame.MainGame;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {

    public World world;
    public Body b2body;

    public Player (World world) {
        this.world = world;
        definePlayer();
    }

    public void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        // todo: temp position, update using defined spawn location on map
        bodyDef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(5 / MainGame.PPM);

        fixtureDef.shape = cShape;
        b2body.createFixture(fixtureDef);
    }

}
