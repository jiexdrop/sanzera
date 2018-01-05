package com.jiexdrop.sanzera.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.jiexdrop.sanzera.control.PlayerController;

/**
 * Created by jiexdrop on 04/01/18.
 */

public class PlayableEntity extends DynamicEntity {
    private transient PlayerController controller;

    @Override
    public void setBody(Body body) {
        this.body = body;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(0.5f, 1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 1f;
        this.fixture = body.createFixture(fixtureDef);
        poly.dispose();

        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);
        circle.setPosition(new Vector2(0, -1f));
        fixtureDef.density = 0f;
        fixtureDef.shape = circle;
        sensorFixture = body.createFixture(fixtureDef);
        circle.dispose();

        body.setFixedRotation(true);
        body.setBullet(true);
    }

    @Override
    public void update(World world, float delta) {
        super.update(world, delta);
        boolean grounded = isPlayerGrounded(world, delta);

        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();


        if(Math.abs(vel.x) > MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * MAX_VELOCITY;
            body.setLinearVelocity(vel.x, vel.y);
        }

        // calculate stilltime & damp
        if(!controller.lefting() && !controller.goingRight()) {
            stillTime += delta;
            body.setLinearVelocity(vel.x * 0.9f, vel.y);
        }
        else {
            stillTime = 0;
        }


        if(!grounded) {
            fixture.setFriction(0f);
            sensorFixture.setFriction(0f);

            body.applyForceToCenter(new Vector2(0f, -98.1f), true);

        } else {

            if(!controller.lefting() && !controller.goingRight() && stillTime > 0.2) {
                fixture.setFriction(100f);
                sensorFixture.setFriction(100f);
            }
            else {
                fixture.setFriction(1f);
                sensorFixture.setFriction(1f);
            }


        }

        if(controller.lefting()){
            body.applyLinearImpulse(-2f, 0, pos.x, pos.y, true);
        }
        if(controller.goingRight()){
            body.applyLinearImpulse(2f, 0, pos.x, pos.y,true);
        }


        if(controller.jumping()){
            if(grounded) {
                body.setLinearVelocity(vel.x, 0);
                body.setTransform(pos.x, pos.y + 0.01f, 0);
                body.applyLinearImpulse(0, 50, pos.x, pos.y, true);
            }
        }

    }

    public PlayableEntity(PlayerController controller, Type type, TextureRegion texture, Vector2 pos, Vector2 size) {
        super(type, texture, pos, size);
        this.controller = controller;
    }


}
