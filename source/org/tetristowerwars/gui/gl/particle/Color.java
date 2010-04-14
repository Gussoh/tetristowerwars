/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl.particle;

/**
 *
 * @author Andreas
 */
public class Color {
    public float r, g, b, a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(Color c) {
        r = c.r;
        g = c.g;
        b = c.b;
        a = c.a;
    }

    public Color() {
    }
}
