/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui.gl;

import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.Block;

/**
 *
 * @author Andreas
 */
public class Pointer {
    protected Vec2 pos;
    protected boolean hit;

    public Pointer(Vec2 pos, boolean hit) {
        this.pos = pos;
        this.hit = hit;
    }
}
