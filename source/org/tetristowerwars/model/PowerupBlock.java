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

import java.awt.geom.Rectangle2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Gussoh
 */
public class PowerupBlock extends RectangularBuildingBlock {

    public PowerupBlock(Body body, Material material, Rectangle2D[] rectangles, Vec2[] outline) {
        super(body, material, rectangles, outline);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
}
