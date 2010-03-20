/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.awt.Point;

/**
 *
 * @author Andreas
 */
public class InputEvent {

    public static final int PRESSED = 0, RELEASED = 1, DRAGGED = 2;
    private final Point position;
    private final int actionId;
    private final int type;


    /**
     * Creates a new input event, describing the details of the event.
     * @param position The position in game world coordinates where the event occurred.
     * @param originator An id identifying the originator of the event.
     */
    public InputEvent(int type, Point position, int originator) {
        this.type = type;
        this.position = position;
        this.actionId = originator;
    }

    public int getActionId() {
        return actionId;
    }

    public Point getPosition() {
        return position;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Id: " + actionId + ", position: " + position;
    }


}
