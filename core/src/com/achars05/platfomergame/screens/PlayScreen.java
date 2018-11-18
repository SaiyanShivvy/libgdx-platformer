package com.achars05.platfomergame.screens;

import com.achars05.platfomergame.MainGame;
import com.achars05.platfomergame.sprites.Player;
import com.achars05.platfomergame.ui.Hud;
import com.achars05.platfomergame.utils.B2WorldCreator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static sun.audio.AudioPlayer.player;

public class PlayScreen implements Screen {

    private MainGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    // Tiled Maps Variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthoCachedTiledMapRenderer mapRenderer;

    // Box2D Variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // player
    private Player player;


    public PlayScreen (MainGame game){
        this.game = game;

        // camera to follow player through the world
        gameCam = new OrthographicCamera();

        // Viewport to maintain virtual aspect ratio
        gamePort = new FitViewport(MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, gameCam);

        // HUD to display score, timer, etc
        hud = new Hud(game.batch);

        // Load tmx map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/level1.tmx");
        mapRenderer = new OrthoCachedTiledMapRenderer(map, 1 / MainGame.PPM);

        // todo: follow player using gameCam
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Box2D setup
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // Create world
        new B2WorldCreator(world, map);

        // create player
        player = new Player (world);
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            //player.b2body.applyLinearImpulse(0, 3f, 0, 0, true);
            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

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

        // Set batch to draw the HUD camera
        hud.stage.draw();
        game.batch.setProjectionMatrix(gameCam.combined);


    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

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
