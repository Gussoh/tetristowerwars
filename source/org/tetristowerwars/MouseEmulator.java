/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.awt.AWTException;
import java.awt.Robot;
import org.tetristowerwars.control.InputEvent;
import org.tetristowerwars.control.InputListener;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.control.TouchInputManager;

/**
 *
 * @author Andreas
 */
public class MouseEmulator implements InputListener {

    private final InputManager inputManager;
    private final Robot robot;
    private int activeId;
    private boolean isActive = false;
    private boolean enabled = false;

    public MouseEmulator(TouchInputManager inputManager) throws AWTException {
        this.inputManager = inputManager;
        robot = new Robot();
    }

    public synchronized void enable() {
        if (!enabled) {
            enabled = true;
            inputManager.addInputListener(this);
        }
    }

    public synchronized void disable() {
        if (enabled) {
            enabled = false;
            inputManager.removeInputListener(this);
            if (isActive) {
                robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
            }
            isActive = false;
        }
    }

    @Override
    public synchronized void onInputDevicePressed(InputEvent event) {
        if (!isActive) {
            isActive = true;
            activeId = event.getActionId();
            robot.mouseMove(event.getPosition().x, event.getPosition().y);
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_MASK);
        }
    }

    @Override
    public synchronized void onInputDeviceReleased(InputEvent event) {
        if (isActive && activeId == event.getActionId()) {
            isActive = false;
            robot.mouseMove(event.getPosition().x, event.getPosition().y);
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_MASK);
        }
    }

    @Override
    public synchronized void onInputDeviceDragged(InputEvent event) {
        if (isActive && activeId == event.getActionId()) {
            robot.mouseMove(event.getPosition().x, event.getPosition().y);
        }
    }
}
