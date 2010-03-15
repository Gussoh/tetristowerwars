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
        Block collisionBlock;

	if ((collisionBlock = gameModel.getBlockFromCoordinates(event.getPosition().x, event.getPosition().y)) == null)
	{
            return;
        }

        if (collisionBlock instanceof BuildingBlock) {
            // Check if a building block joint already exists for this action id
            if (ownerToBuildingBlockMap.containsKey(event.getActionId()) == true) {
                throw new IllegalStateException();
            }

            ownerToBuildingBlockMap.put(event.getActionId(), gameModel.createBuildingBlockJoint((BuildingBlock)collisionBlock, renderer.convertScreenToWorldCoordinates(event.getPosition())));
        }
        /*
        else if (collisionBlock instanceof CanonBlock) {
        }
        else if (collisionBlock instanceof BulletBlock) {
        }
        */
    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {
        // If a building block joint exists for this very id
        if (ownerToBuildingBlockMap.get(event.getActionId()) != null) {
            gameModel.removeBuldingBlockJoint(ownerToBuildingBlockMap.get(event.getActionId()));
        }
    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
        // If a building block joint exists for this very id
        if (ownerToBuildingBlockMap.get(event.getActionId()) != null) {
            gameModel.moveBuildingBlockJoint(ownerToBuildingBlockMap.get(event.getActionId()), renderer.convertScreenToWorldCoordinates(event.getPosition()));
        }
    }
}
