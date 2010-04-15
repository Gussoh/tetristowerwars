/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Andreas
 */
public abstract class InputManager {

    private final List<InputListener> inputListeners = new ArrayList<InputListener>();
    private final Queue<InputEvent> events = new LinkedList<InputEvent>();

    public void addInputListener(InputListener listener) {
        inputListeners.add(listener);
    }

    public void removeInputListener(InputListener listener) {
        inputListeners.remove(listener);
    }

    protected void pushInputEvent(InputEvent event) {
        synchronized (events) {
            events.offer(event);
        }
    }

    public void pumpEvents() {
        synchronized (events) {
            while (!events.isEmpty()) {
                InputEvent inputEvent = events.poll();
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

    public void clearEvents() {
        events.clear();
    }
}
