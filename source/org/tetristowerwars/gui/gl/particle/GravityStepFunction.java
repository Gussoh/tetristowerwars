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

    private float gravity;

    public GravityStepFunction() {
        gravity = -9.82f;
    }

    public GravityStepFunction(float verticalGravity) {
        this.gravity = verticalGravity;
    }

    @Override
    public void step(Particle p, float timeElapsedS) {
        p.velocity.y += gravity * timeElapsedS;
    }
}
