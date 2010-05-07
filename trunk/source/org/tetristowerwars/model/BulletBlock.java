/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.GhostMaterial;
import org.tetristowerwars.model.material.InvulnerableMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.RubberMaterial;

/**
 *
 * @author magnus
 */
public class BulletBlock extends Block {

    private final CannonBlock cannon;
    private int numCollisions = 0;

    public BulletBlock(Body body, CannonBlock cannon, Material material) {
        super(body, material);
        this.cannon = cannon;
        cannon.getOwner().addBullet(this);
    }

    public CannonBlock getCannon() {
        return cannon;
    }

    /**
     *
     * @param otherBlock
     * @return true if bullet should be destroyed, otherwise false.
     */
    protected boolean addCollision(Block otherBlock) {
        numCollisions++;
        Material mat = getMaterial();
        if (mat instanceof RubberMaterial) {
            return numCollisions >= 3;
        } else if (mat instanceof GhostMaterial && otherBlock.getMaterial() instanceof GhostMaterial) {
            return true;
        } else if (mat instanceof InvulnerableMaterial) {
            return otherBlock instanceof GroundBlock || otherBlock instanceof CannonBlock;
        } else if (otherBlock.getMaterial() instanceof RubberMaterial) {
            return false;
        }

        return true;
    }

    public int getNumCollisions() {
        return numCollisions;
    }
}
