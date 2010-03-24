/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.Player;

/**
 *
 * @author magnus
 */
public class CannonBlock extends Block {

    private final int force;
    private final int coolDown;
    private final long lastShot = 0;
    

    public CannonBlock(Body[] bodies, int force, int coolDown, Player player) {
        super(bodies);
        this.force = force;
        this.coolDown = coolDown;
        player.addCannon(this);
    }

    public int getForce() {
        return force;
    }
}
