package com.achars05.platfomergame.sprites;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.screens.PlayScreen;
import com.achars05.platfomergame.ui.Vpad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import sun.applet.Main;

public class Player extends Sprite {

    public enum State {FALLING, JUMPING, IDLE, RUNNING, ATTACKING};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion stand;
    private float stateTimer;
    private boolean isRunningRight;

    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> jump;
    private Animation<TextureRegion> fall;
    private Animation<TextureRegion> attack;

    PlayScreen screen;

    public Player (PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();

        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        isRunningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 3; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("player_idle"), i*50, 0, 50, 37));
        }
        idle = new Animation<TextureRegion>(0.3f, frames);

        frames.clear();

        for (int i = 0; i < 6; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("player_run"), i*50, 0, 50, 37));
        }
        run = new Animation<TextureRegion>(0.1f, frames, Animation.PlayMode.LOOP);

        frames.clear();

        for (int i = 1; i < 3; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("player_jump"), i*50, 0, 50, 37));
        }
        jump = new Animation<TextureRegion>(0.2f, frames, Animation.PlayMode.NORMAL);

        frames.clear();

        for (int i = 0; i < 1; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("player_fall"), i*50, 0, 50, 37));
        }
        fall = new Animation<TextureRegion>(0.2f, frames, Animation.PlayMode.LOOP);

        frames.clear();

        for (int i = 0; i < 16; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("player_attack"), i*52 , 0, 50, 37));
        }
        attack = new Animation<TextureRegion>(0.07f, frames);

        frames.clear();

        // stand = new TextureRegion(getTexture(), super.getRegionX(), super.getRegionY(), 50,37);

        // define the player in box2d
        definePlayer();

        // set initial values for location, etc.
        setBounds(0,0, 33 / MainGame.PPM, 23 / MainGame.PPM);
        setRegion(idle.getKeyFrame(stateTimer, true));
    }

    public void update (float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 3);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;

        switch (currentState){
            case ATTACKING:
                region = attack.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                region = jump.getKeyFrame(stateTimer);
                break;
            case FALLING:
                region = fall.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = run.getKeyFrame(stateTimer, true);
                break;
            default:
                region = idle.getKeyFrame(stateTimer, true);
        }
        //if player is running left and the texture isn't facing left... flip it.
        if ((b2body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = false;
        }
        //if player is running right and the texture isn't facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()){
            region.flip(true, false);
            isRunningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public State getState(){
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || screen.getVpad().isPressAction()){
            return State.ATTACKING;
        }
        else
            return State.IDLE;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        // todo: temp position, update using defined spawn location on map
        bodyDef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(8 / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = MainGame.GROUND_BIT | MainGame.PLATFORM_BIT | MainGame.OBJECT_BIT | MainGame.ENEMY_BIT;
        fixtureDef.shape = cShape;
        b2body.createFixture(fixtureDef);

        // todo: add checking sensor for collison on attack frames.
        PolygonShape attackRangeFront = new PolygonShape();
        attackRangeFront.setAsBox(7 / MainGame.PPM, 13 / MainGame.PPM, new Vector2(0.1f, 0.05f), 0);
        fixtureDef.shape = attackRangeFront;
        fixtureDef.isSensor = true;
        b2body.createFixture(fixtureDef).setUserData("attackRangeLeft");

        PolygonShape attackRangeBack = new PolygonShape();
        attackRangeBack.setAsBox(7 / MainGame.PPM, 13 / MainGame.PPM, new Vector2(-0.1f, 0.05f), 0);
        fixtureDef.shape = attackRangeBack;
        fixtureDef.isSensor = true;
        b2body.createFixture(fixtureDef).setUserData("attackRangeRight");

    }

}
