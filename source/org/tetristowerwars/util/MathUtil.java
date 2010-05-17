/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.util;

import org.jbox2d.common.Vec2;
import org.jbox2d.common.XForm;

/**
 *
 * @author Andreas
 */
public class MathUtil {

    public static final float PI = (float) Math.PI;

    public static float lerp(float value, float minIn, float maxIn, float minOut, float maxOut) {
        if (value > maxIn) {
            return maxOut;
        } else if (value < minIn) {
            return minOut;
        } else {
            return lerpNoCap(value, minIn, maxIn, minOut, maxOut);
        }
    }

    public static float lerpNoCap(float value, float minIn, float maxIn, float minOut, float maxOut) {
        float ratio = ((value - minIn) / (maxIn - minIn));
        return (1 - ratio) * minOut + ratio * maxOut;
    }

    public static float lerpNoCap(float ratio, float min, float max) {
        return (1 - ratio) * min + ratio * max;
    }

    public static float random(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    public static Vec3 rotateNormal(XForm T, Vec3 v) {
        return new Vec3(T.R.col1.x * v.x + T.R.col2.x * v.y,
                T.R.col1.y * v.x + T.R.col2.y * v.y, v.z);
    }

    public static float vecLength(Vec2 a, Vec2 b) {
        float x = Math.abs(a.x - b.x);
        float y = Math.abs(a.y - b.y);

        double x2 = Math.pow(x, 2);
        double y2 = Math.pow(y, 2);

        double length =  Math.sqrt(x2 + y2);

        return (float) length;
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
