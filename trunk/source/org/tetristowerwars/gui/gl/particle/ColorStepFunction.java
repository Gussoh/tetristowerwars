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
public class ColorStepFunction extends ParticleStepFunction {

    private final Color endColor;
    private final float startAtLifeRatio;

    public ColorStepFunction(float startAtLifeRatio, Color endColor) {
        this.endColor = new Color(endColor);
        this.startAtLifeRatio = startAtLifeRatio;
    }

    @Override
    public void step(Particle p, float timeElapsedMs) {
        if (p.ttlRatio > startAtLifeRatio) {
            float ratio = (p.ttlRatio - startAtLifeRatio) / (1.0f - startAtLifeRatio);
            p.currentColor.r = MathUtil.lerpNoCap(ratio, p.startColor.r, endColor.r);
            p.currentColor.g = MathUtil.lerpNoCap(ratio, p.startColor.g, endColor.g);
            p.currentColor.b = MathUtil.lerpNoCap(ratio, p.startColor.b, endColor.b);
            p.currentColor.a = MathUtil.lerpNoCap(ratio, p.startColor.a, endColor.a);
        }
    }
}
