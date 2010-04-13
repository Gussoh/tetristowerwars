/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl.particle;

import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public abstract class ParticleEngine {
    
    protected LinkedList<Particle> particles = new LinkedList<Particle>();
    private ParticleStepFunction stepFunction = null;

    public abstract void createParticles(int numParticles);

    public void setStepFunction(ParticleStepFunction stepFunction) {
        this.stepFunction = stepFunction;
    }
    
    public void update(float elapsedTimeMs) {
        if (stepFunction != null) {
            stepFunction.step(particles, elapsedTimeMs);
        }
    }

    public LinkedList<Particle> getParticles() {
        return particles;
    }
}
