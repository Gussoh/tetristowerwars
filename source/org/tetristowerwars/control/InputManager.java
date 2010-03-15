/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas
 */
public abstract class InputManager {

    private List<InputListener> inputListeners = new ArrayList<InputListener>();

    public void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }

    public void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    protected void fireOnPressEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDevicePressed(event);
        }
    }

    protected void fireOnReleaseEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDeviceReleased(event);
        }
    }

    protected void fireOnDraggedEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDeviceDragged(event);
        }
    }
}
