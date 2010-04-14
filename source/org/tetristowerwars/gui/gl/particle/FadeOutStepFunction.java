/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.particle;

import java.util.LinkedList;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class FadeOutStepFunction extends ParticleStepFunction {

    private final float startAtLifeRatio;

    public FadeOutStepFunction(float startAtLifeRatio) {
        this.startAtLifeRatio = startAtLifeRatio;
    }

    @Override
    public void step(Particle p, float timeElapsedMs) {

        if (p.ttlRatio > startAtLifeRatio) {
            p.currentColor.a = MathUtil.lerpNoCap(p.ttlRatio, startAtLifeRatio, 1.0f, p.startColor.a, 0);
        }
    }
}
