/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 *
 * @author magnus
 */
public abstract class Block {

    private final Body body;
    private Player owner;
    private boolean destroyed = false;
    private boolean powerupHilighted = false;
    private boolean hilighted = false;

    public Block(Body body) {
        this.body = body;
        body.setUserData(this);
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
}
