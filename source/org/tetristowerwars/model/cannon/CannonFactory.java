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
import org.tetristowerwars.model.material.ConcreteMaterial;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Reeen
 */
public class CannonFactory {
    
    private final World world;
    private float blockSize;

    public CannonFactory(World world, float blockSize) {
        this.world = world;
        this.blockSize = blockSize;
    }

    public CannonBlock createBasicCannon(Vec2 pos) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize, -blockSize));
        vertices1.add(new Vec2(blockSize, -blockSize));
        vertices1.add(new Vec2(blockSize, blockSize));
        vertices1.add(new Vec2(-blockSize, blockSize));

        List<Vec2> vertices2 = new ArrayList<Vec2>(3);
        vertices2.add(new Vec2(-blockSize, blockSize));
        vertices2.add(new Vec2(blockSize, blockSize));
        vertices2.add(new Vec2(0, blockSize*2));

        addShape(vertices1, new ConcreteMaterial(), body);
        addShape(vertices2, new ConcreteMaterial(), body);

        return new CannonBlock(new Body[] {body}, 10000, 5);

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
