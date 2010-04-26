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

    private final List<InputListener> inputListeners = new ArrayList<InputListener>();

    public synchronized void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }

    public synchronized void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    public synchronized void close() {
        inputListeners.clear();
        unregisterEventProvider();
    }

    public abstract void unregisterEventProvider();

    protected synchronized void fireOnPressEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDevicePressed(event);
        }
    }

    protected synchronized void fireOnReleaseEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDeviceReleased(event);
        }
    }

    protected synchronized void fireOnDragEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDeviceDragged(event);
        }
    }

}
