package com.jiexdrop.sanzera.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.io.Serializable;

/**
 * Created by jiexdrop on 02/01/18.
 */

public abstract class Entity implements Serializable {

    final static float MAX_VELOCITY = 7f;

    public transient Sprite sprite;

    public Vector2 size;
    public Vector2 position;

    public transient Body body;

    public transient BodyDef bodyDef = new BodyDef();
    public transient Fixture fixture;

    public Type type;

    public Entity(Type type, TextureRegion texture, Vector2 pos, Vector2 size) {
        this.type = type;
        this.sprite = new Sprite(texture);
        this.size = size;
        bodyDef.position.set(pos);
        this.position = bodyDef.position;

        float ratio= (float)texture.getRegionWidth()/ (float)texture.getRegionHeight();

        sprite.setSize(size.x * ratio, size.y);
        sprite.setOrigin((size.x * ratio)/2, (size.y)/2);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public abstract void update(World world, float delta);

    public abstract void setBody(Body body);
}
