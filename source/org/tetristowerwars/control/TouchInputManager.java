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
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.Renderer;

/**
 *
 * @author Andreas
 */
public class TouchInputManager extends InputManager implements TuioListener {

    private final Dimension screenDimension;
    private final TuioClient tuioClient;
    private final Renderer renderer;
    /**
     *
     * @param client
     * @param screenDimension
     * @param renderer if null, screen coordinates are reported instead of world coordinates.
     */
    public TouchInputManager(TuioClient client, Dimension screenDimension, Renderer renderer) {
        this.tuioClient = client;
        this.screenDimension = screenDimension;
        this.renderer = renderer;

        client.addTuioListener(this);
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
        fireOnPressEvent(createInputEvent(InputEvent.PRESSED, tc));
    }

    @Override
    public void updateTuioCursor(TuioCursor tc) {
        fireOnDragEvent(createInputEvent(InputEvent.DRAGGED, tc));
    }

    @Override
    public void removeTuioCursor(TuioCursor tc) {
        fireOnReleaseEvent(createInputEvent(InputEvent.RELEASED, tc));
    }

    @Override
    public void refresh(TuioTime tt) {
        // NOT USED
    }

    private InputEvent createInputEvent(int type, TuioCursor tc) {
        int x = tc.getScreenX(screenDimension.width);
        int y = tc.getScreenY(screenDimension.height);

        Point point = new Point(x, y);
        Vec2 coords;
        if (renderer != null) {
            SwingUtilities.convertPointFromScreen(point, renderer.getInputComponent());
            coords = renderer.convertWindowToWorldCoordinates(point);
        } else {
            coords = new Vec2(x, y);
        }
        
        InputEvent evt = new InputEvent(type, coords, tc.getCursorID());

        return evt;
    }

    @Override
    public void unregisterEventProvider() {
        tuioClient.removeTuioListener(this);
    }
}
