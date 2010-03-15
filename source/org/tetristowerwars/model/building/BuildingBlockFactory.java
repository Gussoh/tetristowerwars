/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.building;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Reeen
 */
public class BuildingBlockFactory {

    private final World world;
    private final float blockSize;

    public BuildingBlockFactory(World world, float blockSize) {
        this.world = world;
        this.blockSize = blockSize;
    }


    public BuildingBlock createSquareBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(4);
        vertices.add(new Vec2(-blockSize, -blockSize));
        vertices.add(new Vec2(blockSize, -blockSize));
        vertices.add(new Vec2(blockSize, blockSize));
        vertices.add(new Vec2(-blockSize, blockSize));

        addShape(vertices, mat, body);

        return new BuildingBlock(new Body[] {body}, mat);
    }

    public BuildingBlock createTriangleBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(3);
        vertices.add(new Vec2(-blockSize, -blockSize));
        vertices.add(new Vec2(blockSize, -blockSize));
        vertices.add(new Vec2(0, blockSize));

        addShape(vertices, mat, body);

        return new BuildingBlock(new Body[] {body}, mat);
    }
    
    public BuildingBlock createLineBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(4);
        vertices.add(new Vec2(-blockSize*0.5f, -blockSize*2));
        vertices.add(new Vec2(blockSize*0.5f, -blockSize*2));
        vertices.add(new Vec2(blockSize*0.5f, blockSize*2));
        vertices.add(new Vec2(-blockSize*0.5f, blockSize*2));

        addShape(vertices, mat, body);

        return new BuildingBlock(new Body[] {body}, mat);
    }
    public BuildingBlock createPyramidBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);
        
        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize*1.5f, -blockSize*(2f/3f)));
        vertices1.add(new Vec2(blockSize*1.5f, -blockSize*(2f/3f)));
        vertices1.add(new Vec2(blockSize*1.5f, blockSize*(1f/3f)));
        vertices1.add(new Vec2(-blockSize*1.5f, blockSize*(1f/3f)));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(-blockSize*0.5f, -blockSize*(2f/3f)));
        vertices2.add(new Vec2(blockSize*0.5f, -blockSize*(2f/3f)));
        vertices2.add(new Vec2(blockSize*0.5f, blockSize*(4f/3f)));
        vertices2.add(new Vec2(-blockSize*0.5f, blockSize*(4f/3f)));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        return new BuildingBlock(new Body[] {body}, mat);
    }
    
    public BuildingBlock createLBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize*2.5f, -blockSize*(2f/3f)));
        vertices1.add(new Vec2(blockSize*0.5f, -blockSize*(2f/3f)));
        vertices1.add(new Vec2(blockSize*0.5f, blockSize*(1f/3f)));
        vertices1.add(new Vec2(-blockSize*2.5f, blockSize*(1f/3f)));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(-blockSize*0.5f, -blockSize*(2f/3f)));
        vertices2.add(new Vec2(blockSize*0.5f, -blockSize*(2f/3f)));
        vertices2.add(new Vec2(blockSize*0.5f, blockSize*(4f/3f)));
        vertices2.add(new Vec2(-blockSize*0.5f, blockSize*(4f/3f)));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        return new BuildingBlock(new Body[] {body}, mat);
    }
    public BuildingBlock createSBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);
        
        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize*1.5f, -blockSize));
        vertices1.add(new Vec2(blockSize*0.5f, -blockSize));
        vertices1.add(new Vec2(blockSize*0.5f, 0));
        vertices1.add(new Vec2(-blockSize*1.5f, 0));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(-blockSize*0.5f, 0));
        vertices2.add(new Vec2(blockSize*1.5f, 0));
        vertices2.add(new Vec2(blockSize*1.5f, blockSize));
        vertices2.add(new Vec2(-blockSize*0.5f, blockSize));

        //Not sure this solves the problem with intersecting and interlocking pieces...
        List<Vec2> vertices3 = new ArrayList<Vec2>(4);
        vertices3.add(new Vec2(-blockSize*0.5f, -blockSize));
        vertices3.add(new Vec2(blockSize*0.5f, -blockSize));
        vertices3.add(new Vec2(blockSize*0.5f, blockSize));
        vertices3.add(new Vec2(-blockSize*0.5f, blockSize));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);
        addShape(vertices3, mat, body);

        return new BuildingBlock(new Body[] {body}, mat);
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
        body.createShape(shapeDef);
        body.setMassFromShapes();
    }

}
