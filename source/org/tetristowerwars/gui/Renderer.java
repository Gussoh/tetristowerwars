/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.geom.Point2D;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public abstract class Renderer {
    protected final GameModel gameModel;
    protected final Controller controller;

    public Renderer(GameModel dataModel, Controller controller) {
        this.gameModel = dataModel;
        this.controller = controller;
    }

    public abstract void renderFrame();

    public abstract Component getMouseInputComponent();

    public abstract Point2D convertScreenToWorldCoordinates(Point screenCoord);
}
