/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Point2D;
import org.tetristowerwars.model.building.BuildingBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
    private long lastStepTimeNano = System.nanoTime();
    private final float worldWidth, worldHeight;
    private final Body groundBody;
    private final BuildingBlockFactory blockFactory;
    private final CannonFactory cannonFactory;
    private final BulletFactory bulletFactory;
    private final LinkedHashSet<BuildingBlockJoint> buildingBlockJoints = new LinkedHashSet<BuildingBlockJoint>();
    private final float playerAreaWidth;
    private final List<Block> blocksToRemove = new ArrayList<Block>();
    private float timeTakenToExecuteUpdateMs;
    private final float constantStepTimeS = 1f / 60f;

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

    public int update() {
        long currentTimeNano = System.nanoTime();
        long stepTimeNano = currentTimeNano - lastStepTimeNano;
        int numTimesStepped = 0;

        if (stepTimeNano < 0) { // in case of nanoTime wrapping around
            stepTimeNano = (long) (constantStepTimeS * 1000000000.0f);
        }

        while (stepTimeNano > (long) (constantStepTimeS * 1000000000.0f) && numTimesStepped < 2) {
            for (BuildingBlockJoint buildingBlockJoint : buildingBlockJoints) {
                buildingBlockJoint.dampAngularVelocity();
            }
            world.step(constantStepTimeS, ITERATIONS_PER_STEP);
            numTimesStepped++;
            stepTimeNano -= (long) (constantStepTimeS * 1000000000.0f);
        }

        lastStepTimeNano = currentTimeNano - stepTimeNano; // Save remaining step time

        if (numTimesStepped > 0) {
            postProcess();
            timeTakenToExecuteUpdateMs = (float) (System.nanoTime() - currentTimeNano) / 1000000f;
        }

        return numTimesStepped;
    }

    private void postProcess() {

        // blocksToRemove will now contain all blocks that are outside the world.
        for (Block block : blocksToRemove) {
            if (block instanceof BuildingBlock) {
                BuildingBlock buildingBlock = (BuildingBlock) block;
                if (!buildingBlockPool.remove(buildingBlock)) {
                    for (Player player : players) {
                        if (player.removeBuildingBlock(buildingBlock)) {
                            break;
                        }
                    }
                }

                BuildingBlockJoint bbj;
                while ((bbj = getAttachedJoint(buildingBlock)) != null) {
                    removeBuldingBlockJoint(bbj);
                }

                for (Body b : buildingBlock.getBodies()) {
                    world.destroyBody(b);
                }

            } else if (block instanceof BulletBlock) {
                BulletBlock bulletBlock = (BulletBlock) block;
                bulletBlock.getOwner().removeBullet(bulletBlock);
                for (Body b : bulletBlock.getBodies()) {
                    world.destroyBody(b);
                }
            } else {
                System.out.println("WARNING: Block of type " + block.getClass().getName() + " not removed when outside of world.");
            }
        }
        blocksToRemove.clear();

        // Check if non-owned blocks should be owned by a player or perhaps destroyed.
        // A copy is needed since we need to modify the block pool while iterating over it.


        for (Player player : players) {

            for (Iterator<BuildingBlock> it = buildingBlockPool.iterator(); it.hasNext();) {
                BuildingBlock buildingBlock = it.next();
                boolean destroyBodies = false;

                for (Body body : buildingBlock.getBodies()) {
                    float blockX = body.getPosition().x;

                    if (blockX >= player.getLeftLimit() + 2 && blockX <= player.getRightLimit() - 2) {
                        it.remove();
                        if (getAttachedJoint(buildingBlock) != null) {
                            player.addBuildingBlock(buildingBlock);
                        } else {
                            destroyBodies = true;
                        }
                    }
                }

                if (destroyBodies) {
                    for (Body body : buildingBlock.getBodies()) {
                        world.destroyBody(body);
                    }
                }
            }

            List<BuildingBlock> playerBlocksToRemove = new LinkedList<BuildingBlock>();

            for (BuildingBlock buildingBlock : player.getBuildingBlocks()) {

                boolean destroyBodies = false;

                for (Body body : buildingBlock.getBodies()) {
                    float blockX = body.getPosition().x;

                    if (blockX <= player.getLeftLimit() - 2 || blockX >= player.getRightLimit() + 2) {
                        destroyBodies = true;
                    }
                }

                if (destroyBodies) {
                    BuildingBlockJoint blockJoint;

                    while ((blockJoint = getAttachedJoint(buildingBlock)) != null) {
                        removeBuldingBlockJoint(blockJoint);
                    }

                    playerBlocksToRemove.add(buildingBlock);
                }
            }

            for (BuildingBlock buildingBlock : playerBlocksToRemove) {
                player.removeBuildingBlock(buildingBlock);
                for (Body body : buildingBlock.getBodies()) {
                    world.destroyBody(body);
                }
            }
        }

        for (Player player : players) {
            player.calcTowerHeight(groundBody);
        }
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
        if (buildingBlockJoints.contains(buildingBlockJoint)) {
            buildingBlockJoint.updatePointerPosition(new Vec2((float) endPosition.getX(), (float) endPosition.getY()));
        }
    }

    public void removeBuldingBlockJoint(BuildingBlockJoint buildingBlockJoint) {
        if (buildingBlockJoints.remove(buildingBlockJoint)) {
            buildingBlockJoint.destroy();
        }
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

    public float getTimeTakenToExecuteUpdateMs() {
        return timeTakenToExecuteUpdateMs;
    }

    public Set<BuildingBlockJoint> getBuildingBlockJoints() {
        return Collections.unmodifiableSet(buildingBlockJoints);
    }

    /*
     * Called by physics engine when a body leaves the world boundries.
     */
    @Override
    public void violation(Body bodyOutsideWorld) {
        Object userdata = bodyOutsideWorld.getUserData();

        if (userdata instanceof Block) {
            blocksToRemove.add((Block) userdata);
        }
    }

    public Player createPlayer(String name, float leftLimit, float rightLimit) {
        Player player = new Player(name, leftLimit, rightLimit);
        players.add(player);

        return player;
    }

    /**
     * Iterate over all joints to find out if this body is attached to a joint.
     * This is ok
     *
     * @param buildingBlock
     * @return
     */
    public BuildingBlockJoint getAttachedJoint(BuildingBlock buildingBlock) {
        for (BuildingBlockJoint buildingBlockJoint : buildingBlockJoints) {
            if (buildingBlockJoint.getBuildingBlock() == buildingBlock) {
                return buildingBlockJoint;
            }
        }

        return null;
    }
}

