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

    public Block(Body body) {
        this.body = body;
        body.setUserData(this);
    }


    protected void destroyBody(World world) {
        if (!destroyed) {
            destroyed = true;
            world.destroyBody(body);
        }
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
