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
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.CannonBlock;

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
        renderer.putCursorPoint(event.getActionId(), event.getPosition());

        Block collisionBlock;

        if ((collisionBlock = gameModel.getBlockFromCoordinates(renderer.convertWindowToWorldCoordinates(event.getPosition()))) == null) {
            return;
        }

        if (collisionBlock instanceof BuildingBlock) {
            // Check if a building block joint already exists for this action id
            if (ownerToBuildingBlockMap.containsKey(event.getActionId()) == true) {
                throw new IllegalStateException();
            }

            ownerToBuildingBlockMap.put(event.getActionId(), gameModel.createBuildingBlockJoint((BuildingBlock) collisionBlock, renderer.convertWindowToWorldCoordinates(event.getPosition())));
        } else if (collisionBlock instanceof CannonBlock) {
            // Add a new cannon block with applied force to the world
            gameModel.getBulletFactory().createBullet((CannonBlock) collisionBlock);
        } else if (collisionBlock instanceof BulletBlock) {
        }
    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {
        renderer.removeCursorPoint(event.getActionId());
        // If a building block joint exists for this very id
        if (ownerToBuildingBlockMap.get(event.getActionId()) != null) {
            gameModel.removeBuldingBlockJoint(ownerToBuildingBlockMap.get(event.getActionId()));
            ownerToBuildingBlockMap.remove(event.getActionId());
        }
    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
        renderer.putCursorPoint(event.getActionId(), event.getPosition());
        // If a building block joint exists for this very id
        if (ownerToBuildingBlockMap.get(event.getActionId()) != null) {
            gameModel.moveBuildingBlockJoint(ownerToBuildingBlockMap.get(event.getActionId()), renderer.convertWindowToWorldCoordinates(event.getPosition()));
        }
    }

    public void writeEvent(InputEvent event) {
        System.out.print(event.toString());
        System.out.println(", world: " + renderer.convertWindowToWorldCoordinates(event.getPosition()));
    }
}
