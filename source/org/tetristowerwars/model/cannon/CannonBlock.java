/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model.cannon;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.Block;

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

}
