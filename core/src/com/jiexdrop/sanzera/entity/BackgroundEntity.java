package com.jiexdrop.sanzera.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by jiexdrop on 04/01/18.
 */

public class BackgroundEntity extends Entity {
    private Vector2 pos;

    public BackgroundEntity(Type type, TextureRegion texture, Vector2 pos, Vector2 size) {
        super(type, texture, pos, size);
        this.pos = pos;
    }

    @Override
    public void update(World world, float delta) {
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getWidth() / 2);
    }

    @Override
    public void setBody(Body body) {

    }
}
