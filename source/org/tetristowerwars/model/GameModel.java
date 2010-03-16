/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

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
import org.tetristowerwars.model.building.BuildingBlockFactory;
import org.tetristowerwars.model.material.SteelMaterial;

/**
 *
 * @author Andreas
 */
public class GameModel {

    //private final LinkedHashSet<Block> blockPool = new LinkedHashSet<Block>();
    // Trying out generic block pool?
    private final LinkedHashSet<BuildingBlock> blockPool = new LinkedHashSet<BuildingBlock>();
    private final LinkedHashSet<CannonBlock> cannonBlockPool = new LinkedHashSet<CannonBlock>();
    private final LinkedHashSet<BulletBlock> bulletBlockPool = new LinkedHashSet<BulletBlock>();
    private final ArrayList<Player> players = new ArrayList<Player>();
    private final World world;
    private static final int ITERATIONS_PER_STEP = 10; // Lower value means less accurate but faster
    private long lastStepTime = System.currentTimeMillis();
    private final float width, height;
    private final Body groundBody;
    private final BuildingBlockFactory blockFactory;
    private final CannonBlockFactory cannonBlockFactory;
    private final BulletBlockFactory bulletBlockFactory;
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
        groundShapeDef.friction = 0.8f;
        groundBody.createShape(groundShapeDef);

        blockFactory = new BuildingBlockFactory(world, blockSize);
        cannonBlockFactory = new CannonBlockFactory(world, blockSize);
        bulletBlockFactory = new BulletBlockFactory(world, blockSize);
    }

    public void update() {
        long currentTimeMs = System.currentTimeMillis();

        float stepTimeMs = currentTimeMs - lastStepTime;
        lastStepTime = currentTimeMs;

        for (BuildingBlockJoint buildingBlockJoint : buildingBlockJoints) {
            buildingBlockJoint.dampAngularVelocity();
        }

        world.step(stepTimeMs / 1000, ITERATIONS_PER_STEP);
    }

    public Body getGroundBody() {
        return groundBody;
    }

    public LinkedHashSet<BuildingBlock> getBlockPool() {
        return blockPool;
    }

    public LinkedHashSet<CannonBlock> getCannonBlockPool() {
        return cannonBlockPool;
    }

    public LinkedHashSet<BulletBlock> getBulletBlockPool() {
        return bulletBlockPool;
    }

    /**
     * Returns the first (and hopefully only) block from the given input
     * coordinates.
     *
     * @param position
     * @param		x		The x coordinate for the input device.
     * @param		y		The y coordinate for the input device.
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

    /**
     * Creates a new cannon block to the world and applies a force to it.
     *
     * @return boolean          True if a new cannon block was created.
     *
     * TODO: Limit cannon blocks to X for each player. PlayerId NYI?
     */

    public boolean createCannonBlock(int actionId, CannonBlock cannonBlock) {
        // TODO: Limit cannon blocks, check here

        BulletBlock bb = getBulletBlockFactory().createBullet(cannonBlock);

        // Apply a force to the center of the bullet body...hoepfully?
        bb.getBodies()[0].applyForce(new Vec2(15.0f, 15.0f), bb.getBodies()[0].getPosition());

        addToBulletBlockPool(bb);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addToBlockPool(BuildingBlock block) {
        blockPool.add(block);
    }

    public void addToBulletBlockPool(BulletBlock block) {
        bulletBlockPool.add(block);
    }

    public void addToCannonBlockPool(CannonBlock block) {
        cannonBlockPool.add(block);
    }

    public BuildingBlockFactory getBlockFactory() {
        return blockFactory;
    }

    public CannonBlockFactory getCannonBlockFactory() {
        return cannonBlockFactory;
    }

    public BulletBlockFactory getBulletBlockFactory() {
        return bulletBlockFactory;
    }

    public World getWorld() {
        return world;
    }

    public LinkedHashSet<BuildingBlockJoint> getBuildingBlockJoints() {
        return buildingBlockJoints;
    }
}
