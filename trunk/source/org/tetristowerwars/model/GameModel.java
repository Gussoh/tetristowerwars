/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Point2D;
import org.tetristowerwars.model.building.BuildingBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BoundaryListener;
import org.jbox2d.dynamics.World;
import org.tetristowerwars.model.building.BuildingBlockFactory;
import org.tetristowerwars.model.cannon.BulletBlock;
import org.tetristowerwars.model.cannon.BulletFactory;
import org.tetristowerwars.model.cannon.CannonBlock;
import org.tetristowerwars.model.cannon.CannonFactory;

/**
 *
 * @author Andreas
 */
public class GameModel implements BoundaryListener {

    //private final LinkedHashSet<Block> blockPool = new LinkedHashSet<Block>();
    // Trying out generic block pool?
    private final LinkedHashSet<BuildingBlock> buildingBlockPool = new LinkedHashSet<BuildingBlock>();
    private final ArrayList<Player> players = new ArrayList<Player>();
    private final World world;
    private static final int ITERATIONS_PER_STEP = 10; // Lower value means less accurate but faster
    private long lastStepTime = System.currentTimeMillis();
    private final float worldWidth, worldHeight;
    private final Body groundBody;
    private final BuildingBlockFactory blockFactory;
    private final CannonFactory cannonFactory;
    private final BulletFactory bulletFactory;
    private final LinkedHashSet<BuildingBlockJoint> buildingBlockJoints = new LinkedHashSet<BuildingBlockJoint>();
    private final float playerAreaWidth;
    private final List<Body> bodiesToRemove = new ArrayList<Body>();

    public GameModel(float worldWidth, float worldHeight, float groundLevel, float blockSize, float playerAreaWidth) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.playerAreaWidth = playerAreaWidth;

        // TODO: How is screen coordinates mapped to physics coordiantes?
        AABB worldBoundries = new AABB(new Vec2(0, 0), new Vec2(worldWidth, worldHeight));
        Vec2 gravity = new Vec2(0, -9.82f);
        world = new World(worldBoundries, gravity, true);

        BodyDef groundDef = new BodyDef();

        groundDef.position.set(worldWidth / 2, groundLevel - worldHeight);
        groundBody = world.createBody(groundDef);

        PolygonDef groundShapeDef = new PolygonDef();
        groundShapeDef.setAsBox(worldWidth, worldHeight);
        groundShapeDef.friction = 0.8f;
        groundBody.createShape(groundShapeDef);

        blockFactory = new BuildingBlockFactory(buildingBlockPool, world, blockSize);
        cannonFactory = new CannonFactory(world, blockSize);
        bulletFactory = new BulletFactory(world, blockSize);
        world.setBoundaryListener(this);
    }

    public void update() {
        long currentTimeMs = System.currentTimeMillis();

        float stepTimeMs = currentTimeMs - lastStepTime;
        lastStepTime = currentTimeMs;

        for (BuildingBlockJoint buildingBlockJoint : buildingBlockJoints) {
            buildingBlockJoint.dampAngularVelocity();
        }

        world.step(stepTimeMs / 1000, ITERATIONS_PER_STEP);

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();
    }

    public Body getGroundBody() {
        return groundBody;
    }

    public Set<BuildingBlock> getBlockPool() {
        return Collections.unmodifiableSet(buildingBlockPool);
    }

    /**
     * Returns the first (and hopefully only) block from the given input
     * coordinates.
     *
     * @param position
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
        buildingBlockJoint.updatePointerPosition(new Vec2((float) endPosition.getX(), (float) endPosition.getY()));
    }

    public void removeBuldingBlockJoint(BuildingBlockJoint buildingBlockJoint) {
        buildingBlockJoints.remove(buildingBlockJoint);
        buildingBlockJoint.destroy();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }
    
    public BuildingBlockFactory getBlockFactory() {
        return blockFactory;
    }

    public CannonFactory getCannonFactory() {
        return cannonFactory;
    }

    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }

    public World getWorld() {
        return world;
    }

    public Set<BuildingBlockJoint> getBuildingBlockJoints() {
        return Collections.unmodifiableSet(buildingBlockJoints);
    }

    /*
     * Called by physics engine when a body leaves the world boundaries.
     */
    @Override
    public void violation(Body bodyOutsideWorld) {
        Object userdata = bodyOutsideWorld.getUserData();

        if (userdata instanceof BuildingBlock) {
            BuildingBlock buildingBlock = (BuildingBlock) userdata;
            buildingBlockPool.remove(buildingBlock);
            for (Player player : players) {
                player.getBuildingBlocks().remove(buildingBlock);
            }

            for (Body b : buildingBlock.getBodies()) {
                bodiesToRemove.add(b);
            }
        } else if (userdata instanceof BulletBlock) {
            BulletBlock bulletBlock = (BulletBlock) userdata;
            bulletBlock.getOwner().removeBullet(bulletBlock);
            for (Body b : bulletBlock.getBodies()) {
                bodiesToRemove.add(b);
            }
        }
    }

    public Player createPlayer(String name) {
        Player player = new Player(name);
        players.add(player);

        return player;
    }
}
