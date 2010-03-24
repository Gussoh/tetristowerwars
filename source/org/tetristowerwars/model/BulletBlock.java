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
public class BulletBlock extends Block {

    private final CannonBlock cannon;

    public BulletBlock(Body body, CannonBlock cannon) {
        super(body);
        this.cannon = cannon;
        cannon.getOwner().addBullet(this);
    }

    public CannonBlock getCannon() {
        return cannon;
    }
}
