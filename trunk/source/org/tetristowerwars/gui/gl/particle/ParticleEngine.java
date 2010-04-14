/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public abstract class ParticleEngine {
    
    protected LinkedList<Particle> particles = new LinkedList<Particle>();
    private final ArrayList<ParticleStepFunction> stepFunctions = new ArrayList<ParticleStepFunction>();

    public abstract void createParticles(int numParticles);

    public void addStepFunction(ParticleStepFunction stepFunction) {
        stepFunctions.add(stepFunction);
    }
    
    public void update(float elapsedTimeS) {
            
        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            p.ageS += elapsedTimeS;

            if (p.ageS > p.ttlS) {
                it.remove();
            }

            p.position.x += p.velocity.x * elapsedTimeS;
            p.position.y += p.velocity.y * elapsedTimeS;

            p.angle += p.rotSpeed * elapsedTimeS;
            p.ttlRatio = p.ageS / p.ttlS;

            for (ParticleStepFunction particleStepFunction : stepFunctions) {
                particleStepFunction.step(p, elapsedTimeS);
            }
        }        
    }

    public LinkedList<Particle> getParticles() {
        return particles;
    }
}
