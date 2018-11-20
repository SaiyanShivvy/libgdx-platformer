package com.achars05.platfomergame.utils;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.sprites.Enemy;
import com.achars05.platfomergame.sprites.InteractiveTileObject;
import com.achars05.platfomergame.sprites.Player;
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
            case MainGame.ENEMY_HITBOX_BIT | MainGame.PLAYER_HITBOX_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.ENEMY_HITBOX_BIT) {
                    ((Enemy)fixA.getUserData()).killed((Player) fixB.getUserData());
                    if (fixB.getUserData().equals("hitRight")){
                        Gdx.app.log("Contact", "R");
                    } else {
                        Gdx.app.log("Contact", "L");
                    }
                }
                else {
                    ((Enemy)fixB.getUserData()).killed((Player) fixA.getUserData());
                    if (fixA.getUserData().equals("hitRight")){
                        Gdx.app.log("Contact", "R");
                    } else {
                        Gdx.app.log("Contact", "L");
                    }
                }
                break;
            case MainGame.ENEMY_BIT | MainGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MainGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
//            case MainGame.ENEMY_BIT | MainGame.ENEMY_BIT:
//                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
//                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
//                break;

//             case MainGame.PLAYER_BIT | MainGame.ENEMY_BIT: // When Plauer
//                if(fixA.getFilterData().categoryBits == MainGame.PLAYER_BIT)
//                    ((Player) fixA.getUserData()).hit((Enemy)fixB.getUserData());
//                else
//                    ((Player) fixB.getUserData()).hit((Enemy)fixA.getUserData());
//                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
