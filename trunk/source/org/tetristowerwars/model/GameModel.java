/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

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

/**
 *
 * @author Andreas
 */
public class GameModel {

    private LinkedHashSet<BuildingBlock> blockPool;
    private ArrayList<Player> players;
    private World world;
    private static final int ITERATIONS_PER_STEP = 10; // Lower value means less accurate but faster
    private long lastStepTime = System.currentTimeMillis();
    private final float width, height;
    private Body groundBody;

    private Map<Body, Object> bodies = new ConcurrentHashMap<Body, Object>();

    public GameModel(float width, float height, float groundLevel) {
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
    }

    public void update() {
        long currentTimeMs = System.currentTimeMillis();

        float stepTimeMs = lastStepTime - currentTimeMs;
        lastStepTime = currentTimeMs;
        world.step(1 / 60f, ITERATIONS_PER_STEP);
    }

    public Body getGroundBody() {
        return groundBody;
    }

    // TODO: JUST A TEST, REMOVE LATER
    public void addBody() {
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.allowSleep = true;
        boxBodyDef.position.set(width / 2, height - 10);
        Body boxBody = world.createBody(boxBodyDef);

        PolygonDef boxShape = new PolygonDef();
        boxShape.setAsBox(10, 10);
        boxShape.density = 1.0f;
        boxBody.createShape(boxShape);

        boxBody.setMassFromShapes();
        bodies.put(boxBody, new Object());
    }

    public Set<Body> getBodies() {
        return bodies.keySet();
    }

    
}
