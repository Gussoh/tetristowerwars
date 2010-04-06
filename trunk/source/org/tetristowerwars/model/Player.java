/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.ContactEdge;

/**
 *
 * @author magnus
 */
public class Player {

    private final LinkedHashSet<BuildingBlock> buildingBlocks = new LinkedHashSet<BuildingBlock>();
    private final LinkedHashSet<CannonBlock> cannons = new LinkedHashSet<CannonBlock>();
    private final LinkedHashSet<BulletBlock> bullets = new LinkedHashSet<BulletBlock>();
    // Bodies owned by player, used for fast look-up in graph contruction.
    private final HashSet<Body> towerBodies = new HashSet<Body>();
    private final String name;
    private final float leftLimit;
    private final float rightLimit;
    private BuildingBlock highestBuilingBlockInTower;

    protected Player(String name, float leftLimit, float rightLimit) {
        this.name = name;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
    }

    public Set<BuildingBlock> getBuildingBlocks() {
        return Collections.unmodifiableSet(buildingBlocks);
    }

    public Set<CannonBlock> getCannons() {
        return Collections.unmodifiableSet(cannons);
    }

    public Set<BulletBlock> getBullets() {
        return Collections.unmodifiableSet(bullets);
    }

    protected void addCannon(CannonBlock cannonBlock) {
        if (cannonBlock.getOwner() != null) {
            throw new IllegalArgumentException("Cannon already owned by player: " + cannonBlock.getOwner());
        }
        cannons.add(cannonBlock);
        cannonBlock.setOwner(this);
    }

    protected boolean removeCannon(CannonBlock cannonBlock) {
        if (cannons.remove(cannonBlock)) {
            cannonBlock.setOwner(null);
            return true;
        }

        return false;
    }

    protected void addBullet(BulletBlock bullet) {
        if (bullet.getOwner() != null) {
            throw new IllegalArgumentException("Bullet already owned by player: " + bullet.getOwner());
        }
        bullets.add(bullet);
        bullet.setOwner(this);
    }

    protected boolean removeBullet(BulletBlock bullet) {
        if (bullets.remove(bullet)) {
            bullet.setOwner(null);
            return true;
        }

        return false;
    }

    protected boolean removeBuildingBlock(BuildingBlock buildingBlock) {
        if (buildingBlocks.remove(buildingBlock)) {
            buildingBlock.setOwner(null);
            towerBodies.remove(buildingBlock.getBody());

            if (buildingBlock == highestBuilingBlockInTower) {
                highestBuilingBlockInTower = null;
            }

            return true;
        }

        return false;
    }

    /**
     * Should only be called from GameModel. Set the owner for a block to this player.
     * The block is also added to the players owned building blocks.
     * @param buildingBlock The building block to change ownership of.
     */
    protected void addBuildingBlock(BuildingBlock buildingBlock) {
        if (buildingBlock.getOwner() != null) {
            throw new IllegalArgumentException("BuildingBlock already owned by player: " + buildingBlock.getOwner());
        }
        buildingBlocks.add(buildingBlock);
        buildingBlock.setOwner(this);

        towerBodies.add(buildingBlock.getBody());
    }

    /**
     * Returns the left limit/border of the player area in world coordinates.
     * @return a value in meters.
     */
    public float getLeftLimit() {
        return leftLimit;
    }

    /**
     *
     * @return the player name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the right limit/border of the player area in world coordinates.
     * @return a value in meters
     */
    public float getRightLimit() {
        return rightLimit;
    }

    /**
     * The highest building block in a highest valid tower.
     * A valid tower is a tower in which all blocks have contact where some
     * block also has contact with the ground.
     *
     * @return the highest building block in the highest valid tower.
     */
    public BuildingBlock getHighestBuilingBlockInTower() {
        return highestBuilingBlockInTower;
    }

    protected void calcHighestBuildingBlockInTower(GroundBlock groundBlock) {

        towerBodies.add(groundBlock.getBody());

        // Create undirected graph
        LinkedHashMap<Body, LinkedHashSet<Body>> body2neighbours = new LinkedHashMap<Body, LinkedHashSet<Body>>();

        for (BuildingBlock buildingBlock : buildingBlocks) {
            populateNeighbours(body2neighbours, buildingBlock.getBody());
        }
        // Finalize the undirected graph by adding the ground body.
        populateNeighbours(body2neighbours, groundBlock.getBody());
        towerBodies.remove(groundBlock.getBody());

        // Will contain the tower height in the end.
        Body highestConnectedBody = groundBlock.getBody();
        float highestPositionInBody = groundBlock.getBody().getPosition().y;

        HashSet<Body> unprocessedBodies = (HashSet<Body>) towerBodies.clone();


        Queue<Body> bodiesToCheck = new LinkedList<Body>();
        bodiesToCheck.add(groundBlock.getBody());

        while (!bodiesToCheck.isEmpty()) {

            Body bodyToCheck = bodiesToCheck.remove();
            XForm xForm = bodyToCheck.getXForm();

            Object userData = bodyToCheck.getUserData();
            if (userData instanceof RectangularBuildingBlock) {
                RectangularBuildingBlock rbb = (RectangularBuildingBlock) userData;
                for (Vec2 v : rbb.getOutline()) {
                    float yPos = xForm.position.y + xForm.R.col1.y * v.x + xForm.R.col2.y * v.y;
                    if (yPos > highestPositionInBody) {
                        highestPositionInBody = yPos;
                        highestConnectedBody = bodyToCheck;
                    }
                }
            } else if (userData instanceof TriangularBuildingBlock) {
                TriangularBuildingBlock tbb = (TriangularBuildingBlock) userData;
                for (Path2D path2D : tbb.getTriangles()) {
                    for (PathIterator pi = path2D.getPathIterator(null); !pi.isDone(); pi.next()) {
                        float coords[] = new float[8];
                        pi.currentSegment(coords);
                        for (int i = 0; i < coords.length; i += 2) {
                            float yPos = xForm.position.y + xForm.R.col1.y * coords[i] + xForm.R.col2.y * coords[i + 1];
                            if (yPos > highestPositionInBody) {
                                highestPositionInBody = yPos;
                                highestConnectedBody = bodyToCheck;
                            }
                        }
                    }
                }
            }

            for (Body body : body2neighbours.get(bodyToCheck)) {
                if (unprocessedBodies.remove(body)) {
                    // The body is already added to the queue if it has not been checked before
                    bodiesToCheck.add(body);
                }
            }
        }

        if (highestConnectedBody.getUserData() instanceof BuildingBlock) {
            highestBuilingBlockInTower = (BuildingBlock) highestConnectedBody.getUserData();
        } else {
            highestBuilingBlockInTower = null;
        }

    }

    // Used by calcHighestBuildingBlockInTower
    private void populateNeighbours(LinkedHashMap<Body, LinkedHashSet<Body>> body2neighbours, Body body) {
        LinkedHashSet<Body> myNeighbours = getNeighbours(body2neighbours, body);
        for (ContactEdge ce = body.m_contactList; ce != null; ce = ce.next) {
            if (towerBodies.contains(ce.other)) {
                // This body has contact with another body which is also owned by the player.
                // Add references in both directions
                myNeighbours.add(ce.other);
                LinkedHashSet<Body> otherNeighbours = getNeighbours(body2neighbours, body);
                otherNeighbours.add(body);
            }
        }
    }

    // Used by calcHighestBuildingBlockInTower
    private LinkedHashSet<Body> getNeighbours(LinkedHashMap<Body, LinkedHashSet<Body>> body2neighbours, Body body) {
        LinkedHashSet<Body> neighbours = body2neighbours.get(body);
        if (neighbours == null) {
            neighbours = new LinkedHashSet<Body>();
            body2neighbours.put(body, neighbours);
        }
        return neighbours;
    }
}
