/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.GroundMaterial;

/**
 *
 * @author Andreas
 */
public class GroundBlock extends Block {

    public GroundBlock(Body body) {
        super(body, new GroundMaterial());
    }
}
