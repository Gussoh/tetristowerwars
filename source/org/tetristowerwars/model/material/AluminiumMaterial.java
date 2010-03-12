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
public class AluminiumMaterial extends Material {

    @Override
    public float getDensity() {
        return 2.7f;
    }

    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY;
    }

}
