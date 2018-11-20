package com.achars05.platfomergame.ui;

import com.achars05.platfomergame.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Vpad implements Disposable {
    public Stage stage;
    public Touchpad touchpad;
    public OrthographicCamera cam;
    private Viewport viewport;
    boolean pressJump, pressLeft, pressRight, pressAction;

    /*
public Vpad(SpriteBatch sb) {
viewport = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, new OrthographicCamera());
stage = new Stage(viewport, sb);

Gdx.input.setInputProcessor(stage);

Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

touchpad = new Touchpad(20, skin);
touchpad.setBounds(15, 15, 100, 100);
stage.addActor(touchpad);
}
*/

    public Vpad(SpriteBatch sb) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 400, cam);
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();

        Image leftIcon = new Image(new Texture("ui/btnLeft.png"));
        leftIcon.setSize(50, 50);
        leftIcon.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressLeft = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressLeft = false;
            }
        });

        Image rightIcon = new Image(new Texture("ui/btnRight.png"));
        rightIcon.setSize(50, 50);
        rightIcon.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressRight = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressRight = false;
            }
        });

        Image jumpIcon = new Image(new Texture("ui/btnJump.png"));
        jumpIcon.setSize(50, 50);
        jumpIcon.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressJump = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressJump = false;
            }
        });

        Image actionIcon = new Image(new Texture("ui/btnAction.png"));
        actionIcon.setSize(50, 50);
        actionIcon.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressAction = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressAction = false;
            }
        });
        table.bottom().left();
        table.row();
        table.add(leftIcon).size(leftIcon.getWidth(), leftIcon.getHeight()).padLeft(10).padRight(15).padBottom(10);
        table.add(rightIcon).size(rightIcon.getWidth(), rightIcon.getHeight()).padRight(265).padBottom(10);
        table.add(jumpIcon).size(jumpIcon.getWidth(), jumpIcon.getHeight()).padLeft(265).padBottom(10);
        table.add(actionIcon).size(actionIcon.getWidth(), actionIcon.getHeight()).padLeft(15).padBottom(50);
        stage.addActor(table);
    }

    public Vpad() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 400, cam);
    }

    public boolean isPressJump() {
        return pressJump;
    }

    public boolean isPressLeft() {
        return pressLeft;
    }

    public boolean isPressRight() {
        return pressRight;
    }

    public boolean isPressAction() {
        return pressAction;
    }

    public void resize (int width, int height) {
        viewport.update(width, height);
    }

    public void dispose () {
        stage.dispose();
    }
}
