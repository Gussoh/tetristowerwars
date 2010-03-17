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
public class ConcreteMaterial extends Material{

    @Override
    public float getDensity() {
        return 3f;
    }

    @Override
    public Color getColor() {
        return Color.GRAY;
    }

}