/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author magnus
 */
public class CannonBlock extends Block {

    private final int force;
    private final int coolDown;
    private final long lastShot = 0;
    private float angleValue = 0;
    private float speedFactor = 1f;
    private final boolean shootingToLeft;
    
    public CannonBlock(Body body, int force, int coolDown, Player player, boolean shootToLeft) {
        super(body);
        this.force = force;
        this.coolDown = coolDown;
        player.addCannon(this);
        this.shootingToLeft = shootToLeft;
    }

    public int getForce() {
        return force;
    }

    protected void adjustPipe(float f) {
        angleValue += f;
    }

    public float getAngleInRadians() {
        return MathUtil.lerp((float)Math.sin(angleValue * speedFactor), -1f, 1f, 0f, (float)Math.PI * 0.5f);

    }

    public float getAngleInDegrees() {
        return MathUtil.lerp((float)Math.sin(angleValue * speedFactor), -1f, 1f, 0f, 90f);
    }

    public boolean isShootingToLeft() {
        return shootingToLeft;
    }
}
