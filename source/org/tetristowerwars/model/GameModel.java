/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Point2D;
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
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

/**
 *
 * @author Andreas
 */
public class GameModel {

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
    private final List<Block> blocksToRemove = new ArrayList<Block>();
    private float timeTakenToExecuteUpdateMs;
    private final float constantStepTimeS = 1f / 60f;
    private final List<GameModelListener> gameModelListeners = new ArrayList<GameModelListener>();
    private final PhysicsEngineListener physicsEngineListener = new PhysicsEngineListener();

    /**
     * Creates a new GameModel, the model for the game world. The game world uses the meters/seconds/kilograms units.
     * In the game world coordinate space, 1 unit equals 1 meter.
     *
     * @param worldWidth The width of the world in meters.
     * @param worldHeight The height of the world in meters.
     * @param groundLevel The ground level
     * @param blockSize
     */
    public GameModel(float worldWidth, float worldHeight, float groundLevel, float blockSize) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // TODO: How is screen coordinates mapped to physics coordiantes?
        AABB worldBoundries = new AABB(new Vec2(0, 0), new Vec2(worldWidth, worldHeight));
        Vec2 gravity = new Vec2(0, -9.82f);
        world = new World(worldBoundries, gravity, true);

        BodyDef groundDef = new BodyDef();

        groundDef.position.set(worldWidth / 2, groundLevel - worldHeight);
        groundBody = world.createBody(groundDef);

        PolygonDef groundShapeDef = new PolygonDef();
        groundShapeDef.setAsBox(worldWidth / 2, worldHeight);
        groundShapeDef.friction = 0.8f;
        groundBody.createShape(groundShapeDef);

        blockFactory = new BuildingBlockFactory(buildingBlockPool, world, blockSize);
        cannonFactory = new CannonFactory(world, blockSize);
        bulletFactory = new BulletFactory(world, blockSize);
        world.setBoundaryListener(physicsEngineListener);
        world.setContactListener(physicsEngineListener);
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
                world.destroyBody(buildingBlock.getBody());

            } else if (block instanceof BulletBlock) {
                BulletBlock bulletBlock = (BulletBlock) block;
                bulletBlock.getOwner().removeBullet(bulletBlock);
                world.destroyBody(bulletBlock.getBody());
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

                Body body = buildingBlock.getBody();
                float blockX = body.getPosition().x;

                if (blockX >= player.getLeftLimit() + 2 && blockX <= player.getRightLimit() - 2) {
                    it.remove();
                    if (getAttachedJoint(buildingBlock) != null) {
                        player.addBuildingBlock(buildingBlock);
                    } else {
                        world.destroyBody(body);
                    }
                }
            }

            List<BuildingBlock> playerBlocksToRemove = new LinkedList<BuildingBlock>();

            for (BuildingBlock buildingBlock : player.getBuildingBlocks()) {
                float blockX = buildingBlock.getBody().getPosition().x;

                if (blockX <= player.getLeftLimit() - 2 || blockX >= player.getRightLimit() + 2) {
                    BuildingBlockJoint blockJoint;

                    while ((blockJoint = getAttachedJoint(buildingBlock)) != null) {
                        removeBuldingBlockJoint(blockJoint);
                    }
                    playerBlocksToRemove.add(buildingBlock);
                }
            }

            for (BuildingBlock buildingBlock : playerBlocksToRemove) {
                player.removeBuildingBlock(buildingBlock);
                world.destroyBody(buildingBlock.getBody());
            }
        }
        for (Player player : players) {
            player.calcTowerHeight(groundBody);
        }
    }

    public Body getGroundBody() {
        return groundBody;
    }

    public Set<BuildingBlock> getBuildingBlockPool() {
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

    public BuildingBlockFactory getBuildingBlockFactory() {
        return blockFactory;
    }

    public CannonFactory getCannonFactory() {
        return cannonFactory;
    }

    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }

    public float getTimeTakenToExecuteUpdateMs() {
        return timeTakenToExecuteUpdateMs;
    }

    public Set<BuildingBlockJoint> getBuildingBlockJoints() {
        return Collections.unmodifiableSet(buildingBlockJoints);
    }

    /**
     * Creates a new player in a given area of the game world.
     *
     * @param name The name of the player.
     * @param leftLimit The left limit/border of this player in world coordinates.
     * @param rightLimit The right limit/border of this player in world coordinates.
     * @return The created player object.
     */
    public Player createPlayer(String name, float leftLimit, float rightLimit) {
        Player player = new Player(name, leftLimit, rightLimit);
        players.add(player);

        return player;
    }

    /**
     * Iterate over all joints to find out if this body is attached to a joint.
     * Since we almost never have more than 2 joints,
     *
     * @param buildingBlock The block to use in the search.
     * @return The joint this building block is attached to, or null if joint is found.
     */
    public BuildingBlockJoint getAttachedJoint(BuildingBlock buildingBlock) {
        for (BuildingBlockJoint buildingBlockJoint : buildingBlockJoints) {
            if (buildingBlockJoint.getBuildingBlock() == buildingBlock) {
                return buildingBlockJoint;
            }
        }

        return null;
    }

    public void addGameModelListener(GameModelListener listener) {
        gameModelListeners.add(listener);
    }

    public void removeGameModelListener(GameModelListener listener) {
        gameModelListeners.remove(listener);
    }

    private class PhysicsEngineListener implements ContactListener, BoundaryListener {

        /**
         * Called when a contact point is added.
         * @param point
         */
        @Override
        public void add(ContactPoint point) {
            //throw new UnsupportedOperationException("Not supported yet.");
            Object userData1 = point.shape1.getBody().getUserData();
            Object userData2 = point.shape2.getBody().getUserData();

            if (userData1 instanceof Block && userData2 instanceof Block) {
                float velocity = point.velocity.length();
                for (GameModelListener gameModelListener : gameModelListeners) {
                    gameModelListener.onBlockCollision((Block) userData1, (Block) userData2, worldWidth);
                }
            }
        }

        /**
         * Called when a contact point persists
         * @param point
         */
        @Override
        public void persist(ContactPoint point) {
            // Currently not used.
        }

        /**
         * Called when a contact point is removed.
         *
         * @param point
         */
        @Override
        public void remove(ContactPoint point) {
            // Currently not used
        }

        /**
         * Called after a contact point is solved.
         * @param point
         */
        @Override
        public void result(ContactResult point) {
            // Currently not used.
        }

        /*
         * Called by physics engine when a body leaves the world boundries.
         * Bodies cannot be removed here.
         */
        @Override
        public void violation(Body bodyOutsideWorld) {
            Object userdata = bodyOutsideWorld.getUserData();

            if (userdata instanceof Block) {
                blocksToRemove.add((Block) userdata);
            }
        }
    }
}

