/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl.particle;

/**
 *
 * @author Andreas
 */
public class VelocityDampStepFunction extends ParticleStepFunction {
    private final float dampFactor;

    public VelocityDampStepFunction(float dampFactor) {
        this.dampFactor = dampFactor;
    }

    @Override
    public void step(Particle p, float timeElapsedS) {

      
    }
}
