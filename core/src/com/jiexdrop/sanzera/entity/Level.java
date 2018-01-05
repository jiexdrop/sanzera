package com.jiexdrop.sanzera.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jiexdrop.sanzera.Sanzera;
import com.jiexdrop.sanzera.control.PlayerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jiexdrop on 04/01/18.
 */

public class Level {

    private String name;
    private ArrayList<Entity> entities;
    private World world;
    private TextureAtlas atlas;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private float accumulator = 0;
    private PlayableEntity player;
    private PlayerController playerController;
    private Type placingItem = Type.GRASS;
    private Sanzera parent;

    private Texture background;

    private boolean resetPos;
    private boolean newWorld;

    public Level(Sanzera parent, World world, TextureAtlas atlas, PlayerController playerController) {
        this.parent = parent;
        this.name = parent.prefs.getString(Sanzera.ACTUAL_LEVEL);
        camera = new OrthographicCamera(Gdx.graphics.getWidth()/Sanzera.PTM_RATIO, Gdx.graphics.getHeight()/Sanzera.PTM_RATIO);
        this.entities = new ArrayList<Entity>();

        this.world = world;

        this.atlas = atlas;
        this.playerController = playerController;

        debugRenderer = new Box2DDebugRenderer();

        background = new Texture(Gdx.files.internal("bg.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        world.setContactListener(new ListenerClass());

        load();
    }


    Vector2 cameraPos = new Vector2();


    public void update(float delta){
        cameraPos.x = camera.position.x;
        cameraPos.y = camera.position.y;
        cameraPos.lerp(player.body.getPosition(), delta * 24);
        camera.position.set(cameraPos.x, cameraPos.y, 0);
        camera.update();


        if(playerController.isDebug()) {
            debugRenderer.render(world, camera.combined);
            placeBlocks();

            if(Gdx.input.isKeyJustPressed(Input.Keys.PLUS)){
                this.placingItem = placingItem.next();
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS)){
                this.placingItem = placingItem.previous();
            }

            if(playerController.placeBlock()){
                TextureRegion textToAdd = atlas.findRegion(placingItem.name);


                Entity toAdd = new StaticEntity(placingItem, textToAdd, toGrid(posCursor), new Vector2(3,3));
                toAdd.setBody(world.createBody(toAdd.bodyDef));
                entities.add(toAdd);

            }

            if(playerController.placeFlag()){
                Entity toAdd = new StaticEntity(Type.FLAG_BLUE, atlas.findRegion(Type.FLAG_BLUE.name), toGrid(posCursor), new Vector2(3,3));
                toAdd.setBody(world.createBody(toAdd.bodyDef));

                entities.add(toAdd);
            }

            if(playerController.removeBlock()){
                Entity toRemove = findBlock(toGrid(posCursor));
                if(toRemove !=null) {
                    world.destroyBody(toRemove.body);
                    entities.remove(toRemove);
                }
            }
        }
        if(playerController.saveLevel()){
            System.out.println("SAVE");
            save();
        }

        for (Entity e: entities) {
            e.update(world, delta);
        }

        if(resetPos){
            resetPos = false;
            player.body.setTransform(new Vector2(0,10),0);
        }

        if(newWorld){
            newWorld = false;
            load();
        }


        doPhysicsStep(delta);


    }

    private Entity findBlock(Vector2 vector2) {
        for (Entity e: entities) {
            if(e.body !=null)
                for (Fixture fix: e.body.getFixtureList()){
                    if(fix.testPoint(vector2.x, vector2.y))  return e;
                }
        }
        return null;
    }

    private Vector2 toGrid(Vector3 position) {
        int x = (int)position.x;
        int y = (int)position.y;
        return new Vector2(x,y);
    }


    Vector3 posCursor;
    private void placeBlocks() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();

        posCursor = camera.unproject(new Vector3(x, y, 0));

    }


    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Sanzera.TIME_STEP) {
            world.step(Sanzera.TIME_STEP, Sanzera.VELOCITY_ITERATIONS, Sanzera.POSITION_ITERATIONS);
            accumulator -= Sanzera.TIME_STEP;
        }
    }

    public Type getPlacingItem() {
        return placingItem;
    }

    public void draw(SpriteBatch batch){
        //batch.totalRenderCalls = 0;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, cameraPos.x -  Gdx.graphics.getWidth()/2, cameraPos.y - Gdx.graphics.getHeight()/2, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (Entity e: entities) {
            e.draw(batch);
        }
        batch.end();

        //System.out.println(batch.totalRenderCalls);
    }


    public void createPlayer() {

        player = new PlayableEntity(playerController, Type.TOCH_LIT, atlas.findRegion(Type.TOCH_LIT.name), new Vector2(0, 10), new Vector2(2, 2));
        player.setBody(world.createBody(player.bodyDef));
        entities.add(player);

    }


    public void createTile(){
        Entity entity = new StaticEntity(Type.GRASS, atlas.findRegion(Type.GRASS.name), new Vector2(), new Vector2(3, 3));
        entity.setBody(world.createBody(entity.bodyDef));
        entities.add(entity);
    }


    public void save() {
        FileHandle fl = Gdx.files.local(name + Sanzera.DAT_FILE_SAVE);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(fl.write(false));
            ArrayList<Entity> toAdd = new ArrayList<Entity>(entities);
            toAdd.remove(player);
            out.writeObject(toAdd);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void load(){
        entities.clear();
        //clear world

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for(int i = 0; i < bodies.size; i++)
        {
            if(!world.isLocked())
                world.destroyBody(bodies.get(i));
        }

        name = parent.prefs.getString(Sanzera.ACTUAL_LEVEL);
        FileHandle fl = Gdx.files.internal(name + Sanzera.DAT_FILE_SAVE);
        ObjectInputStream in;
        try {
            System.out.println("Loaded " + name + Sanzera.DAT_FILE_SAVE + " e - " + fl.exists());
            if(fl.exists()) {
                in = new ObjectInputStream(fl.read());
                ArrayList<Entity> recoverEntities = (ArrayList<Entity>) in.readObject();

                for (Entity e: recoverEntities) {
                    Entity entity = new StaticEntity(e.type, atlas.findRegion(e.type.name), e.position, e.size);
                    entity.setBody(world.createBody(entity.bodyDef));
                    entities.add(entity);
                }

                createPlayer();

                in.close();
            } else {
                createPlayer();
                createTile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void resetPos(){
        resetPos = true;
    }


    public class ListenerClass implements ContactListener {
        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }

        @Override
        public void beginContact(Contact contact) {
            for (Entity e:entities) {
                if(e.fixture!=null) {
                    if (e.fixture.equals(contact.getFixtureA())) {
                        if(e.type.equals(Type.FLAG_BLUE)) {

                            if(!newWorld) {
                                parent.nextLevel();
                                newWorld = true;
                            }
                            return;

                        }
                        if(e.type.equals(Type.WATER) || e.type.equals(Type.WATER_MID) || e.type.equals(Type.SPIKES)) {
                            resetPos();
                            break;
                        }
                    }
                }
            }
        }
    };
}


