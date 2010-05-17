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

    public static final short PRESSED = 0, RELEASED = 1, DRAGGED = 2;
    private final Vec2 position;
    private final int actionId;
    private final int type;


    /**
     * Creates a new input event, describing the details of the event.
     * @param type Type of event. See constants in InputEvent.
     * @param position The position in world coordinates.
     * @param originator An id identifying the originator of the event.
     */
    public InputEvent(int type, Vec2 position, int originator) {
        this.type = type;
        this.position = position;
        this.actionId = originator;
    }

    public int getActionId() {
        return actionId;
    }

    /**
     *
     * @return the position in window coordinates.
     */
    public Vec2 getPosition() {
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
