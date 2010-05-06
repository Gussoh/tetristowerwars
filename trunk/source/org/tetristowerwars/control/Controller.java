/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.control;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
    private final Map<Integer, Point> currentCursorPositions = new LinkedHashMap<Integer, Point>();

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

        currentCursorPositions.put(event.getActionId(), event.getPosition());

        if (actionIdToJoint.containsKey(event.getActionId()) == true) {
            return; // Ignore TUIO bugs where an ID can appear twice on the screen
        }

        selectBlock(event.getActionId(), event.getPosition());
    }

    private void selectBlock(int actionId, Point position) {
        if (actionIdToJoint.containsKey(actionId)) {
            return;
        }

        Block selectedBlock = gameModel.getBlockFromCoordinates(renderer.convertWindowToWorldCoordinates(position));

        if (selectedBlock == null) {
            renderer.putCursorPoint(actionId, position, false);
            return;
        }

        if (selectedBlock instanceof BuildingBlock) {

            if (gameModel.getWinningCondition() != null && gameModel.getWinningCondition().isGameOver()) {
                return; // Do not let user create joints when game is over.
            }

            renderer.putCursorPoint(actionId, position, true);
            actionIdToJoint.put(actionId, gameModel.createBuildingBlockJoint((BuildingBlock) selectedBlock, renderer.convertWindowToWorldCoordinates(position)));
        } else if (selectedBlock instanceof CannonBlock) {
            // Add a new cannon block with applied force to the world
            //gameModel.getBulletFactory().createBullet((CannonBlock) collisionBlock);
        } else if (selectedBlock instanceof BulletBlock) {
        } else if (selectedBlock instanceof TriggerBlock) {
            TriggerBlock triggerBlock = (TriggerBlock) selectedBlock;
            if (triggerBlock.isVisible()) {
                triggerBlock.getTriggerListener().onTriggerPressed(triggerBlock);
                actionIdToTrigger.put(actionId, triggerBlock);
            }
        }
    }

    @Override
    public synchronized void onInputDeviceReleased(InputEvent event) {
        eventQueue.offer(event);
    }

    private void handleInputDeviceReleased(InputEvent event) {
        currentCursorPositions.remove(event.getActionId());

        performReleaseAction(event);
    }

    private void performReleaseAction(InputEvent event) {

        currentCursorPositions.remove(event.getActionId());

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
        currentCursorPositions.put(event.getActionId(), event.getPosition());

        boolean hit = false;
        // If a building block joint exists for this very id
        BuildingBlockJoint bbj = actionIdToJoint.get(event.getActionId());
        if (bbj != null) {
            hit = true;

            Vec2 newPointerPosition = renderer.convertWindowToWorldCoordinates(event.getPosition());
            if (newPointerPosition.sub(bbj.getPointerPosition()).length() > 40) {
                performReleaseAction(event);
            } else {
                gameModel.moveBuildingBlockJoint(actionIdToJoint.get(event.getActionId()), renderer.convertWindowToWorldCoordinates(event.getPosition()));
            }
        } else {
            selectBlock(event.getActionId(), event.getPosition());
        }

        renderer.putCursorPoint(event.getActionId(), event.getPosition(), hit);
    }

    public void writeEvent(InputEvent event) {
        System.out.print(event.toString());
        System.out.println(", world: " + renderer.convertWindowToWorldCoordinates(event.getPosition()));
    }

    public synchronized void pumpEvents() {

        if (gameModel.getWinningCondition().isGameOver()) {
            LinkedHashSet<Integer> actionSet = new LinkedHashSet<Integer>(actionIdToJoint.keySet());

            for (Integer id : actionSet) {
                performReleaseAction(new InputEvent(InputEvent.RELEASED, new Point(), id));
            }
        }

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


        for (Map.Entry<Integer, Point> entry : currentCursorPositions.entrySet()) {

            selectBlock(entry.getKey(), entry.getValue());
        }
    }
}
