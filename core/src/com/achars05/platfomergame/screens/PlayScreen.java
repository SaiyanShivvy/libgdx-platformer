package com.achars05.platfomergame.screens;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.sprites.Enemy;
import com.achars05.platfomergame.sprites.Player;
import com.achars05.platfomergame.sprites.Skeleton;
import com.achars05.platfomergame.ui.Hud;
import com.achars05.platfomergame.ui.Vpad;
import com.achars05.platfomergame.utils.B2WorldCreator;
import com.achars05.platfomergame.utils.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class PlayScreen implements Screen {

    private MainGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private Vpad vpad;

    // Sprites
    private TextureAtlas altas;

    // Tiled Maps Variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthoCachedTiledMapRenderer mapRenderer;

    // Box2D Variables
    private World world;
    private Box2DDebugRenderer b2dr;
    B2WorldCreator creator;

    // player
    private Player player;


    public PlayScreen (MainGame game){
        this.game = game;

        // create atlas
        altas = new TextureAtlas("characters/sprites.atlas");

        // camera to follow player through the world
        gameCam = new OrthographicCamera();

        // Viewport to maintain virtual aspect ratio
        gamePort = new FitViewport(MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, gameCam);

        // HUD to display score, timer, etc
        hud = new Hud(game.batch);

        // Touchpad
        vpad = new Vpad(game.batch);

        // Load tmx map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/level1.tmx");
        mapRenderer = new OrthoCachedTiledMapRenderer(map, 1 / MainGame.PPM);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Box2D setup
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // Create world
        creator = new B2WorldCreator(this);

        // create player
        player = new Player (this);

        world.setContactListener(new WorldContactListener());
    }

    @Override
    public void show() {

    }

    public TextureAtlas getAltas() {
        return altas;
    }

    public Vpad getVpad() {
        return vpad;
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        // Virtual Controls
        if ((vpad.isPressLeft()) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        else if ((vpad.isPressRight()) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        else if (vpad.isPressJump() && player.b2body.getLinearVelocity().y == 0) {
            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);

        for(Enemy enemy : creator.getBones()) {
            enemy.update(dt);
        }

        hud.update(dt);

        gameCam.position.x = player.b2body.getPosition().x;
        // update game camera
        gameCam.update();
        // tell renderer to draw only what the camera can see of the game world
        mapRenderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        // separate update logic from render
        update(delta);

        // clear game screen with black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render game map
        mapRenderer.render();

        // render Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getBones()) {
            enemy.draw(game.batch);
        }
        game.batch.end();

        // Set batch to draw the HUD camera
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        game.batch.setProjectionMatrix(vpad.stage.getCamera().combined);
        vpad.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
