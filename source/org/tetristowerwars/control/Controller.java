/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.control;

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

    protected final GameModel gameModel;
    private final Renderer renderer;
    protected final Map<Integer, BuildingBlockJoint> actionIdToJoint = new HashMap<Integer, BuildingBlockJoint>();
    private final Map<Integer, TriggerBlock> actionIdToTrigger = new HashMap<Integer, TriggerBlock>();
    private final Queue<InputEvent> eventQueue = new LinkedList<InputEvent>();
    private final Map<Integer, Vec2> currentCursorPositions = new LinkedHashMap<Integer, Vec2>();

    public Controller(GameModel dataModel, Renderer renderer) {
        this.gameModel = dataModel;
        this.renderer = renderer;
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

        Block b = selectBlock(event.getActionId(), event.getPosition());
        handleSelectedBlock(event.getActionId(), event.getPosition(), b);
    }

    protected Block selectBlock(int actionId, Vec2 position) {
        if (actionIdToJoint.containsKey(actionId)) {
            return null;
        }

        Block selectedBlock = gameModel.getBlockFromCoordinates(position);

        if (selectedBlock == null) {
            renderer.putCursorPoint(actionId, position, false);
            return null;
        }

        return selectedBlock;
    }

    protected void handleSelectedBlock(int actionId, Vec2 position, Block selectedBlock) {
        if (selectedBlock instanceof BuildingBlock) {

            if (gameModel.getWinningCondition() != null && gameModel.getWinningCondition().isGameOver()) {
                return; // Do not let user create joints when game is over.
            }

            renderer.putCursorPoint(actionId, position, true);
            actionIdToJoint.put(actionId, gameModel.createBuildingBlockJoint((BuildingBlock) selectedBlock, position));
        } else if (selectedBlock instanceof CannonBlock) {
            // Add a new cannon block with applied force to the world
            //gameModel.getBulletFactory().createBullet((CannonBlock) collisionBlock);
        } else if (selectedBlock instanceof BulletBlock) {
        } else if (selectedBlock instanceof TriggerBlock) {
            TriggerBlock triggerBlock = (TriggerBlock) selectedBlock;
            if (triggerBlock.isVisible() && !actionIdToTrigger.containsKey(actionId)) {
                triggerBlock.getTriggerListener().onTriggerPressed(triggerBlock, this);
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

        performReleaseAction(event.getActionId());
    }

    protected void performReleaseAction(int actionId) {

        currentCursorPositions.remove(actionId);

        // If a building block joint exists for this very id
        renderer.removeCursorPoint(actionId);
        BuildingBlockJoint bbj = actionIdToJoint.remove(actionId);
        if (bbj != null) {
            gameModel.removeBuldingBlockJoint(bbj);
        } else {
            TriggerBlock triggerBlock = actionIdToTrigger.remove(actionId);
            if (triggerBlock != null) {
                triggerBlock.getTriggerListener().onTriggerReleased(triggerBlock, this);
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

            Vec2 newPos = event.getPosition();
            if (newPos.sub(bbj.getPointerPosition()).length() > 40) {
                performReleaseAction(event.getActionId());
            } else {
                gameModel.moveBuildingBlockJoint(actionIdToJoint.get(event.getActionId()), event.getPosition());
            }
        } else {
            Block b = selectBlock(event.getActionId(), event.getPosition());
            handleSelectedBlock(event.getActionId(), event.getPosition(), b);
        }

        renderer.putCursorPoint(event.getActionId(), event.getPosition(), hit);
    }

    
    public synchronized void pumpEvents() {

        if (gameModel.getWinningCondition().isGameOver()) {
            LinkedHashSet<Integer> actionSet = new LinkedHashSet<Integer>(actionIdToJoint.keySet());

            for (Integer id : actionSet) {
                performReleaseAction(id);
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


        for (Map.Entry<Integer, Vec2> entry : currentCursorPositions.entrySet()) {
            Block b = selectBlock(entry.getKey(), entry.getValue());
            handleSelectedBlock(entry.getKey(), entry.getValue(), b);
        }

        for (TriggerBlock triggerBlock : actionIdToTrigger.values()) {
            triggerBlock.getTriggerListener().onTriggerHold(triggerBlock, this);
        }
    }
}
