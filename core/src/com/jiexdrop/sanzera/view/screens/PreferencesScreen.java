package com.jiexdrop.sanzera.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jiexdrop.sanzera.Sanzera;

/**
 * Created by jiexdrop on 26/12/17.
 */

public class PreferencesScreen implements Screen {
    private Sanzera parent;
    private Stage stage;
    private Table table;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion background;
    private Skin skin;

    public PreferencesScreen(Sanzera sanzera) {
        parent = sanzera;
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        atlas = parent.manager.get(Sanzera.ATLAS);
        skin = parent.manager.get(Sanzera.SKIN);
        background = atlas.findRegion("grassCenter");
        table.setBackground(new TiledDrawable(background));
        stage.addActor(table);
        TextButton back = new TextButton("back", skin);

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Sanzera.MENU);
            }
        });

        table.add(back);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        int row_height = width / 12;
        int col_width = height/ 12;
    }

    @Override
    public void pause() {
        parent.changeScreen(Sanzera.MENU);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
