/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author magnus
 */
public abstract class Block {

    private final Body[] bodies;
    private Player owner;


    public Block(Body[] bodies) {
        this.bodies = bodies;
        
        for (Body body : bodies) {
            body.setUserData(this);
        }
    }


    public Body[] getBodies() {
        return bodies;
    }

    public Player getOwner() {
        return owner;
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
    }
}
