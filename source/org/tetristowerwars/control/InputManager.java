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
    private final List<InputEvent> events = new ArrayList<InputEvent>();

    public void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }

    public void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    protected void pushInputEvent(InputEvent event) {
        synchronized (events) {
            events.add(event);
        }
    }

    public void pumpEvents() {
        synchronized (events) {
            for (InputEvent inputEvent : events) {
                switch (inputEvent.getType()) {
                    case InputEvent.PRESSED:
                        fireOnPressEvent(inputEvent);
                        break;
                    case InputEvent.RELEASED:
                        fireOnReleaseEvent(inputEvent);
                        break;
                    case InputEvent.DRAGGED:
                        fireOnDragEvent(inputEvent);
                        break;
                }
            }

            events.clear();
        }
    }

    private void fireOnPressEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDevicePressed(event);
        }
    }

    private void fireOnReleaseEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDeviceReleased(event);
        }
    }

    private void fireOnDragEvent(InputEvent event) {
        for (InputListener inputListener : inputListeners) {
            inputListener.onInputDeviceDragged(event);
        }
    }
}
