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
    private final Map<Integer, BuildingBlockJoint> actionIdToJoint = new HashMap<Integer, BuildingBlockJoint>();

    public Controller(GameModel dataModel, InputManager inputManager, Renderer renderer) {
        this.gameModel = dataModel;
        this.inputManager = inputManager;
        this.renderer = renderer;
        inputManager.addInputListener(this);
    }

    @Override
    public void onInputDevicePressed(InputEvent event) {



        Block collisionBlock = gameModel.getBlockFromCoordinates(renderer.convertWindowToWorldCoordinates(event.getPosition()));

        if (collisionBlock == null) {
            renderer.putCursorPoint(event.getActionId(), event.getPosition(), false);
            return;
        }

        if (collisionBlock instanceof BuildingBlock) {
            // Check if a building block joint already exists for this action id
            if (actionIdToJoint.containsKey(event.getActionId()) == true) {
                // TODO: Do we really remove the old actionId? Workaround remove it.
                performReleaseAction(event);
            }

            renderer.putCursorPoint(event.getActionId(), event.getPosition(), true);

            actionIdToJoint.put(event.getActionId(), gameModel.createBuildingBlockJoint((BuildingBlock) collisionBlock, renderer.convertWindowToWorldCoordinates(event.getPosition())));
        } else if (collisionBlock instanceof CannonBlock) {
            // Add a new cannon block with applied force to the world
            gameModel.getBulletFactory().createBullet((CannonBlock) collisionBlock);
        } else if (collisionBlock instanceof BulletBlock) {
        }
    }

    @Override
    public void onInputDeviceReleased(InputEvent event) {
        renderer.removeCursorPoint(event.getActionId());
        performReleaseAction(event);
    }

    private void performReleaseAction(InputEvent event) {
        // If a building block joint exists for this very id
        BuildingBlockJoint bbj = actionIdToJoint.remove(event.getActionId());
        if (bbj != null) {
            gameModel.removeBuldingBlockJoint(bbj);
        }
    }

    @Override
    public void onInputDeviceDragged(InputEvent event) {
        boolean hit = false;
        // If a building block joint exists for this very id
        if (actionIdToJoint.get(event.getActionId()) != null) {
            hit = true;
            gameModel.moveBuildingBlockJoint(actionIdToJoint.get(event.getActionId()), renderer.convertWindowToWorldCoordinates(event.getPosition()));
        }
        
        renderer.putCursorPoint(event.getActionId(), event.getPosition(), hit);
    }

    public void writeEvent(InputEvent event) {
        System.out.print(event.toString());
        System.out.println(", world: " + renderer.convertWindowToWorldCoordinates(event.getPosition()));
    }
}
