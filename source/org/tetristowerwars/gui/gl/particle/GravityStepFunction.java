/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.particle;

import java.util.Iterator;
import java.util.LinkedList;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Andreas
 */
public class GravityStepFunction extends ParticleStepFunction {

    private Vec2 gravity;
    private final float dampFactor;

    public GravityStepFunction() {
        gravity = new Vec2(0, -9.82f);
        dampFactor = 1;
    }

    public GravityStepFunction(float dampFactor) {
        this.dampFactor = dampFactor;
        gravity = new Vec2(0, -9.82f);
    }

    

    public GravityStepFunction(Vec2 gravity, float dampFactor) {
        this.gravity = gravity;
        this.dampFactor = dampFactor;
    }

    @Override
    public void step(LinkedList<Particle> particles, float timeElapsedMs) {

        float timeElapsedS = timeElapsedMs * 0.001f;

        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();

            p.ageMs += timeElapsedMs;

            if (p.ageMs > p.ttlMs) {
                it.remove();
            } else {
                p.position.x += p.velocity.x * timeElapsedS;
                p.position.y += p.velocity.y * timeElapsedS;

                p.velocity.x += gravity.x * timeElapsedS;
                p.velocity.y += gravity.y * timeElapsedS;

                float dampValue = 1.0f - (1.0f - dampFactor) * (timeElapsedS * 10);

                p.velocity.x *= dampValue;
                p.velocity.y *= dampValue;

                p.angle += p.rotSpeed * timeElapsedS;
            }
        }
    }
}
