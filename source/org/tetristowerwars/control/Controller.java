/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.util.HashMap;
import java.util.Map;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public class Controller implements InputListener {
    private final GameModel gameModel;
    private final InputManager inputManager;
    private final Map<Integer, BuildingBlockJoint> ownerToBuildingBlockMap = new HashMap<Integer, BuildingBlockJoint>();

    public Controller(GameModel dataModel, InputManager inputManager) {
        this.gameModel = dataModel;
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
