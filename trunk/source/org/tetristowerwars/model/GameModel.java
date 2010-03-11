/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.tetristowerwars.model.building.BuildingBlock;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
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
        groundDef.position.set(width / 2, groundLevel - height / 2);
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addToBlockPool(BuildingBlock block) {

        blockPool.add(block);
    }

    public BuildingBlockFactory getBlockFactory() {
        return blockFactory;
    }




    
}
