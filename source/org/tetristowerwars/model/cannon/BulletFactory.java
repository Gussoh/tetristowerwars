/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.cannon;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.PolygonDef;
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

        Body body = createBody(cannon.getBodies()[0].getPosition());

        List<Vec2> vertices = new ArrayList<Vec2>(4);
        vertices.add(new Vec2(-blockSize*0.5f, -blockSize*0.5f));
        vertices.add(new Vec2(blockSize*0.5f, -blockSize*0.5f));
        vertices.add(new Vec2(blockSize*0.5f, blockSize*0.5f));
        vertices.add(new Vec2(-blockSize*0.5f, blockSize*0.5f));

        addShape(vertices, new SteelMaterial(), body);

        return new BulletBlock(new Body[] {body});

    }

    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);

        return world.createBody(boxBodyDef);
    }

    private void addShape(List<Vec2> vertices, Material mat, Body body) {

        PolygonDef shapeDef = new PolygonDef();
        shapeDef.density = mat.getDensity();
        shapeDef.vertices = vertices;
        shapeDef.isSensor = false;
        shapeDef.friction = 0.8f;
        shapeDef.restitution = 0.1f;
        body.createShape(shapeDef);
        body.setMassFromShapes();
    }

}
