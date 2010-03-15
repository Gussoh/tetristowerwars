/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.util.HashMap;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public class Controller implements InputListener {
    private final GameModel dataModel;
    private final InputManager inputManager;


    public Controller(GameModel dataModel, InputManager inputManager) {
        this.dataModel = dataModel;
        this.inputManager = inputManager;
        inputManager.addInputListener(this);
    }

    @Override
    public void onInputDevicePressed(InputEvent event) {

    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {

    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
        
    }
}
