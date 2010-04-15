/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl.particle;

import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class VelocityDampStepFunction extends ParticleStepFunction {
    private final float dampTimeS;

    public VelocityDampStepFunction(float dampTimeS) {
        this.dampTimeS = dampTimeS;
    }

    @Override
    public void step(Particle p, float timeElapsedS) {
        p.velocity.x = MathUtil.lerp(p.getAgeS(), 0, dampTimeS, p.startVelocity.x, 0);
        p.velocity.y = MathUtil.lerp(p.getAgeS(), 0, dampTimeS, p.startVelocity.y, 0);
    }
}
