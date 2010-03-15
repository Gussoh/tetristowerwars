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
    public MouseInputManager(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
		// System.out.println("MouseInputManager, clicked at " + e.toString());
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        this.fireOnPressEvent(new InputEvent(new Point(e.getX(), e.getY()), 0));
		// System.out.println("MouseInputManager, pressed at " + e.toString());

		/*
		BuildingBlock hitBlock = gameModel.getBlockFromCoordinates(e.getX(), e.getY());

        if (hitBlock != null) {
            System.out.println("ZOMFG, YOU JUST HIT SOMETHING!!!!");
            hitBlock.getBodies()[0].applyForce(new Vec2(7000.0f, 5000.0f), new Vec2(-0.0f, 60.0f));
        }
        else {
            //System.out.println("miss");
        }
		*/
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        this.fireOnReleaseEvent(new InputEvent(new Point(e.getX(), e.getY()), 0));
		// System.out.println("MouseInputManager, released at " + e.toString());
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
		// System.out.println("MouseInputManager, entered at " + e.toString());
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
		// System.out.println("MouseInputManager, exited at " + e.toString());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.fireOnDraggedEvent(new InputEvent(new Point(e.getX(), e.getY()), 0));
		// System.out.println("MouseInputManager, dragged at " + e.toString());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
		// System.out.println("MouseInputManager, moved at " + e.toString());
    }

}
