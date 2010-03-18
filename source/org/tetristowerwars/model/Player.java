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

    public Player(String name) {
        this.name = name;
    }

    public LinkedHashSet<BuildingBlock> getBuildingBlocks() {
        return buildingBlocks;
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
}
