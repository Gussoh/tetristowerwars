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
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.Renderer;

/**
 *
 * @author Andreas
 */
public class MouseInputManager extends InputManager implements MouseListener, MouseMotionListener {

    private static final int dummyId = 0;
    private final Renderer renderer;

    public MouseInputManager(Renderer renderer) {
        this.renderer = renderer;
        Component component = renderer.getInputComponent();
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // Not used
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        Vec2 worldCoordinates = renderer.convertWindowToWorldCoordinates(e.getPoint());
        InputEvent evt = new InputEvent(InputEvent.PRESSED, worldCoordinates, dummyId);
        fireOnPressEvent(evt);
        
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        Vec2 worldCoordinates = renderer.convertWindowToWorldCoordinates(e.getPoint());
        InputEvent evt = new InputEvent(InputEvent.RELEASED, worldCoordinates, dummyId);
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
        Vec2 worldCoordinates = renderer.convertWindowToWorldCoordinates(e.getPoint());
        InputEvent evt = new InputEvent(InputEvent.DRAGGED, worldCoordinates, dummyId);
        fireOnDragEvent(evt);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }

    @Override
    public void unregisterEventProvider() {
        Component component = renderer.getInputComponent();
        component.removeMouseListener(this);
        component.removeMouseMotionListener(this);
    }
}
