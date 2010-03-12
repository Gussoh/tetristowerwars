/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Rickard
 */
public class MouseEventController implements MouseListener {

    private final GameModel gameModel;

    public MouseEventController(GameModel g) {
        super();

        gameModel = g;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println("MouseEventController, clicked at " + e.toString());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // System.out.println("MouseEventController, entered at " + e.toString());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // System.out.println("MouseEventController, exited at " + e.toString());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("MouseEventController, pressed at " + e.toString());

        BuildingBlock hitBlock = gameModel.getBlockFromCoordinates(e.getX(), e.getY());

        if (hitBlock != null) {
            System.out.println("ZOMFG, YOU JUST HIT SOMETHING!!!!");
            hitBlock.getBodies()[0].applyForce(new Vec2(4000.0f, 5000.0f), new Vec2(-0.0f, 60.0f));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // System.out.println("MouseEventController, released at " + e.toString());
    }
}
