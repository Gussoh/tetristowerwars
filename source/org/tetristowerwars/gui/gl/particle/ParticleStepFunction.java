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
public abstract class ParticleStepFunction {

    public abstract void step(Particle p, float timeElapsedS);
}
