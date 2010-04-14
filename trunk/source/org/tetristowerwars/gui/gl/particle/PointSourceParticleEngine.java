/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.particle;

import org.jbox2d.common.Vec2;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class PointSourceParticleEngine extends ParticleEngine {

    protected Vec2 position = new Vec2();
    protected float startDirection;
    protected float halfSpread;
    protected float minSpeed;
    protected float maxSpeed;
    protected float minRotSpeed;
    protected float maxRotSpeed;
    protected float minTtlMs;
    protected float maxTtlMs;
    protected Color minColor = new Color();
    protected Color maxColor = new Color();
    private boolean lockedColorRatio;

    public void setDirection(float direction, float spread) {
        this.startDirection = direction;
        this.halfSpread = spread * 0.5f;
    }

    public void setPosition(Vec2 position) {
        this.position = new Vec2(position.x, position.y);
    }

    public void setSpeed(float minSpeed, float maxSpeed) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    /**
     *
     * @param minRotSpeed in radians per second
     * @param maxRotSpeed in radians per second
     */
    public void setRotationSpeed(float minRotSpeed, float maxRotSpeed) {
        this.minRotSpeed = minRotSpeed;
        this.maxRotSpeed = maxRotSpeed;
    }

    public void setTimeToLive(float minTtlMs, float maxTtlMs) {
        this.minTtlMs = minTtlMs;
        this.maxTtlMs = maxTtlMs;
    }

    public void setColor(Color minColor, Color maxColor, boolean lockedRatio) {
        this.minColor = new Color(minColor);
        this.maxColor = new Color(maxColor);
        this.lockedColorRatio = lockedRatio;
    }

    @Override
    public void createParticles(int numParticles) {
        for (int i = 0; i < numParticles; i++) {
            float initialAngle = MathUtil.random(0, 2.0f * (float) Math.PI);
            float direction = MathUtil.random(startDirection - halfSpread, startDirection + halfSpread);
            float speed = MathUtil.random(minSpeed, maxSpeed);
            float rotSpeed = MathUtil.random(minRotSpeed, maxRotSpeed);
            float ttlMs = MathUtil.random(minTtlMs, maxTtlMs);

            Color color;

            if (lockedColorRatio) {
                float ratio = (float) Math.random();
                float r = MathUtil.lerp(ratio, minColor.r, maxColor.r);
                float g = MathUtil.lerp(ratio, minColor.g, maxColor.g);
                float b = MathUtil.lerp(ratio, minColor.b, maxColor.b);
                float a = MathUtil.lerp(ratio, minColor.a, maxColor.a);
                color = new Color(r, g, b, a);
            } else {
                float r = MathUtil.lerp((float) Math.random(), minColor.r, maxColor.r);
                float g = MathUtil.lerp((float) Math.random(), minColor.r, maxColor.r);
                float b = MathUtil.lerp((float) Math.random(), minColor.r, maxColor.r);
                float a = MathUtil.lerp((float) Math.random(), minColor.r, maxColor.r);
                color = new Color(r, g, b, a);
            }

            Vec2 velocity = new Vec2((float) Math.cos(direction) * speed, (float) Math.sin(direction) * speed);

            particles.add(new Particle(position, velocity, initialAngle, rotSpeed, ttlMs, color));
        }
    }
}
