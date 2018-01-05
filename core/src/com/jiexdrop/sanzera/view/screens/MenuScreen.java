package com.jiexdrop.sanzera.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jiexdrop.sanzera.Sanzera;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jiexdrop on 26/12/17.
 */

public class MenuScreen implements Screen {

    private Sanzera parent;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion background;


    public MenuScreen(Sanzera sanzera) {
        parent = sanzera;
        stage = new Stage(new ScreenViewport());

        parent.manager.finishLoading();
        skin = parent.manager.get(Sanzera.SKIN);
        atlas = parent.manager.get(Sanzera.ATLAS);
        background = atlas.findRegion("grassCenter");

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when teh screen size is changed
        stage.clear();

        stage.getViewport().update(width, height, true);

        final Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        table.setBackground(new TiledDrawable(background));

        //create buttons
        TextButton play = new TextButton("Play", skin);
        final TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        final int row_height = width / 12;
        final int col_width = height/ 12;

        table.add(play).fillX().uniformX().size(col_width*4,row_height);
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX().size(col_width*4,row_height);
        table.row();
        table.add(exit).fillX().uniformX().size(col_width*4,row_height);

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                table.clearChildren();

                for (final String s: parent.levels) {
                    TextButton button = new TextButton(s, skin);

                    button.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            parent.prefs.putString(Sanzera.ACTUAL_LEVEL, s);
                            parent.prefs.flush();
                            parent.changeScreen(Sanzera.APPLICATION);
                        }
                    });

                    table.add(button).size(col_width*3,row_height).row();
                }
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(Sanzera.PREFERENCES);
            }
        });

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
        // dispose of assets when not needed anymore
        stage.dispose();
    }
}
