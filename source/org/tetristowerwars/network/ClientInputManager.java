/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.network;

import org.tetristowerwars.control.InputEvent;
import org.tetristowerwars.control.InputManager;

/**
 *
 * @author Andreas
 */
public class ClientInputManager extends InputManager {


    @Override
    public void unregisterEventProvider() {
    }

    protected void handleIncomingEvent(InputEvent inputEvent) {
        switch (inputEvent.getType()) {
            case InputEvent.DRAGGED:
                fireOnDragEvent(inputEvent);
                break;
            case InputEvent.PRESSED:
                fireOnPressEvent(inputEvent);
                break;
            case InputEvent.RELEASED:
                fireOnReleaseEvent(inputEvent);
                break;
        }
    }

}
