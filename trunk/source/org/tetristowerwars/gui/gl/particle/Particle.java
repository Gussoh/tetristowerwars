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

    protected Vec2 position;
    protected final Vec2 startVelocity;
    protected Vec2 velocity;
    protected float angle;
    protected float rotSpeed;
    protected final float ttlS;
    protected float ageS;
    protected float ttlRatio;
    protected final Color startColor;
    protected Color currentColor;
    protected float radius;

    protected Particle(Vec2 position, float radius, Vec2 velocity, float angle, float rotSpeed, float ttlS, Color color) {
        this.position = new Vec2(position.x, position.y);
        this.radius = radius;
        this.velocity = new Vec2(velocity.x, velocity.y);
        this.startVelocity = new Vec2(velocity.x, velocity.y);
        this.angle = angle;
        this.ttlS = ttlS;
        this.ageS = 0;
        this.startColor = new Color(color);
        this.currentColor = new Color(color);
    }

    public float getAgeS() {
        return ageS;
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

    public float getTtlS() {
        return ttlS;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public float getTtlRatio() {
        return ttlRatio;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public Color getStartColor() {
        return startColor;
    }

    public float getRadius() {
        return radius;
    }
}
