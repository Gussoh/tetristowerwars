/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.control;

import java.util.HashMap;
import java.util.Map;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.building.BuildingBlock;

/**
 *
 * @author Andreas
 */
public class Controller implements InputListener {
    private final GameModel gameModel;
    private final InputManager inputManager;
	private final Renderer renderer;
    private final Map<Integer, BuildingBlockJoint> ownerToBuildingBlockMap = new HashMap<Integer, BuildingBlockJoint>();

    public Controller(GameModel dataModel, InputManager inputManager, Renderer renderer) {
        this.gameModel = dataModel;
        this.inputManager = inputManager;
		this.renderer = renderer;
        inputManager.addInputListener(this);
    }

    @Override
    public void onInputDevicePressed(InputEvent event) {
		BuildingBlock collisionBlock;

		if ((collisionBlock = (BuildingBlock)gameModel.getBlockFromCoordinates(event.getPosition().x, event.getPosition().y)) == null)
		{
			return;
		}

		ownerToBuildingBlockMap.put(event.getActionId(), gameModel.createBuildingBlockJoint(collisionBlock, renderer.convertScreenToWorldCoordinates(event.getPosition())));
    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {
    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
    }
}
