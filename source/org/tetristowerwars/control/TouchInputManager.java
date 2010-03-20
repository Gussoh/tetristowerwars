/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.SwingUtilities;

/**
 *
 * @author Andreas
 */
public class TouchInputManager extends InputManager implements TuioListener {
    private final Dimension screenDimension;
    private final Component gamePanel;
    
    public TouchInputManager(TuioClient client, Dimension screenDimension, Component gamePanel) {
        client.addTuioListener(this);
        this.screenDimension = screenDimension;
        this.gamePanel = gamePanel;
    }

    @Override
    public void addTuioObject(TuioObject to) {
        // NOT USED
    }

    @Override
    public void updateTuioObject(TuioObject to) {
        // NOT USED
    }

    @Override
    public void removeTuioObject(TuioObject to) {
        // NOT USED
    }

    @Override
    public void addTuioCursor(TuioCursor tc) {
        createInputEvent(InputEvent.PRESSED, tc);
    }

    @Override
    public void updateTuioCursor(TuioCursor tc) {
        createInputEvent(InputEvent.DRAGGED, tc);
    }

    @Override
    public void removeTuioCursor(TuioCursor tc) {
        createInputEvent(InputEvent.RELEASED, tc);
    }

    @Override
    public void refresh(TuioTime tt) {
        // NOT USED
    }

    private void createInputEvent(int type, TuioCursor tc) {
        int x = tc.getScreenX(screenDimension.width);
        int y = tc.getScreenY(screenDimension.height);

        Point point = new Point(x, y);
        SwingUtilities.convertPointFromScreen(point, gamePanel);
        
        InputEvent evt = new InputEvent(type, point, tc.getCursorID());
        pushInputEvent(evt);
    }

    // TODO: Implement
}
