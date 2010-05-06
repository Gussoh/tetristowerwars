/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.material;

/**
 *
 * @author Andreas
 */
public class PowerupMaterial extends Material {

    private final SteelMaterial material = new SteelMaterial();

    @Override
    public float getDensity() {
        return material.getDensity();
    }

}
