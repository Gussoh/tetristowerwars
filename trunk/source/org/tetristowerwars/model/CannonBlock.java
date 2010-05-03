/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author magnus
 */
public class CannonBlock extends Block {

    private final float force;
    private final float coolDown;
    private float remainingCoolDown;
    private float angleValue = 0;
    private float speedFactor = 1f;
    private final boolean shootingToLeft;
    private boolean cannonLoaded = false;
    private float timeUntilShooting = 0;
    private Material shotMaterial;
    private final BulletFactory bulletFactory;
    
    public CannonBlock(Body body, float force, float coolDown, Player player, boolean shootToLeft, BulletFactory bulletFactory) {
        super(body);
        this.force = force;
        this.coolDown = coolDown;
        player.addCannon(this);
        this.shootingToLeft = shootToLeft;
        this.bulletFactory = bulletFactory;
    }

    public float getForce() {
        return force * MathUtil.lerp(shotMaterial.getDensity(), 0.7f, 10.0f, 0.7f, 6.0f);
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

    protected void shoot(Material material) {
        shotMaterial = material;
        cannonLoaded = true;
        timeUntilShooting = 3.0f;
    }

    public boolean isCannonLoaded() {
        return cannonLoaded;
    }

    public float getTimeUntilShooting() {
        return timeUntilShooting;
    }
    public float getRemainingCoolDown() {
        return remainingCoolDown;
    }

    public void update(float timeElapsedS) {
        angleValue += timeElapsedS;

        if (cannonLoaded) {
            timeUntilShooting -= timeElapsedS;

            if (timeUntilShooting < 0) {
                timeUntilShooting = 0;
                bulletFactory.createBullet(this, shotMaterial);
                cannonLoaded = false;
                remainingCoolDown = coolDown;
            }
        } else {
            remainingCoolDown -= timeElapsedS;
            remainingCoolDown = Math.max(0, remainingCoolDown);
        }
    }
    // TODO: Fix cannon reset
}
