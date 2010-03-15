/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.awt.Point;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Andreas
 */
public class InputEvent {
    private final Point position;
    private final int actionId;


    /**
     * Creates a new input event, describing the details of the event.
     * @param position The position in game world coordinates where the event occurred.
     * @param originator An id identifying the originator of the event.
     */
    public InputEvent(Point position, int originator) {
        this.position = position;
        this.actionId = originator;
    }

    public int getActionId() {
        return actionId;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Id: " + actionId + ", position: " + position;
    }


}
