package com.jiexdrop.sanzera.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.jiexdrop.sanzera.control.PlayerController;

import javax.xml.soap.Text;

/**
 * Created by jiexdrop on 03/01/18.
 */

public class DynamicEntity extends Entity {

    public transient Fixture sensorFixture;

    private Entity groundedPlatform = null;

    float stillTime = 0;

    public DynamicEntity(Type type, TextureRegion texture, Vector2 pos, Vector2 size) {
        super(type, texture, pos, size);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void update(World world, float delta) {
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getWidth() / 2);
        sprite.setRotation((body.getAngle()*180.f)/(float)Math.PI);
    }

    @Override
    public void setBody(Body body) {
        this.body = body;

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(size.x/2,size.y/2);
        this.fixture = body.createFixture(poly, 1);
        poly.dispose();

    }


    protected boolean isPlayerGrounded(World world, float deltaTime) { //TODO Generalize
        groundedPlatform = null;
        Array<Contact> contactList = world.getContactList();
        for(int i = 0; i < contactList.size; i++) {
            Contact contact = contactList.get(i);
            if(contact.isTouching() && (contact.getFixtureA() == sensorFixture ||
                    contact.getFixtureB() == sensorFixture)) {

                Vector2 pos = body.getPosition();
                WorldManifold manifold = contact.getWorldManifold();
                boolean below = true;
                for(int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
                    below &= (manifold.getPoints()[j].y < pos.y - 1f);
                }

                if(below) {
                    if(contact.getFixtureA().getUserData() != null) {
                        groundedPlatform = (Entity) contact.getFixtureA().getBody().getUserData();
                    }

                    if(contact.getFixtureB().getUserData() != null) {
                        groundedPlatform = (Entity) contact.getFixtureB().getBody().getUserData();
                    }
                    return true;
                }

                return false;
            }
        }
        return false;
    }

}
