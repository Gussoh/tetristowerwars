/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author magnus
 */
public class CannonBlock extends Block {

    private final int force;
    private final int coolDown;
    private final long lastShot = 0;
    private float angleValue = 0;


    public CannonBlock(Body body, int force, int coolDown, Player player) {
        super(body);
        this.force = force;
        this.coolDown = coolDown;
        player.addCannon(this);
    }

    public int getForce() {
        return force;
    }

    protected void adjustPipe(float f) {
        angleValue += f;
    }

    public float getAngleInRadians() {
        return (float) Math.asin(angleValue);
    }

    public float getAngleInDegrees() {
        return (float) Math.toDegrees(Math.asin(angleValue));
    }
}
