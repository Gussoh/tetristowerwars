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

    private final Body body;
    private Player owner;


    public Block(Body body) {
        this.body = body;
        body.setUserData(this);
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
