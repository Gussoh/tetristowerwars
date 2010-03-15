/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.tetristowerwars.model.building.BuildingBlock;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
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
     * @param		x		The x coordinate for the mouse pointer.
     * @param		y		The y coordinate for the mouse pointer.
     * @return BuildingBlock
     */
	public BuildingBlock getBlockFromCoordinates(int x, int y) {
		for (BuildingBlock bb : blockPool) {
			float tempx = x - bb.getBodies()[0].getPosition().x;
			float tempy = y - (500 - bb.getBodies()[0].getPosition().y);

			// System.out.println("Check x: " + tempx + ", y: " + tempy);

			// Check if the coordinates are close to the upper-left corner
            if (tempx > 0.0f && tempx < 10.0f && tempy > 0.0f && tempy < 10.0f)
                return bb;
        }

        return null;
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




    
}
