/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model.material;

import java.awt.Color;

/**
 *
 * @author Reeen
 */
public abstract class Material {

    public abstract float getDensity(); //defined in g/cm^3

    public static Material createRandomMaterial() {

        double randomValue = Math.random();

        if (randomValue < 0.36) {
            return new WoodMaterial();
        } else if (randomValue < 0.66) {
            return new BrickMaterial();
        } else {
            return new SteelMaterial();
        }
    }
}
