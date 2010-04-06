/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.collision.MassData;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author magnus, Reeen
 */
public abstract class BuildingBlock extends Block {

    protected final Material material;
    protected final MassData massData;

    protected BuildingBlock(Body body, Material material) {
        super(body);
        this.material = material;
        this.massData = new MassData();
    }

    protected abstract void calcMassData();

    public Material getMaterial() {
        return material;
    }

    public MassData getOriginalMassData() {
        return massData;
    }
}
