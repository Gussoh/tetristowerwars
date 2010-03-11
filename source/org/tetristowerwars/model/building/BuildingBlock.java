/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.building;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author magnus
 */
public class BuildingBlock extends Block {
    private final Material material;

    protected BuildingBlock(Body[] bodies, Material material) {
        super(bodies);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    
}
