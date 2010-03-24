/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;

/**
 *
 * @author Reeen
 */
public class BulletFactory {
    private final World world;
    private final float blockSize;

    public BulletFactory(World world, float blockSize) {
        this.world = world;
        this.blockSize = blockSize;
    }

    public BulletBlock createBullet(CannonBlock cannon) {

        Vec2 canPos = cannon.getBody().getPosition();
        Body body = createBody(new Vec2(canPos.x-blockSize-1, canPos.y+blockSize+1));

        addShape(blockSize/2, new SteelMaterial(), body);

        body.applyImpulse(new Vec2(-cannon.getForce(), cannon.getForce()), body.getPosition());

        return new BulletBlock(body, cannon);
    }

    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);


        return world.createBody(boxBodyDef);
    }

    private void addShape(float radius, Material mat, Body body) {

        CircleDef shapeDef = new CircleDef();
        shapeDef.density = mat.getDensity();
        shapeDef.radius = radius;
        shapeDef.localPosition = new Vec2(0,0); //????
        shapeDef.isSensor = false;
        shapeDef.friction = 0.8f;
        shapeDef.restitution = 0.1f;
        body.createShape(shapeDef);
        body.setMassFromShapes();
    }
}
