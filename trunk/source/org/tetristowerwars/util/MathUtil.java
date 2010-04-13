/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.util;

import org.jbox2d.common.XForm;

/**
 *
 * @author Andreas
 */
public class MathUtil {

    public static final float PI = (float) Math.PI;

    public static float lerp(float value, float minIn, float maxIn, float minOut, float maxOut) {
        float lerpedValue = ((value - minIn) / (maxIn - minIn)) * (maxOut - minOut) + minOut;

        if (lerpedValue > maxOut) {
            return maxOut;
        } else if (lerpedValue < minOut) {
            return minOut;
        } else {
            return lerpedValue;
        }
    }

    public static float lerp(float ratio, float min, float max) {
        return ratio * (max - min) + min;
    }

    public static float random(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    
    public static Vec3 rotateNormal(XForm T, Vec3 v) {
        return new Vec3(T.R.col1.x * v.x + T.R.col2.x * v.y,
                T.R.col1.y * v.x + T.R.col2.y * v.y, v.z);
    }
}
