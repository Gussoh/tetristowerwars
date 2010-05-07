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
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Reeen
 */
public class BuildingBlockFactory {

    private final Set<BuildingBlock> buildingBlockPool;
    private final GameModel gameModel;
    private final float blockSize;

    protected BuildingBlockFactory(Set<BuildingBlock> buildingBlockPool, GameModel gameModel, float blockSize) {
        this.buildingBlockPool = buildingBlockPool;
        this.gameModel = gameModel;
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

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, vertices.toArray(new Vec2[0]));
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

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
        gameModel.fireBodyCreationNotification(block);

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
        gameModel.fireBodyCreationNotification(block);

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
        recs[0] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * 2, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 0.5f, 0, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(-blockSize * 0.5f, blockSize, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, vertices.toArray(new Vec2[0]));
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    public BuildingBlock createCrossBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        Vec2[] outline = new Vec2[12];

        outline[0] = new Vec2(-blockSize * 0.5f, -blockSize * 1.5f);
        outline[1] = new Vec2(blockSize * 0.5f, -blockSize * 1.5f);
        outline[2] = new Vec2(blockSize * 0.5f, -blockSize * 0.5f);
        outline[3] = new Vec2(blockSize * 1.5f, -blockSize * 0.5f);
        outline[4] = new Vec2(blockSize * 1.5f, blockSize * 0.5f);
        outline[5] = new Vec2(blockSize * 0.5f, blockSize * 0.5f);
        outline[6] = new Vec2(blockSize * 0.5f, blockSize * 1.5f);
        outline[7] = new Vec2(-blockSize * 0.5f, blockSize * 1.5f);
        outline[8] = new Vec2(-blockSize * 0.5f, blockSize * 0.5f);
        outline[9] = new Vec2(-blockSize * 1.5f, blockSize * 0.5f);
        outline[10] = new Vec2(-blockSize * 1.5f, -blockSize * 0.5f);
        outline[11] = new Vec2(-blockSize * 0.5f, -blockSize * 0.5f);

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

        Rectangle2D[] recs = new Rectangle2D[5];
        recs[0] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * 1.5f, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * 1.5f, -blockSize * 0.5f, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize * 0.5f, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * 0.5f, -blockSize * 0.5f, blockSize, blockSize);
        recs[4] = new Rectangle2D.Float(-blockSize * 0.5f, blockSize * 0.5f, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, outline);
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    public BuildingBlock createPyramidBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        Vec2[] outline = new Vec2[8];

        outline[0] = new Vec2(-blockSize * 1.5f, -blockSize * (3f / 4f));
        outline[1] = new Vec2(blockSize * 1.5f, -blockSize * (3f / 4f));
        outline[2] = new Vec2(blockSize * 1.5f, blockSize * (1f / 4f));
        outline[3] = new Vec2(blockSize * 0.5f, blockSize * (1f / 4f));
        outline[4] = new Vec2(blockSize * 0.5f, blockSize * (5f / 4f));
        outline[5] = new Vec2(-blockSize * 0.5f, blockSize * (5f / 4f));
        outline[6] = new Vec2(-blockSize * 0.5f, blockSize * (1f / 4f));
        outline[7] = new Vec2(-blockSize * 1.5f, blockSize * (1f / 4f));

        vertices1.add(new Vec2(-blockSize * 1.5f, -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * 1.5f, -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * 1.5f, blockSize * (1f / 4f)));
        vertices1.add(new Vec2(-blockSize * 1.5f, blockSize * (1f / 4f)));

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

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, outline);
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    // Right-L
    public BuildingBlock createRightLBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        Vec2[] outline = new Vec2[6];

        outline[0] = new Vec2(-blockSize * (7f / 4f), -blockSize * (3f / 4f));
        outline[1] = new Vec2(blockSize * (5f / 4f), -blockSize * (3f / 4f));
        outline[2] = new Vec2(blockSize * (5f / 4f), blockSize * (5f / 4f));
        outline[3] = new Vec2(blockSize * (1f / 4f), blockSize * (5f / 4f));
        outline[4] = new Vec2(blockSize * (1f / 4f), blockSize * (1f / 4f));
        outline[5] = new Vec2(-blockSize * (7f / 4f), blockSize * (1f / 4f));

        vertices1.add(new Vec2(-blockSize * (7f / 4f), -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4f), -blockSize * (3f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4f), blockSize * (1f / 4f)));
        vertices1.add(new Vec2(-blockSize * (7f / 4f), blockSize * (1f / 4f)));

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

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, outline);
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    // Left-L
    public BuildingBlock createLeftLBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        List<Vec2> vertices1 = new ArrayList<Vec2>(4);
        List<Vec2> vertices2 = new ArrayList<Vec2>(4);
        Vec2[] outline = new Vec2[6];

        outline[0] = new Vec2(-blockSize * (7f / 4f), -blockSize * (1f / 4f));
        outline[1] = new Vec2(blockSize * (1f / 4f), -blockSize * (1f / 4f));
        outline[2] = new Vec2(blockSize * (1f / 4f), -blockSize * (5f / 4f));
        outline[3] = new Vec2(blockSize * (5f / 4f), -blockSize * (5f / 4f));
        outline[4] = new Vec2(blockSize * (5f / 4f), blockSize * (3f / 4f));
        outline[5] = new Vec2(-blockSize * (7f / 4f), blockSize * (3f / 4f));


        vertices1.add(new Vec2(-blockSize * (7f / 4f), -blockSize * (1f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4f), -blockSize * (1f / 4f)));
        vertices1.add(new Vec2(blockSize * (5f / 4f), blockSize * (3f / 4f)));
        vertices1.add(new Vec2(-blockSize * (7f / 4f), blockSize * (3f / 4f)));

        vertices2.add(new Vec2(blockSize * (1f / 4f), -blockSize * (5f / 4f)));
        vertices2.add(new Vec2(blockSize * (5f / 4f), -blockSize * (5f / 4f)));
        vertices2.add(new Vec2(blockSize * (5f / 4f), blockSize * (3f / 4f)));
        vertices2.add(new Vec2(blockSize * (1f / 4f), blockSize * (3f / 4f)));

        addShape(vertices1, mat, body);
        addShape(vertices2, mat, body);

        Rectangle2D[] recs = new Rectangle2D[4];
        recs[0] = new Rectangle2D.Float(blockSize * (1f / 4f), blockSize * (1f / 4f), blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * (7f / 4f), -blockSize * (3f / 4f), blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * (3f / 4f), -blockSize * (3f / 4f), blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * (1f / 4f), -blockSize * (3f / 4f), blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, outline);
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    public BuildingBlock createRightSBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);
        Vec2[] outline = new Vec2[8];

        outline[0] = new Vec2(-blockSize * 1.5f, -blockSize);
        outline[1] = new Vec2(blockSize * 0.5f, -blockSize);
        outline[2] = new Vec2(blockSize * 0.5f, 0);
        outline[3] = new Vec2(blockSize * 1.5f, 0);
        outline[4] = new Vec2(blockSize * 1.5f, blockSize);
        outline[5] = new Vec2(-blockSize * 0.5f, blockSize);
        outline[6] = new Vec2(-blockSize * 0.5f, 0);
        outline[7] = new Vec2(-blockSize * 1.5f, 0);

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

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, outline);
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    public BuildingBlock createLeftSBlock(Vec2 pos, Material mat) {

        Body body = createBody(pos);

        Vec2[] outline = new Vec2[8];
        outline[0] = new Vec2(-blockSize * 1.5f, 0);
        outline[1] = new Vec2(-blockSize * 0.5f, 0);
        outline[2] = new Vec2(-blockSize * 0.5f, -blockSize);
        outline[3] = new Vec2(blockSize * 1.5f, -blockSize);
        outline[4] = new Vec2(blockSize * 1.5f, 0);
        outline[5] = new Vec2(blockSize * 0.5f, 0);
        outline[6] = new Vec2(blockSize * 0.5f, blockSize);
        outline[7] = new Vec2(-blockSize * 1.5f, blockSize);

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
        recs[0] = new Rectangle2D.Float(-blockSize * 1.5f, -blockSize, blockSize, blockSize);
        recs[1] = new Rectangle2D.Float(-blockSize * 0.5f, -blockSize, blockSize, blockSize);
        recs[2] = new Rectangle2D.Float(-blockSize * 0.5f, 0, blockSize, blockSize);
        recs[3] = new Rectangle2D.Float(blockSize * 0.5f, 0, blockSize, blockSize);

        BuildingBlock block = new RectangularBuildingBlock(body, mat, recs, outline);
        buildingBlockPool.add(block);
        gameModel.fireBodyCreationNotification(block);

        return block;
    }

    public BuildingBlock createRandomRectangularBuildingBlock(Vec2 pos, Material mat) {
        double randomValue = Math.random() * 8;

        if (randomValue < 1.0) {
            return createCrossBlock(pos, mat);
        } else if (randomValue < 2.0) {
            return createLeftLBlock(pos, mat);
        } else if (randomValue < 3.0) {
            return createLeftSBlock(pos, mat);
        } else if (randomValue < 4.0) {
            return createLineBlock(pos, mat);
        } else if (randomValue < 5.0) {
            return createPyramidBlock(pos, mat);
        } else if (randomValue < 6.0) {
            return createRightLBlock(pos, mat);
        } else if (randomValue < 7.0) {
            return createRightSBlock(pos, mat);
        } else {
            return createSquareBlock(pos, mat);
        }
    }

    private Body createBody(Vec2 pos) {

        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(pos.x, pos.y);

        return gameModel.getWorld().createBody(boxBodyDef);
    }

    private void addShape(List<Vec2> vertices, Material mat, Body body) {

        PolygonDef shapeDef = new PolygonDef();
        shapeDef.density = mat.getDensity();
        shapeDef.vertices = vertices;
        shapeDef.isSensor = false;
        shapeDef.friction = 0.8f;
        shapeDef.restitution = mat.getRestitution();
        body.createShape(shapeDef);

    }
}
