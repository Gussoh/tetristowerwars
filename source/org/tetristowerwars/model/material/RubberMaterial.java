/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.material;

/**
 *
 * @author Andreas
 */
public class RubberMaterial extends Material {

    @Override
    public float getDensity() {
        return 3.0f;
    }

    @Override
    public float getRestitution() {
        return 0.5f;
    }
}
