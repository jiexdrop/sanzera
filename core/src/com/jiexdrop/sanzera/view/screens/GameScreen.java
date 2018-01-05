package com.jiexdrop.sanzera.view.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jiexdrop.sanzera.Sanzera;
import com.jiexdrop.sanzera.control.PlayerController;
import com.jiexdrop.sanzera.entity.Level;
import com.jiexdrop.sanzera.entity.Type;


public class GameScreen implements Screen
{
    private Sanzera parent;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Skin skin;

    private Level level;

    private Stage stage;


    private TextButton right;
    private TextButton left;
    private TextButton jump;
    private Image item;

    //Now set a Pixels to Meters ratio

    private PlayerController controller;

    private World world;

    public GameScreen(Sanzera sanzera) {
        parent = sanzera;
        batch = new SpriteBatch();
        skin = parent.manager.get(Sanzera.SKIN);
        atlas = parent.manager.get(Sanzera.ATLAS, TextureAtlas.class);
        stage = new Stage(new ScreenViewport());

        world = new World(new Vector2(0, -9.81f), true);

        left = new TextButton("Left", skin);
        right = new TextButton("Right", skin);
        jump = new TextButton("Jump", skin);

        Gdx.input.setInputProcessor(stage);
        controller = new PlayerController(left, right, jump);
        level = new Level(parent, world, atlas, controller);
    }

    @Override
    public void show() {



    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f,0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        level.update(delta);

        level.draw(batch);

        stage.act(delta);
        stage.draw();

        if(controller.newLevel()){
            parent.prefs.putString(Sanzera.ACTUAL_LEVEL, parent.addLevel());
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if(controller.isDebug()) {

            item.setDrawable(new TextureRegionDrawable(atlas.findRegion(level.getPlacingItem().name)));
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) parent.changeScreen(Sanzera.MENU);
    }



    @Override
    public void resize(int width, int height) {
        stage.clear();
        stage.getViewport().update(width, height, true);


        Table table = new Table();
        table.setFillParent(true);


        item = new Image(atlas.findRegion(Type.GRASS.name));

        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;
        

        table.top().add(item).row();

        table.bottom().row();
        table.add(left).size(col_width*4,(float)(row_height*2));
        table.add(right).size(col_width*4,(float)(row_height*2));
        table.add(jump).size(col_width*4,(float)(row_height*2));


        stage.addActor(table);

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
        world.dispose();
        stage.dispose();
        atlas.dispose();
    }

}
