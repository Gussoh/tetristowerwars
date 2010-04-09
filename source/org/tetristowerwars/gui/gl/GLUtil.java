/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Andreas
 */
public class GLUtil {

    static void fixTextureParameters(Texture texture) {
        texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
        texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    }

    /**
     * Rotates a vectore around (0, 0).
     *
     * @param v the vector to rotate
     * @param radians radians to rotate
     * @return a new rotated vector.
     */
    public static Vec2 rotate(Vec2 v, float radians) {
        Vec2 newVec = new Vec2(v.x, v.y);
        doRotate(newVec, radians);

        return newVec;
    }

    public static Vec2 rotate(Vec2 v, float radians, Vec2 center) {
        Vec2 newVec = new Vec2(v.x - center.x, v.y - center.y);
        doRotate(newVec, radians);
        newVec.x += center.x;
        newVec.y += center.y;

        return newVec;
    }

    private static void doRotate(Vec2 v, float radians) {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        float tempX = v.x * cos + v.y * sin;
        v.y = v.x * -sin + v.y * cos;
        v.x = tempX;
    }

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

    public static float random(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }
}
