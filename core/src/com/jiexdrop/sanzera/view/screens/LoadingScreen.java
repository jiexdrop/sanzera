package com.jiexdrop.sanzera.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jiexdrop.sanzera.Sanzera;

/**
 * Created by jiexdrop on 26/12/17.
 */

public class LoadingScreen implements Screen{
    private Sanzera parent;
    private TextureAtlas atlas;
    private AtlasRegion title;

    private Stage stage;
    private Table table;
    private Image titleImage;
    private AtlasRegion background;


    public LoadingScreen(Sanzera sanzera){
        parent = sanzera;
        stage = new Stage(new ScreenViewport());


        parent.manager.load(Sanzera.ATLAS, TextureAtlas.class);
        parent.manager.load(Sanzera.SKIN_ATLAS, TextureAtlas.class);
        parent.manager.load(Sanzera.SKIN, Skin.class, new SkinLoader.SkinParameter(Sanzera.SKIN_ATLAS));

        parent.manager.finishLoading();

        atlas = parent.manager.get(Sanzera.ATLAS, TextureAtlas.class);
        title = atlas.findRegion("lock_yellow");
        background = atlas.findRegion("bg");

    }

    @Override
    public void show() {

        titleImage = new Image(title);

        table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.setBackground(new TiledDrawable(background));


        table.add(titleImage).align(Align.center).pad(10, 0, 0, 0).colspan(10);
        table.row(); // move to next row

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if(parent.manager.isLoaded(Sanzera.SKIN)){
            parent.changeScreen(Sanzera.MENU);
        }
    }



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);
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
        stage.dispose();
    }

}
