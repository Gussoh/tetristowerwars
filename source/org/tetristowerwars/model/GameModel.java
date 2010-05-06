/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BoundaryListener;
import org.jbox2d.dynamics.ContactFilter;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;
import org.tetristowerwars.util.MutableEntry;

/**
 *
 * @author Andreas
 */
public class GameModel {

    //private final LinkedHashSet<Block> blockPool = new LinkedHashSet<Block>();
    // Trying out generic block pool?
    private final LinkedHashSet<BuildingBlock> buildingBlockPool = new LinkedHashSet<BuildingBlock>();
    private final LinkedHashSet<TriggerBlock> triggerBlocks = new LinkedHashSet<TriggerBlock>();
    private final ArrayList<Player> players = new ArrayList<Player>();
    private final World world;
    private static final int ITERATIONS_PER_STEP = 12; // Lower value means less accurate but faster
    private final AABB worldBoundries;
    private final GroundBlock groundBlock;
    private final BuildingBlockFactory blockFactory;
    private final CannonFactory cannonFactory;
    private final BulletFactory bulletFactory;
    private final TriggerBlockFactory triggerBlockFactory;
    private final PowerupFactory powerupFactory;
    private final LinkedHashSet<BuildingBlockJoint> buildingBlockJoints = new LinkedHashSet<BuildingBlockJoint>();
    private final Set<MutableEntry<Block, Integer>> blocksToRemove = new LinkedHashSet<MutableEntry<Block, Integer>>();
    private final Map<BuildingBlock, CannonBlock> blocksOverlappingCannon = new LinkedHashMap<BuildingBlock, CannonBlock>();
    private final Map<PowerupBlock, Block> powerupOverlappingBlock = new LinkedHashMap<PowerupBlock, Block>();
    private float timeTakenToExecuteUpdateMs;
    private final List<GameModelListener> gameModelListeners = new ArrayList<GameModelListener>();
    private final PhysicsEngineListener physicsEngineListener = new PhysicsEngineListener();
    private final float groundLevel;
    private final float blockSize;
    private boolean winningNotificationTriggered = false;
    private WinningCondition winningCondition = null;
    private Player leader = null;
    private final Set<MutableEntry<Block, Integer>> blocksToModify = new LinkedHashSet<MutableEntry<Block, Integer>>();
    private boolean bulletTimerValid;
    private long bulletTimer;
    private boolean gameOver;
    private final static long GAME_OVER_TIMER = 3000;

    /**
     * Creates a new GameModel, the model for the game world. The game world uses the meters/seconds/kilograms units.
     * In the game world coordinate space, 1 unit equals 1 meter.
     *
     * @param worldWidth The width of the world in meters.
     * @param worldHeight The height of the world in meters.
     * @param groundLevel The height of the ground in meters.
     * @param blockSize The block side length in meters.
     */
    public GameModel(float worldWidth, float worldHeight, float groundLevel, float blockSize) {


        this.blockSize = blockSize;
        worldBoundries = new AABB(new Vec2(0, 0), new Vec2(worldWidth, worldHeight));
        Vec2 gravity = new Vec2(0, -9.82f);
        world = new World(worldBoundries, gravity, true);
        this.groundLevel = groundLevel;

        BodyDef groundDef = new BodyDef();

        groundDef.position.set(worldWidth / 2, groundLevel - worldHeight / 2);
        groundBlock = new GroundBlock(world.createBody(groundDef));

        PolygonDef groundShapeDef = new PolygonDef();
        groundShapeDef.setAsBox(worldWidth / 2, worldHeight / 2);
        groundShapeDef.friction = 0.8f;
        groundBlock.getBody().createShape(groundShapeDef);

        blockFactory = new BuildingBlockFactory(buildingBlockPool, this, blockSize);
        cannonFactory = new CannonFactory(this, blockSize);
        bulletFactory = new BulletFactory(this, blockSize);
        triggerBlockFactory = new TriggerBlockFactory(this);
        powerupFactory = new PowerupFactory(this);
        world.setBoundaryListener(physicsEngineListener);
        world.setContactListener(physicsEngineListener);
        world.setContactFilter(physicsEngineListener);
    }

    public void reset() {
        int i = 0;
        System.out.println("pool: " + buildingBlockPool.size());
        for (BuildingBlock buildingBlock : buildingBlockPool) {
            blocksToRemove.add(new MutableEntry<Block, Integer>(buildingBlock, i));
            i += 2;
        }

        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getBuildingBlocks().size());
            for (BuildingBlock buildingBlock : player.getBuildingBlocks()) {
                blocksToRemove.add(new MutableEntry<Block, Integer>(buildingBlock, i));
                i += 2;
            }

            for (BulletBlock bulletBlock : player.getBullets()) {
                blocksToRemove.add(new MutableEntry<Block, Integer>(bulletBlock, i));
                i += 2;
            }
            for (CannonBlock cannonBlock : player.getCannons()) {
                cannonBlock.abort();
            }
        }

        blocksOverlappingCannon.clear();

        winningNotificationTriggered = false;

        if (winningCondition != null) {
            winningCondition.reset();
        }
        gameOver = false;

        for (GameModelListener gameModelListener : gameModelListeners) {
            gameModelListener.onGameReset();
        }
    }

    /**
     * Steps the physics engine and game model by 1/60 seconds.
     */
    public void update() {

        long currentTimeNano = System.nanoTime();

        if (gameOver) {
            if (!winningNotificationTriggered) {
                winningNotificationTriggered = true;
            }
            for (GameModelListener gameModelListener : gameModelListeners) {
                gameModelListener.onWinningConditionFulfilled(winningCondition);
            }
            return;
        }

        if (winningCondition != null && winningCondition.isGameOver()) {

            boolean moreBulletsExist = false;
            for (Player player : players) {
                if (!player.getBullets().isEmpty()) {
                    moreBulletsExist = true;
                    break;
                }
            }

            if (moreBulletsExist) {
                bulletTimerValid = false;
            } else {
                if (!bulletTimerValid) {
                    bulletTimerValid = true;
                    bulletTimer = System.currentTimeMillis() + GAME_OVER_TIMER;
                } else if (System.currentTimeMillis() > bulletTimer) {
                    gameOver = true;
                    bulletTimerValid = false;
                }
            }
        }

        world.step(1f / 60f, ITERATIONS_PER_STEP);

        updateBlocksIntersectingWithCannon();

        for (BuildingBlockJoint buildingBlockJoint : buildingBlockJoints) {
            buildingBlockJoint.dampAngularVelocity();
        }

        // blocksToRemove will now contain all blocks that are outside the world.
        for (Iterator<MutableEntry<Block, Integer>> it = blocksToRemove.iterator(); it.hasNext();) {
            MutableEntry<Block, Integer> mutableEntry = it.next();
            Block block = mutableEntry.getKey();

            int delay = mutableEntry.getValue();
            if (delay > 0) {
                mutableEntry.setValue(delay - 1);
            } else {
                it.remove();
                if (block instanceof BuildingBlock) {
                    BuildingBlock buildingBlock = (BuildingBlock) block;

                    Player owner = buildingBlock.getOwner();
                    if (owner == null) {
                        buildingBlockPool.remove(buildingBlock);
                    } else {
                        owner.removeBuildingBlock(buildingBlock);

                    }

                    BuildingBlockJoint bbj;
                    while ((bbj = getAttachedJoint(buildingBlock)) != null) {
                        removeBuldingBlockJoint(bbj);
                    }
                    for (GameModelListener gameModelListener : gameModelListeners) {
                        gameModelListener.onBlockDestruction(buildingBlock);
                    }
                    blocksOverlappingCannon.remove(buildingBlock);
                    buildingBlock.destroyBody(world);


                } else if (block instanceof BulletBlock) {
                    BulletBlock bulletBlock = (BulletBlock) block;
                    bulletBlock.getOwner().removeBullet(bulletBlock);
                    for (GameModelListener gameModelListener : gameModelListeners) {
                        gameModelListener.onBlockDestruction(bulletBlock);
                    }
                    bulletBlock.destroyBody(world);
                } else {
                    System.out.println("WARNING: Block of type " + block.getClass().getName() + " not removed when outside of world.");
                }
            }
        }

        for (Player player : players) {

            for (CannonBlock cannonBlock : player.getCannons()) {
                cannonBlock.update(1f / 60f);
            }

            // Check if non-owned blocks has passed into a player area and should be
            // owned by a player or if it should be destroyed.
            for (Iterator<BuildingBlock> it = buildingBlockPool.iterator(); it.hasNext();) {
                BuildingBlock buildingBlock = it.next();

                float blockX = buildingBlock.getBody().getPosition().x;

                // Is the non-owned block inside a player area?
                if (blockX >= player.getLeftLimit() + 0.5f && blockX <= player.getRightLimit() - 0.5f) {
                    it.remove();

                    if (getAttachedJoint(buildingBlock) != null) {
                        player.addBuildingBlock(buildingBlock);
                        for (GameModelListener gameModelListener : gameModelListeners) {
                            gameModelListener.onBuildingBlockOwnerChanged(buildingBlock);
                        }
                    } else {
                        for (GameModelListener gameModelListener : gameModelListeners) {
                            gameModelListener.onBlockDestruction(buildingBlock);
                        }
                        buildingBlock.destroyBody(world);
                    }
                }
            }

            /**
             * Check if a player owned block has fallen outside the player area, if so delete it!
             * 
             * Need to save what blocks to delete since deleting while iterating would result in a
             * concurrent modification exception.
             */
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
                blocksOverlappingCannon.remove(buildingBlock);
                for (GameModelListener gameModelListener : gameModelListeners) {
                    gameModelListener.onBlockDestruction(buildingBlock);
                }
                buildingBlock.destroyBody(world);
            }
        }

        //Modify any block (right now only used by BulletBlocks)
        for (Iterator<MutableEntry<Block, Integer>> it = blocksToModify.iterator(); it.hasNext();) {
            MutableEntry<Block, Integer> mutableEntry = it.next();
            Block block = mutableEntry.getKey();

            int delay = mutableEntry.getValue();
            if (delay > 0) {
                mutableEntry.setValue(delay - 1);
            } else {
                it.remove();
                block.getBody().getShapeList().m_isSensor = false;
            }
        }


        if (winningCondition != null) {
            if (winningCondition.getLeader().getPlayer() != leader) {
                leader = winningCondition.getLeader().getPlayer();
                for (GameModelListener gameModelListener : gameModelListeners) {
                    gameModelListener.onLeaderChanged(leader);
                }
            }
        }

        for (Player player : players) {
            boolean hilightHighestBlock = (player == leader && winningCondition.timeLeftUntilGameOver() != -1);
            player.calcHighestBuildingBlockInTower(groundBlock, hilightHighestBlock, groundLevel);
        }

        timeTakenToExecuteUpdateMs = (float) (System.nanoTime() - currentTimeNano) / 1000000f;
    }

    /**
     * Return the ground block.
     * @return the ground block.
     */
    public GroundBlock getGroundBlock() {
        return groundBlock;
    }

    /**
     * The building block pool contains all non-owned building blocks.
     * @return An unmodifiable version of the buildingBlockPool.
     */
    public Set<BuildingBlock> getBuildingBlockPool() {
        return Collections.unmodifiableSet(buildingBlockPool);
    }

    public Set<TriggerBlock> getTriggerBlocks() {
        return Collections.unmodifiableSet(triggerBlocks);
    }

    public float getBlockSize() {
        return blockSize;
    }

    /**
     * Returns the first (and hopefully only) block from the given input
     * coordinates.
     *
     * @param position the position in world coordinates.
     * @return One block found at those coordinates or null if nothing was found.
     */
    public Block getBlockFromCoordinates(Vec2 position) {
        float x = position.x;
        float y = position.y;
        Shape[] shapes = world.query(new AABB(new Vec2(x - 0.1f, y - 0.1f), new Vec2(x + 0.1f, y + 0.1f)), 3);

        if (shapes != null && shapes.length > 0) {
            for (int i = 0; i < shapes.length && shapes[i] != null; i++) {
                Shape shape = shapes[i];

                if (shape.testPoint(shape.getBody().getXForm(), new Vec2(x, y))) {
                    Block b = (Block) shape.getBody().getUserData();
                    if (b instanceof BuildingBlock) {
                        return b;
                    }
                    if ((b instanceof TriggerBlock && ((TriggerBlock) b).isVisible())) {
                        return b;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Creates a new building block joint for a given building block and at a given anchor position.
     * The joint is "rubber band" like.
     * Several joints can be connected to the same block at the same time.
     *
     * @param buildingBlock The building block to attach to.
     * @param position The anchor point in world coordinates.
     * @return The new joint.
     */
    public BuildingBlockJoint createBuildingBlockJoint(BuildingBlock buildingBlock, Vec2 position) {
        BuildingBlockJoint bbj = new BuildingBlockJoint(world, buildingBlock, position);
        buildingBlockJoints.add(bbj);
        for (GameModelListener gameModelListener : gameModelListeners) {
            gameModelListener.onJointCreation(bbj);
        }

        if (buildingBlock instanceof PowerupBlock) {
            Player owner = buildingBlock.getOwner();
            if (owner != null) {
                for (BuildingBlock bb : owner.getBuildingBlocks()) {
                    if (bb.isUpgradable()) {
                        bb.setPowerupHilighted(true);
                    }
                }

                for (CannonBlock cannonBlock : owner.getCannons()) {
                    if (cannonBlock.isUpgradable()) {
                        cannonBlock.setPowerupHilighted(true);
                    }
                }
            }
        }

        return bbj;
    }

    /**
     * Moves the end point of an existing building block joint to a new position.
     * Note that if the joint has been removed by the game model due to some game related event,
     * this method does nothing.
     * 
     * @param buildingBlockJoint the joint to update.
     * @param endPosition The new end position in world coordinates.
     */
    public void moveBuildingBlockJoint(BuildingBlockJoint buildingBlockJoint, Vec2 endPosition) {
        if (buildingBlockJoints.contains(buildingBlockJoint)) {
            buildingBlockJoint.updatePointerPosition(endPosition);
        }
    }

    /**
     * Removes a joint from the game world.<p>
     * If the BuildingBlock attached to the joint is overlapping a
     * CannonBlock when the joint is removed, an attempt to load the cannon is made.
     * @param buildingBlockJoint the joint to remove
     */
    public void removeBuldingBlockJoint(BuildingBlockJoint buildingBlockJoint) {
        if (buildingBlockJoints.remove(buildingBlockJoint)) {
            for (GameModelListener gameModelListener : gameModelListeners) {
                gameModelListener.onJointDestruction(buildingBlockJoint);
            }
            BuildingBlock bb = buildingBlockJoint.getBuildingBlock();
            XForm xForm = bb.getBody().getXForm();
            boolean isLastJointForThisBlock = getAttachedJoint(bb) == null;
            buildingBlockJoint.destroy(isLastJointForThisBlock);


            if (bb instanceof PowerupBlock) {
                Player owner = bb.getOwner();
                if (owner != null) {
                    for (BuildingBlock buildingBlock : owner.getBuildingBlocks()) {
                        buildingBlock.setPowerupHilighted(false);
                    }
                    for (CannonBlock cannonBlock : owner.getCannons()) {
                        cannonBlock.setPowerupHilighted(false);
                    }
                }

                Block overlappedBlock = powerupOverlappingBlock.remove((PowerupBlock) bb);

                if (overlappedBlock != null) {
                    if (overlappedBlock instanceof Upgradable) {
                        Upgradable upgradable = (Upgradable) overlappedBlock;
                        if (upgradable.isUpgradable()) {
                            upgradable.upgrade();
                            blocksToRemove.add(new MutableEntry<Block, Integer>(bb, 0));
                        }
                    }
                }
            } else if (isLastJointForThisBlock) {

                //Cannon-loading code
                CannonBlock cannon = blocksOverlappingCannon.get(bb);
                if (cannon != null && !cannon.isArmed()) {
                    blocksToRemove.add(new MutableEntry<Block, Integer>(bb, 0));
                    cannon.shoot(bb.getMaterial());
                }
            }


        }
    }

    private void updateBlocksIntersectingWithCannon() {

        for (Player player : players) {
            for (CannonBlock cannonBlock : player.getCannons()) {
                cannonBlock.setHilighted(false);
            }
        }

        for (Iterator<Map.Entry<BuildingBlock, CannonBlock>> it = blocksOverlappingCannon.entrySet().iterator(); it.hasNext();) {
            Map.Entry<BuildingBlock, CannonBlock> entry = it.next();
            BuildingBlock bb = entry.getKey();
            CannonBlock cannon = entry.getValue();
            XForm xForm = bb.getBody().getXForm();

            if (bb.isDestroyed()) {
                it.remove();
                continue;
            }

            boolean found = false;
            for (Shape s = bb.getBody().getShapeList(); s != null; s = s.m_next) {

                AABB aabb = new AABB();
                s.computeAABB(aabb, xForm);
                Shape foundShapes[] = world.query(aabb, 10);


                for (Shape shape : foundShapes) {
                    Object userData = shape.getBody().getUserData();
                    if (userData == cannon) {

                        found = true;
                        if (cannon.isArmed()) {
                            cannon.setHilighted(false);
                        } else {
                            cannon.setHilighted(true);
                        }
                        break;
                    }
                }
            }

            if (!found) {
                it.remove();
            }
        }
    }

    public void updatePowerupIntersections() {



        for (Iterator<Map.Entry<PowerupBlock, Block>> it = powerupOverlappingBlock.entrySet().iterator(); it.hasNext();) {
            Map.Entry<PowerupBlock, Block> entry = it.next();
            PowerupBlock powerupBlock = entry.getKey();
            Block block = entry.getValue();
            XForm xForm = powerupBlock.getBody().getXForm();

            boolean found = false;
            for (Shape s = powerupBlock.getBody().getShapeList(); s != null; s = s.m_next) {

                AABB aabb = new AABB();
                s.computeAABB(aabb, xForm);
                Shape foundShapes[] = world.query(aabb, 10);


                for (Shape shape : foundShapes) {
                    Object userData = shape.getBody().getUserData();
                    if (userData == block) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                it.remove();
            }
        }
    }

    /**
     * Return all players created in this game model.
     * @return An unmodifiable list of the players.
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Returns the building block factory which can be used to create new 
     * building blocks in this game world.
     * @return
     */
    public BuildingBlockFactory getBuildingBlockFactory() {
        return blockFactory;
    }

    public TriggerBlockFactory getTriggerBlockFactory() {
        return triggerBlockFactory;
    }

    /**
     * Returns the cannon block factory which can be used to create new
     * cannon blocks in this game world.
     * @return
     */
    public CannonFactory getCannonFactory() {
        return cannonFactory;
    }

    public PowerupFactory getPowerupFactory() {
        return powerupFactory;
    }

    /**
     * Returns the bullet block factory which can be used to create new
     * bullet blocks in this game world.
     * @return
     */
    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }

    /**
     * Returns the last time taken to step the physics engine and run the
     * post processing method. Used for performance analysis.
     * @return
     */
    public float getTimeTakenToExecuteUpdateMs() {
        return timeTakenToExecuteUpdateMs;
    }

    /**
     * The joints in this game world.
     * @return An unmodifiable set of the building block joints.
     */
    public Set<BuildingBlockJoint> getBuildingBlockJoints() {
        return Collections.unmodifiableSet(buildingBlockJoints);
    }

    /**
     * Creates a new player in a given area of the game world.
     *
     * @param name The name of the player.
     * @param playerIndex 
     * @param leftLimit The left limit/border of this player in world coordinates.
     * @param rightLimit The right limit/border of this player in world coordinates.
     * @return The created player object.
     */
    public Player createPlayer(String name, int playerIndex, float leftLimit, float rightLimit) {
        Player player = new Player(name, playerIndex, leftLimit, rightLimit);
        players.add(player);

        return player;
    }

    /**
     * Iterate over all joints to find out if this body is attached to a joint.
     * Note that a body might be attached to several joints. This function only
     * returns the first joint found.
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

    /**
     * Add a listener to receive game model events.
     * @param listener the listener to add.
     */
    public void addGameModelListener(GameModelListener listener) {
        gameModelListeners.add(listener);
    }

    /**
     * Removes a listener.
     * @param listener the listener to remove.
     */
    public void removeGameModelListener(GameModelListener listener) {
        gameModelListeners.remove(listener);
    }

    public AABB getWorldBoundries() {
        return worldBoundries;
    }

    public float getGroundLevel() {
        return groundLevel;
    }

    protected void fireBodyCreationNotification(Block block) {
        if (block instanceof BulletBlock || block instanceof PowerupBlock) {
            blocksToModify.add(new MutableEntry<Block, Integer>(block, 60));
        }
        for (GameModelListener gameModelListener : gameModelListeners) {
            gameModelListener.onBlockCreation(block);
        }
    }

    protected void fireTimeLeftBeat(int timeLestS) {
        for (GameModelListener gameModelListener : gameModelListeners) {
            gameModelListener.onTimerBeat(timeLestS);
        }
    }

    protected World getWorld() {
        return world;
    }

    public void setWinningCondition(WinningCondition condition) {
        winningCondition = condition;
    }

    public Player getLeader() {
        return leader;
    }

    public WinningCondition getWinningCondition() {
        return winningCondition;
    }

    protected void addTriggerBlock(TriggerBlock triggerBlock) {
        triggerBlocks.add(triggerBlock);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private class PhysicsEngineListener implements ContactListener, BoundaryListener, ContactFilter {

        /**
         * Called when a contact point is added.
         * @param point
         */
        @Override
        public void add(ContactPoint point) {
            //throw new UnsupportedOperationException("Not supported yet.");
            Object userData1 = point.shape1.getBody().getUserData();
            Object userData2 = point.shape2.getBody().getUserData();

            float normalSpeed = Math.abs(Vec2.dot(point.normal, point.velocity));
            Vec2 tangent = new Vec2(point.normal.y, -point.normal.x);
            float tangentSpeed = Math.abs(Vec2.dot(tangent, point.velocity));

            if (userData1 instanceof Block && userData2 instanceof Block) {

                if (userData1 instanceof BulletBlock) {
                    if (!shouldBulletCollide((BulletBlock) userData1, (Block) userData2)) {
                        return;
                    }

                    blocksToRemove.add(new MutableEntry<Block, Integer>((Block) userData1, 4));
                    if (userData2 != groundBlock && !(userData2 instanceof CannonBlock)) {
                        blocksToRemove.add(new MutableEntry<Block, Integer>((Block) userData2, 6));
                    }
                }

                if (userData2 instanceof BulletBlock) {
                    if (!shouldBulletCollide((BulletBlock) userData2, (Block) userData1)) {
                        return;
                    }

                    blocksToRemove.add(new MutableEntry<Block, Integer>((Block) userData2, 4));
                    if (userData1 != groundBlock && !(userData1 instanceof CannonBlock)) {
                        blocksToRemove.add(new MutableEntry<Block, Integer>((Block) userData1, 6));
                    }
                }

                for (GameModelListener gameModelListener : gameModelListeners) {
                    gameModelListener.onBlockCollision((Block) userData1, (Block) userData2, normalSpeed, tangentSpeed, point.position);
                }
            }

            if (userData1 instanceof BuildingBlock && userData2 instanceof CannonBlock) {
            } else if (userData1 instanceof CannonBlock && userData2 instanceof BuildingBlock) {
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
                blocksToRemove.add(new MutableEntry<Block, Integer>((Block) userdata, 0));
            }
        }

        private boolean shouldBulletCollide(BulletBlock bullet, Block otherBlock) {
            if (bullet.getCannon() == otherBlock) {
                return false; // dont allow the bullet to hit its own cannon
            } else if (otherBlock.getOwner() == null && otherBlock != groundBlock) {
                return false;
            } else {
                return true;
            }
        }

        private boolean shouldBuildingBlockCollideWithCannon(BuildingBlock bb) {
            BuildingBlockJoint bbj = getAttachedJoint(bb);
            if (bbj != null) {
                return false;
            }

            return true;
        }

        @Override
        public boolean shouldCollide(Shape shape1, Shape shape2) {
            Object userData1 = shape1.getBody().getUserData();
            Object userData2 = shape2.getBody().getUserData();

            if (userData1 instanceof PowerupBlock) {
                if (userData2 instanceof Block) {
                    return shouldPowerUpCollide((PowerupBlock) userData1, (Block) userData2);
                }

            }

            if (userData2 instanceof PowerupBlock) {
                if (userData1 instanceof Block) {
                    return shouldPowerUpCollide((PowerupBlock) userData2, (Block) userData1);
                }
            }

            if (userData1 instanceof TriggerBlock || userData2 instanceof TriggerBlock) {
                return false;
            }

            if (userData1 instanceof BulletBlock && userData2 instanceof Block) {
                return shouldBulletCollide((BulletBlock) userData1, (Block) userData2);
            } else if (userData2 instanceof BulletBlock && userData1 instanceof Block) {
                return shouldBulletCollide((BulletBlock) userData2, (Block) userData1);
            }

            if (userData1 instanceof CannonBlock && userData2 instanceof BuildingBlock) {
                CannonBlock cannon = (CannonBlock) userData1;
                BuildingBlock bb = (BuildingBlock) userData2;
                if (shouldBuildingBlockCollideWithCannon(bb)) {
                    return true;
                } else {
                    blocksOverlappingCannon.put(bb, cannon);
                    return false;
                }
            } else if (userData2 instanceof CannonBlock && userData1 instanceof BuildingBlock) {

                CannonBlock cannon = (CannonBlock) userData1;
                BuildingBlock bb = (BuildingBlock) userData2;
                if (shouldBuildingBlockCollideWithCannon(bb)) {
                    return true;
                } else {
                    blocksOverlappingCannon.put(bb, cannon);
                    return false;
                }
            }

            return true;

        }

        private boolean shouldPowerUpCollide(PowerupBlock powerupBlock, Block block) {
            System.out.println("POWER");

            boolean noCollision = false;
            if (getAttachedJoint(powerupBlock) != null) {
                noCollision = true;
            } else if (block instanceof PowerupBlock) {
                if (getAttachedJoint((BuildingBlock) block) != null) {
                    noCollision = true;
                }
            }
            if (noCollision) {
                if (block instanceof BuildingBlock || block instanceof CannonBlock) {
                    powerupOverlappingBlock.put(powerupBlock, block);
                    return false;
                }
            }
            return true;
        }
    }
}

