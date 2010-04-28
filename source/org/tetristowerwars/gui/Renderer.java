/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui;

import java.awt.Component;
import java.awt.Point;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public abstract class Renderer {

    protected final GameModel gameModel;

    public Renderer(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public abstract void renderFrame();

    public abstract Component getInputComponent();

    public abstract Vec2 convertWindowToWorldCoordinates(Point windowCoord);

    /**
     *
     * @param id An id for the point.
     * @param point The point in window coordinates.
     */
    public abstract void putCursorPoint(int id, Point point, boolean hit);
    public abstract void removeCursorPoint(int id);
}
