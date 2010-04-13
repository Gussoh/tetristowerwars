/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.particle;

import org.jbox2d.common.Vec2;

/**
 *
 * @author Andreas
 */
public class Particle {

    protected Vec2 position = new Vec2();
    protected Vec2 velocity = new Vec2();
    protected float angle;
    protected float rotSpeed;
    protected float ttlMs;
    protected float ageMs;

    protected Particle() {
        angle = 0;
        rotSpeed = 0;
        ttlMs = 0;
        ageMs = 0;
    }

    protected Particle(Vec2 position, Vec2 velocity, float angle, float rotSpeed, float ttlMs) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
        this.angle = angle;
        this.ttlMs = ttlMs;
        this.ageMs = 0;
    }

    public float getAgeMs() {
        return ageMs;
    }

    public float getAngle() {
        return angle;
    }

    public Vec2 getPosition() {
        return position;
    }

    public float getRotSpeed() {
        return rotSpeed;
    }

    public float getTtlMs() {
        return ttlMs;
    }

    public Vec2 getVelocity() {
        return velocity;
    }


    public float getTtlRatio() {
        return ageMs / ttlMs;
    }
}
