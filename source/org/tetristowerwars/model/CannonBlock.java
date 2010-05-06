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
public class CannonBlock extends Block implements Upgradable {

    private final float force;
    private float remainingCoolDown;
    private float angleValue = 0;
    private float speedFactor = 1f;
    private final boolean shootingToLeft;
    private boolean armed = false;
    private float timeUntilShooting = 0;
    private Material shotMaterial;
    private final BulletFactory bulletFactory;
    private float shootTime;
    
    public CannonBlock(Body body, float force, float shootTime, Player player, boolean shootToLeft, BulletFactory bulletFactory) {
        super(body);
        this.force = force;
        player.addCannon(this);
        this.shootingToLeft = shootToLeft;
        this.bulletFactory = bulletFactory;
        this.shootTime = shootTime;
    }

    public float getForce() {
        return force * MathUtil.lerp(shotMaterial.getDensity(), 0.7f, 10.0f, 0.7f, 6.0f);
    }


    @Override
    public void upgrade() {
        shootTime = Math.max(shootTime - 1, 0);
    }

    @Override
    public boolean isUpgradable() {
        return shootTime > 0;
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
        armed = true;
        timeUntilShooting = shootTime;
    }

    public boolean isArmed() {
        return armed;
    }

    public float getTimeUntilShooting() {
        return timeUntilShooting;
    }
    public float getRemainingCoolDown() {
        return remainingCoolDown;
    }

    public void update(float timeElapsedS) {
        angleValue += timeElapsedS;

        if (armed) {
            timeUntilShooting -= timeElapsedS;

            if (timeUntilShooting < 0) {
                timeUntilShooting = 0;
                bulletFactory.createBullet(this, shotMaterial);
                armed = false;
            }
        } 
    }

    public void abort() {
        armed = false;
    }
    // TODO: Fix cannon reset
}
