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
public class GroundMaterial extends Material{

    @Override
    public float getDensity() {
        return 1000f;
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

}
