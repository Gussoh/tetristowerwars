/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.ui;

import org.tetristowerwars.logic.Controller;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public class Renderer {
    private final GameModel dataModel;
    private final Controller controller;

    public Renderer(GameModel dataModel, Controller controller) {
        this.dataModel = dataModel;
        this.controller = controller;
    }


}
