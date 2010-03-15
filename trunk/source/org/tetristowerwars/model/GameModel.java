/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.Point;
import java.awt.geom.Point2D;
import org.tetristowerwars.model.building.BuildingBlock;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.tetristowerwars.model.building.BuildingBlockFactory;

/**
 *
 * @author Andreas
 */
public class GameModel {

    private final LinkedHashSet<BuildingBlock> blockPool = new LinkedHashSet<BuildingBlock>();
    private final ArrayList<Player> players = new ArrayList<Player>();
    private final World world;
    private static final int ITERATIONS_PER_STEP = 10; // Lower value means less accurate but faster
    private long lastStepTime = System.currentTimeMillis();
    private final float width, height;
    private final Body groundBody;
    private final BuildingBlockFactory blockFactory;
    private final LinkedHashSet<BuildingBlockJoint> buildingBlockJoints = new LinkedHashSet<BuildingBlockJoint>();

    public GameModel(float width, float height, float groundLevel, float blockSize) {
        this.width = width;
        this.height = height;

        // TODO: How is screen coordinates mapped to physics coordiantes?
        AABB worldBoundries = new AABB(new Vec2(0, 0), new Vec2(width, height));
        Vec2 gravity = new Vec2(0, -9.82f);
        world = new World(worldBoundries, gravity, true);

        BodyDef groundDef = new BodyDef();
        groundDef.position.set(width / 2, groundLevel - height);
        groundBody = world.createBody(groundDef);

        PolygonDef groundShapeDef = new PolygonDef();
        groundShapeDef.setAsBox(width, height);
        groundBody.createShape(groundShapeDef);

        blockFactory = new BuildingBlockFactory(world, blockSize);
    }

    public void update() {
        long currentTimeMs = System.currentTimeMillis();

        float stepTimeMs = currentTimeMs - lastStepTime;
        lastStepTime = currentTimeMs;
        world.step(stepTimeMs / 1000, ITERATIONS_PER_STEP);
    }

    public Body getGroundBody() {
        return groundBody;
    }

    public LinkedHashSet<BuildingBlock> getBlockPool() {
        return blockPool;
    }

    /**
     * Returns the first (and hopefully only) block from the given mouse
     * coordinates.
     *
     * @param position
     * @param		x		The x coordinate for the mouse pointer.
     * @param		y		The y coordinate for the mouse pointer.
     * @return BuildingBlock
     */
    public Block getBlockFromCoordinates(Point2D position) {
        float x = (float) position.getX();
        float y = (float) position.getY();

        Shape[] shapes = world.query(new AABB(new Vec2(x - 1, y - 1), new Vec2(x + 1, y + 1)), 1);

        if (shapes != null && shapes.length > 0) {
            return (Block) shapes[0].getBody().getUserData();
        }

        return null;
    }

    public BuildingBlockJoint createBuildingBlockJoint(BuildingBlock buildingBlock, Point2D position) {
        BuildingBlockJoint bbj = new BuildingBlockJoint(world, buildingBlock, new Vec2((float) position.getX(), (float) position.getY()));
        buildingBlockJoints.add(bbj);
        return bbj;
    }

    public void moveBuildingBlockJoint(BuildingBlockJoint buildingBlockJoint, Point2D endPosition) {
        buildingBlockJoint.updatePointerPosition(new Vec2((float)endPosition.getX(), (float)endPosition.getY()));
    }

    public void removeBuldingBlockJoint(BuildingBlockJoint buildingBlockJoint) {
        buildingBlockJoints.remove(buildingBlockJoint);
        buildingBlockJoint.destroy();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addToBlockPool(BuildingBlock block) {

        blockPool.add(block);
    }

    public BuildingBlockFactory getBlockFactory() {
        return blockFactory;
    }

    public World getWorld() {
        return world;
    }

    public LinkedHashSet<BuildingBlockJoint> getBuildingBlockJoints() {
        return buildingBlockJoints;
    }
}
