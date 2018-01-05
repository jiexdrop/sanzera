package com.jiexdrop.sanzera.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jiexdrop.sanzera.Sanzera;

/**
 * Created by jiexdrop on 02/01/18.
 */

public class StaticEntity extends Entity {

    public StaticEntity(Type type, TextureRegion texture, Vector2 pos, Vector2 size) {
        super(type, texture, pos, size);

    }

    @Override
    public void update(World world, float delta) {

    }

    @Override
    public void setBody(Body body) {
        this.body = body;


        PolygonShape box = new PolygonShape();
        box.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getWidth() / 2);
        sprite.setRotation((body.getAngle()*180.f)/(float)Math.PI);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.isSensor = type.col;

        fixture = body.createFixture(fixtureDef);

        box.dispose();
    }
}
