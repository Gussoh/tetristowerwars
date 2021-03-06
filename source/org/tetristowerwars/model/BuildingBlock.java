/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.collision.MassData;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.GhostMaterial;
import org.tetristowerwars.model.material.InvulnerableMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.RubberMaterial;

/**
 *
 * @author magnus, Reeen
 */
public abstract class BuildingBlock extends Block implements Upgradable {

    
    protected MassData massData;

    public static final int NORMAL = 0, RUBBER = 1, GHOST = 2, INVULNERABLE = 3;
    private int mode = NORMAL;
    

    protected BuildingBlock(Body body, Material material) {
        super(body, material);
        this.massData = new MassData();
    }

    protected abstract void updateMassData();


    public MassData getOriginalMassData() {
        return massData;
    }

    @Override
    public boolean isUpgradable() {
        return mode != INVULNERABLE;
    }

    @Override
    public void upgrade() {
        mode = Math.min(mode + 1, INVULNERABLE);

        switch (mode) {
            case RUBBER:
                setMaterial(new RubberMaterial());
                break;
            case GHOST:
                setMaterial(new GhostMaterial());
                break;
            case INVULNERABLE:
                setMaterial(new InvulnerableMaterial());
                break;
        }
    }

    @Override
    public void setMaterial(Material material) {
        super.setMaterial(material);
        updateMassData();
    }


}
