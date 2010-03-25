/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    private final Set<BuildingBlock> buildingBlockPool;
    private final World world;
    private final float blockSize;

    public BuildingBlockFactory(Set<BuildingBlock> buildingBlockPool, World world, float blockSize) {
        this.buildingBlockPool = buildingBlockPool;
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

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(-blockSize, -blockSize, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(0, -blockSize, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize, 0, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(0, 0, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    public BuildingBlock createTriangleBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(3);
        vertices.add(new Vec2(-blockSize, -blockSize));
        vertices.add(new Vec2(blockSize, -blockSize));
        vertices.add(new Vec2(0, (float) (Math.sqrt(3) * blockSize - blockSize)));

        addShape(vertices, mat, body);

        Path2D trianglePath = new Path2D.Float();
        trianglePath.moveTo(-blockSize, -blockSize);
        trianglePath.lineTo(blockSize, -blockSize);
        trianglePath.lineTo(0, (float) (Math.sqrt(3) * blockSize - blockSize));
        trianglePath.closePath();

        BuildingBlock block = new TriangularBuildingBlock(body, mat, new Path2D[]{trianglePath});
        buildingBlockPool.add(block);

        return block;
    }

    // Mostly to test triangle-combos
    public BuildingBlock createGemBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(4);
        vertices.add(new Vec2(-blockSize, 0));
        vertices.add(new Vec2(0, (float) -(Math.sqrt(3) * blockSize)));
        vertices.add(new Vec2(blockSize, 0));
        vertices.add(new Vec2(0, (float) (Math.sqrt(3) * blockSize)));

        addShape(vertices, mat, body);

        Path2D.Float[] trianglePaths = new Path2D.Float[2];

        trianglePaths[0] = new Path2D.Float();
        trianglePaths[0].moveTo(-blockSize, 0);
        trianglePaths[0].lineTo(blockSize, 0);
        trianglePaths[0].lineTo(0, (float) (Math.sqrt(3) * blockSize));
        trianglePaths[0].closePath();

        trianglePaths[1] = new Path2D.Float();
        trianglePaths[1].moveTo(-blockSize, 0);
        trianglePaths[1].lineTo(0, (float) -(Math.sqrt(3) * blockSize));
        trianglePaths[1].lineTo(blockSize, 0);
        trianglePaths[1].closePath();

        BuildingBlock block = new TriangularBuildingBlock(body, mat, trianglePaths);
        buildingBlockPool.add(block);

        return block;
    }

    public BuildingBlock createLineBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices = new ArrayList<Vec2>(4);
        vertices.add(new Vec2(-blockSize * 0.5f, -blockSize * 2));
        vertices.add(new Vec2(blockSize * 0.5f, -blockSize * 2));
        vertices.add(new Vec2(blockSize * 0.5f, blockSize * 2));
        vertices.add(new Vec2(-blockSize * 0.5f, blockSize * 2));

        addShape(vertices, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(-blockSize / 2, -blockSize * 2, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize, -blockSize, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize, 0, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(-blockSize, blockSize, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    public BuildingBlock createCrossBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        List<Vec2> vertices2 = new ArrayList<Vec2>(4);

        vertices1.add(new Vec2(-blockSize * 0.5f, -blockSize * 1.5f));
        vertices1.add(new Vec2(blockSize * 0.5f, -blockSize * 1.5f));
        vertices1.add(new Vec2(blockSize * 0.5f, blockSize * 1.5f));
        vertices1.add(new Vec2(-blockSize * 0.5f, blockSize * 1.5f));

        vertices2.add(new Vec2(-blockSize * 1.5f, -blockSize * 0.5f));
        vertices2.add(new Vec2(blockSize * 1.5f, -blockSize * 0.5f));
        vertices2.add(new Vec2(blockSize * 1.5f, blockSize * 0.5f));
        vertices2.add(new Vec2(-blockSize * 1.5f, blockSize * 0.5f));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * 1.5f, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * 1.5f, -blockSize * 0.5f, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * 0.5f, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * 0.5f, -blockSize * 0.5f, blockSize, blockSize);
        recs[4] = new Rectangle2D.Float(-blockSize * 0.5f, blockSize * 0.5f, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    public BuildingBlock createPyramidBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize * 1.5f, -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * 1.5f, -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * 1.5f, blockSize * (1f / 4f)));
        vertices1.add(new Vec2(-blockSize * 1.5f, blockSize * (1f / 4f)));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(-blockSize * 0.5f, -blockSize * (3f / 4f)));
        vertices2.add(new Vec2(blockSize * 0.5f, -blockSize * (3f / 4f)));
        vertices2.add(new Vec2(blockSize * 0.5f, blockSize * (5f / 4f)));
        vertices2.add(new Vec2(-blockSize * 0.5f, blockSize * (5f / 4f)));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * (5f / 4f), blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * 1.5f, -blockSize * (1f / 4f), blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * (1f / 4f), blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * 0.5f, -blockSize * (1f / 4f), blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    // Right-L
    public BuildingBlock createRightLBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize * (7f / 4f), -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4f), -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4f), blockSize * (1f / 4f)));
        vertices1.add(new Vec2(-blockSize * (7f / 4f), blockSize * (1f / 4f)));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(blockSize * (1f / 4f), -blockSize * (3f / 4f)));
        vertices2.add(new Vec2(blockSize * (5f / 4f), -blockSize * (3f / 4f)));
        vertices2.add(new Vec2(blockSize * (5f / 4f), blockSize * (5f / 4f)));
        vertices2.add(new Vec2(blockSize * (1f / 4f), blockSize * (5f / 4f)));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(blockSize * (1f / 4f), -blockSize * (5f / 4f), blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * (7f / 4f), -blockSize * (1f / 4f), blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * (3f / 4f), -blockSize * (1f / 4f), blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * (1f / 4f), -blockSize * (1f / 4f), blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    // Left-L
    public BuildingBlock createLeftLBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize * (7f / 4), -blockSize * (1f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4), -blockSize * (1f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4), blockSize * (3f / 4f)));
        vertices1.add(new Vec2(-blockSize * (7f / 4), blockSize * (3f / 4f)));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(blockSize * (1f / 4f), -blockSize * (5f / 4f)));
        vertices2.add(new Vec2(blockSize * (5f / 4f), -blockSize * (5f / 4f)));
        vertices2.add(new Vec2(blockSize * (5f / 4f), blockSize * (3f / 4f)));
        vertices2.add(new Vec2(blockSize * (1f / 4f), blockSize * (3f / 4f)));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(blockSize * (1f / 4f), -blockSize * (1f / 4f), blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * (7f / 4f), blockSize * (3f / 4f), blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * (3f / 4f), blockSize * (3f / 4f), blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * (1f / 4f), blockSize * (3f / 4f), blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    public BuildingBlock createRightSBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize * 1.5f, -blockSize));
        vertices1.add(new Vec2(blockSize * 0.5f, -blockSize));
        vertices1.add(new Vec2(blockSize * 0.5f, 0));
        vertices1.add(new Vec2(-blockSize * 1.5f, 0));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(-blockSize * 0.5f, 0));
        vertices2.add(new Vec2(blockSize * 1.5f, 0));
        vertices2.add(new Vec2(blockSize * 1.5f, blockSize));
        vertices2.add(new Vec2(-blockSize * 0.5f, blockSize));

        //Not sure this solves the problem with intersecting and interlocking pieces...
        List<Vec2> vertices3 = new ArrayList<Vec2>(4);
        vertices3.add(new Vec2(-blockSize * 0.5f, -blockSize));
        vertices3.add(new Vec2(blockSize * 0.5f, -blockSize));
        vertices3.add(new Vec2(blockSize * 0.5f, blockSize));
        vertices3.add(new Vec2(-blockSize * 0.5f, blockSize));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);
        addShape(vertices3, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(blockSize * 0.5f, -blockSize, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 1.5f, 0, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(-blockSize * 0.5f, 0, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
    }

    public BuildingBlock createLeftSBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        vertices1.add(new Vec2(-blockSize * 1.5f, 0));
        vertices1.add(new Vec2(blockSize * 0.5f, 0));
        vertices1.add(new Vec2(blockSize * 0.5f, blockSize));
        vertices1.add(new Vec2(-blockSize * 1.5f, blockSize));

        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        vertices2.add(new Vec2(-blockSize * 0.5f, -blockSize));
        vertices2.add(new Vec2(blockSize * 1.5f, -blockSize));
        vertices2.add(new Vec2(blockSize * 1.5f, 0));
        vertices2.add(new Vec2(-blockSize * 0.5f, 0));

        //Not sure this solves the problem with intersecting and interlocking pieces...
        List<Vec2> vertices3 = new ArrayList<Vec2>(4);
        vertices3.add(new Vec2(-blockSize * 0.5f, -blockSize));
        vertices3.add(new Vec2(blockSize * 0.5f, -blockSize));
        vertices3.add(new Vec2(blockSize * 0.5f, blockSize));
        vertices3.add(new Vec2(-blockSize * 0.5f, blockSize));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);
        addShape(vertices3, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(-blockSize * 1.5f, blockSize, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * 0.5f, blockSize, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 0.5f, 0, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * 0.5f, 0, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs);
        buildingBlockPool.add(block);

        return block;
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

    }
}
