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

/**
 *
 * @author Andreas
 */
public class TouchInputManager extends InputManager implements TuioListener {
    private final Dimension screenDimension;
    private final Component panelComponent;
    
    public TouchInputManager(TuioClient client, Dimension screenDimension, Component panelComponent) {
        client.addTuioListener(this);
        this.screenDimension = screenDimension;
        this.panelComponent = panelComponent;
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
        fireOnPressEvent(createInputEvent(tc));
    }

    @Override
    public void updateTuioCursor(TuioCursor tc) {
        fireOnDraggedEvent(createInputEvent(tc));
    }

    @Override
    public void removeTuioCursor(TuioCursor tc) {
        fireOnReleaseEvent(createInputEvent(tc));
    }

    @Override
    public void refresh(TuioTime tt) {
        // NOT USED
    }

    private InputEvent createInputEvent(TuioCursor tc) {
        int x = tc.getScreenX(screenDimension.width);
        int y = tc.getScreenY(screenDimension.height);
        Point locationOnScreen = panelComponent.getLocationOnScreen();
        Point locationOnPanel = new Point(x - locationOnScreen.x, y - locationOnScreen.y);

        return new InputEvent(locationOnPanel, tc.getCursorID());
    }

    // TODO: Implement
}
