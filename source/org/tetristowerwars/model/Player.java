/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.util.Collections;
import org.tetristowerwars.model.cannon.CannonBlock;
import org.tetristowerwars.model.building.BuildingBlock;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jbox2d.collision.AABB;
import org.tetristowerwars.model.cannon.BulletBlock;

/**
 *
 * @author magnus
 */
public class Player {

    private final LinkedHashSet<BuildingBlock> buildingBlocks = new LinkedHashSet<BuildingBlock>();
    private final LinkedHashSet<CannonBlock> cannons = new LinkedHashSet<CannonBlock>();
    private final LinkedHashSet<BulletBlock> bullets = new LinkedHashSet<BulletBlock>();
    private final String name;
    private final float leftLimit;
    private final float rightLimit;


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

    public void addCannon(CannonBlock cannonBlock) {
        if (cannonBlock.getOwner() != null) {
            throw new IllegalArgumentException("Cannon already owned by player: " + cannonBlock.getOwner());
        }
        cannons.add(cannonBlock);
        cannonBlock.setOwner(this);
    }

    public void removeCannon(CannonBlock cannonBlock) {
        if (cannons.remove(cannonBlock)) {
            cannonBlock.setOwner(null);
        }
    }

    public void addBullet(BulletBlock bullet) {
        if (bullet.getOwner() != null) {
            throw new IllegalArgumentException("Bullet already owned by player: " + bullet.getOwner());
        }
        bullets.add(bullet);
        bullet.setOwner(this);
    }

    public void removeBullet(BulletBlock bullet) {
        if (bullets.remove(bullet)) {
            bullet.setOwner(null);
        }
    }

    protected void removeBuildingBlock(BuildingBlock buildingBlock) {
        if (buildingBlocks.remove(buildingBlock)) {
            buildingBlock.setOwner(null);
        }
    }

    protected void addBuildingBlock(BuildingBlock buildingBlock) {
        if (buildingBlock.getOwner() != null) {
            throw new IllegalArgumentException("BuildingBlock already owned by player: " + buildingBlock.getOwner());
        }
        buildingBlocks.add(buildingBlock);
        buildingBlock.setOwner(this);
    }

    public float getLeftLimit() {
        return leftLimit;
    }

    public String getName() {
        return name;
    }

    public float getRightLimit() {
        return rightLimit;
    }

    
}
