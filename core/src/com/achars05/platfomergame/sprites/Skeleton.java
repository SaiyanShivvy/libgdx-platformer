package com.achars05.platfomergame.sprites;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

public class Skeleton extends Enemy{

    public enum State {IDLE, WALK, ATTACK, HURT, DIE};
    public State currentState;
    public State previousState;

    private float stateTime;
    private Animation<TextureRegion> idle; // 11 frames
    private Animation<TextureRegion> walk; // 13 frames
    private Animation<TextureRegion> attack; // 16 frames
    private Animation<TextureRegion> hurt; // 8 frames
    private Animation<TextureRegion> die; // 15 frames
    Array<TextureRegion> frames;

    boolean isRightFacing;

    public Skeleton(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();

        for (int i = 0; i < 11; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("Skeleton Idle"), i*24, 0, 24, 32));
        }
        idle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 13; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("Skeleton Walk"), i*22, 0, 22, 33));
        }
        walk = new Animation<TextureRegion>(0.16f, frames);
        frames.clear();

        for (int i = 0; i < 8; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("Skeleton Hit"), i*30, 0, 30, 32));
        }
        hurt = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 16; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("Skeleton Attack"), i*43, 0, 43, 37));
        }
        attack = new Animation<TextureRegion>(0.15f, frames);
        frames.clear();

        for (int i = 0; i < 15; i++){
            frames.add(new TextureRegion(screen.getAltas().findRegion("Skeleton Dead"), i*33, 0, 33, 32));
        }
        die = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        currentState = State.IDLE;
        previousState = State.IDLE;
        isRightFacing = true;

        stateTime = 0;
        setBounds(getX(), getY(), 17 / MainGame.PPM, 22 / MainGame.PPM);
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2.5f, b2body.getPosition().y - getHeight() / 2.5f);
        setRegion(idle.getKeyFrame(stateTime, true));
//        setRegion(getFrame(dt));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        // todo: temp position, update using defined spawn location on map
        bodyDef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(8 / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = MainGame.GROUND_BIT | MainGame.PLATFORM_BIT | MainGame.ENEMY_BIT | MainGame.OBJECT_BIT | MainGame.PLAYER_BIT;
        fixtureDef.shape = cShape;
        b2body.createFixture(fixtureDef);
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;

        switch (currentState){
            case ATTACK:
                region = attack.getKeyFrame(stateTime, true);
                break;
            case HURT:
                region = hurt.getKeyFrame(stateTime);
                break;
            case DIE:
                region = die.getKeyFrame(stateTime);
                break;
            case WALK:
                region = walk.getKeyFrame(stateTime, true);
                break;
            default:
                region = idle.getKeyFrame(stateTime, true);
        }
        //if player is running left and the texture isn't facing left... flip it.
        if ((b2body.getLinearVelocity().x < 0 || !isRightFacing) && !region.isFlipX()) {
            region.flip(true, false);
            isRightFacing = false;
        }
        //if player is running right and the texture isn't facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || isRightFacing) && region.isFlipX()){
            region.flip(true, false);
            isRightFacing = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTime = currentState == previousState ? stateTime + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public State getState(){
        if (previousState == State.IDLE){
            return State.WALK;
        }
//        else if (){ // if the play is within its attack range sensor
//            return State.ATTACK;
//        }
//        else if (){ // if within players attackrange during animation
//            return State.HURT;
//        }
//        else if (){ // if health 0, need to implement health
//            return State.DIE;
//        }
        else
            return State.IDLE;
    }

    public float getStateTime(){
        return stateTime;
    }
}
