/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.collision.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author magnus
 */
public abstract class Block {

    private Material material;
    private final Body body;
    private Player owner;
    private boolean destroyed = false;
    private boolean powerupHilighted = false;
    private boolean hilighted = false;

    public Block(Body body, Material material) {
        this.body = body;
        body.setUserData(this);
        this.material = material;
    }

    public boolean isPowerupHilighted() {
        return powerupHilighted;
    }

    public void setPowerupHilighted(boolean hilighted) {
        this.powerupHilighted = hilighted;
    }


    public void setHilighted(boolean b) {
        hilighted = b;
    }

    public boolean isHilighted() {
        return hilighted;
    }

    protected void destroyBody(World world) {
        if (!destroyed) {
            destroyed = true;
            world.destroyBody(body);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Body getBody() {
        return body;
    }

    public Player getOwner() {
        return owner;
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
        for (Shape s = body.getShapeList(); s != null; s = s.getNext()) {
            s.setRestitution(material.getRestitution());
        }
    }

    
}
