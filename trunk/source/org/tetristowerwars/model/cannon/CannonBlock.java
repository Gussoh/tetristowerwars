/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model.cannon;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;

/**
 *
 * @author magnus
 */
public class CannonBlock extends Block {

    private final int force;
    private final int coolDown;
    private final long lastShot = 0;

    public CannonBlock(Body[] bodies, int force, int coolDown) {
        super(bodies);
        this.force = force;
        this.coolDown = coolDown;
    }

    public int getForce() {
        return force;
    }

    public Material getMaterial() {
        return new SteelMaterial();
    }

}
