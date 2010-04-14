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
    protected float ttlS;
    protected float ageS;
    protected float ttlRatio;
    protected Color startColor, currentColor;

    protected Particle() {
        angle = 0;
        rotSpeed = 0;
        ttlS = 0;
        ageS = 0;
    }

    protected Particle(Vec2 position, Vec2 velocity, float angle, float rotSpeed, float ttlS, Color color) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.velocity.x = velocity.x;
        this.velocity.y = velocity.y;
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
}
