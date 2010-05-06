/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * rubber walls
 *
   dra till kanon: lower countdown
 * own block pool
 * more cannons
 * cannon pool stacks

 * dra till ett annat block:
 * rubber blocks
 * ghost blocks - cannon balls go through
 * invounrable blocks
 *
 * n'r man drar dem ska de inte kollidera med n[gonting?
 * n'r de bara ligger ska de fungera som vanliga block
 *
 *
 */

package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author Gussoh
 */
public class PowerupBlock extends Block {

    public PowerupBlock(Body body) {
        super(body);
    }

}
