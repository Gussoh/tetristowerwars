/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BulletBlock;
import org.tetristowerwars.model.CannonBlock;
import org.tetristowerwars.model.TriggerBlock;

/**
 *
 * @author Andreas
 */
public class Controller implements InputListener {

    private final GameModel gameModel;
    private final InputManager inputManager;
    private final Renderer renderer;
    private final Map<Integer, BuildingBlockJoint> actionIdToJoint = new HashMap<Integer, BuildingBlockJoint>();
    private final Map<Integer, TriggerBlock> actionIdToTrigger = new HashMap<Integer, TriggerBlock>();
    private final Queue<InputEvent> eventQueue = new LinkedList<InputEvent>();

    public Controller(GameModel dataModel, InputManager inputManager, Renderer renderer) {
        this.gameModel = dataModel;
        this.inputManager = inputManager;
        this.renderer = renderer;
        inputManager.addInputListener(this);
    }

    @Override
    public synchronized void onInputDevicePressed(InputEvent event) {
        eventQueue.offer(event);
    }

    private void handleInputDevicePressed(InputEvent event) {

        if (actionIdToJoint.containsKey(event.getActionId()) == true) {
            return; // Ignore TUIO bugs where an ID can appear twice on the screen
        }

        Block selectedBlock = gameModel.getBlockFromCoordinates(renderer.convertWindowToWorldCoordinates(event.getPosition()));

        if (selectedBlock == null) {
            renderer.putCursorPoint(event.getActionId(), event.getPosition(), false);
            return;
        }

        if (selectedBlock instanceof BuildingBlock) {
            // Check if a building block joint already exists for this action id
           /* if (actionIdToJoint.containsKey(event.getActionId()) == true) {
            // TODO: Do we really remove the old actionId? Workaround remove it.
            performReleaseAction(event);
            }*/

            renderer.putCursorPoint(event.getActionId(), event.getPosition(), true);

            actionIdToJoint.put(event.getActionId(), gameModel.createBuildingBlockJoint((BuildingBlock) selectedBlock, renderer.convertWindowToWorldCoordinates(event.getPosition())));
        } else if (selectedBlock instanceof CannonBlock) {
            // Add a new cannon block with applied force to the world
            //gameModel.getBulletFactory().createBullet((CannonBlock) collisionBlock);
        } else if (selectedBlock instanceof BulletBlock) {
        } else if (selectedBlock instanceof TriggerBlock) {
            TriggerBlock triggerBlock = (TriggerBlock) selectedBlock;
            triggerBlock.getTriggerListener().onTriggerPressed(triggerBlock);
            actionIdToTrigger.put(event.getActionId(), triggerBlock);
        }
    }

    @Override
    public synchronized void onInputDeviceReleased(InputEvent event) {
        eventQueue.offer(event);
    }

    private void handleInputDeviceReleased(InputEvent event) {
        performReleaseAction(event);
    }

    private void performReleaseAction(InputEvent event) {
        // If a building block joint exists for this very id
        renderer.removeCursorPoint(event.getActionId());
        BuildingBlockJoint bbj = actionIdToJoint.remove(event.getActionId());
        if (bbj != null) {
            gameModel.removeBuldingBlockJoint(bbj);
        } else {
            TriggerBlock triggerBlock = actionIdToTrigger.remove(event.getActionId());
            if (triggerBlock != null) {
                triggerBlock.getTriggerListener().onTriggerReleased(triggerBlock);
            }
        }
    }

    @Override
    public synchronized void onInputDeviceDragged(InputEvent event) {
        eventQueue.offer(event);
    }

    private void handleInputDeviceDragged(InputEvent event) {
        boolean hit = false;
        // If a building block joint exists for this very id
        if (actionIdToJoint.get(event.getActionId()) != null) {
            hit = true;
            BuildingBlockJoint bbj = actionIdToJoint.get(event.getActionId());
            Vec2 newPointerPosition = renderer.convertWindowToWorldCoordinates(event.getPosition());
            if (newPointerPosition.sub(bbj.getPointerPosition()).length() > 40) {
                performReleaseAction(event);
            } else {
                gameModel.moveBuildingBlockJoint(actionIdToJoint.get(event.getActionId()), renderer.convertWindowToWorldCoordinates(event.getPosition()));
            }
        }

        renderer.putCursorPoint(event.getActionId(), event.getPosition(), hit);
    }

    public void writeEvent(InputEvent event) {
        System.out.print(event.toString());
        System.out.println(", world: " + renderer.convertWindowToWorldCoordinates(event.getPosition()));
    }

    public synchronized void pumpEvents() {

        while (!eventQueue.isEmpty()) {
            InputEvent event = eventQueue.poll();
            switch (event.getType()) {
                case InputEvent.PRESSED:
                    handleInputDevicePressed(event);
                    break;
                case InputEvent.RELEASED:
                    handleInputDeviceReleased(event);
                    break;
                case InputEvent.DRAGGED:
                    handleInputDeviceDragged(event);
                    break;
            }
        }

    }
}
