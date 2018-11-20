package com.achars05.platfomergame.utils;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.sprites.Enemy;
import com.achars05.platfomergame.sprites.InteractiveTileObject;
import com.achars05.platfomergame.sprites.Player;
import com.achars05.platfomergame.sprites.WorldObjects.Pits;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case MainGame.PLAYER_BIT | MainGame.PIT_OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.PLAYER_BIT)
                {
                    Gdx.app.log("PITFALL", "You have fallen");
                }
                break;

            case MainGame.PLAYER_BIT | MainGame.EXIT_BIT:
                if (fixA.getFilterData().categoryBits == MainGame.PLAYER_BIT){
                    Gdx.app.log("Exit", "End Level");
                }
        }

    }

    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
