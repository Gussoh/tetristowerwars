/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.control;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Andreas
 */
public class MouseInputManager extends InputManager implements MouseListener, MouseMotionListener {

    private static final int dummyId = 0;
    private final Component component;

    public MouseInputManager(Component component) {
        this.component = component;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // Not used
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        InputEvent evt = new InputEvent(InputEvent.PRESSED, new Point(e.getX(), e.getY()), dummyId);
        fireOnPressEvent(evt);
        
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        InputEvent evt = new InputEvent(InputEvent.RELEASED, new Point(e.getX(), e.getY()), dummyId);
        fireOnReleaseEvent(evt);
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        InputEvent evt = new InputEvent(InputEvent.DRAGGED, new Point(e.getX(), e.getY()), dummyId);
        fireOnDragEvent(evt);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    @Override
    public void unregisterEventProvider() {
        component.removeMouseListener(this);
        component.removeMouseMotionListener(this);
    }
}
