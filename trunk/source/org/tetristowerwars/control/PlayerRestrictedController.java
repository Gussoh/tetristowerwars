/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.control;

import java.util.Map.Entry;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Andreas
 */
public class PlayerRestrictedController extends Controller implements GameModelListener {

    private final Player player;

    public PlayerRestrictedController(GameModel dataModel, Renderer renderer, Player player) {
        super(dataModel, renderer);
        dataModel.addGameModelListener(this);
        this.player = player;
    }

    @Override
    protected Block selectBlock(int actionId, Vec2 position) {
        Block block = super.selectBlock(actionId, position);
        if (block == null) {
            return null;
        } else {
            Player owner = block.getOwner();
            if (owner == null || owner == player) {
                return block;
            } 
        }

        return null;
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed, Vec2 contactPoint) {
    }

    @Override
    public void onJointCreation(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockDestruction(Block block) {
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockCreation(Block block) {
    }

    @Override
    public void onBuildingBlockOwnerChanged(BuildingBlock block) {
        if (block.getOwner() != player) {
            boolean found = false;
            int idToRemove = 0;
            for (Entry<Integer, BuildingBlockJoint> entry : actionIdToJoint.entrySet()) {
                if (entry.getValue().getBuildingBlock() == block) {
                    idToRemove = entry.getKey();
                    found = true;
                    break;
                }
            }
            if (found) {
                performReleaseAction(idToRemove);
                gameModel.destroyBuildingBlock(block, 0);
            }
        }

    }

    @Override
    public void onWinningConditionFulfilled(WinningCondition condition) {
    }

    @Override
    public void onLeaderChanged(Player leader) {
    }

    @Override
    public void onGameReset() {
    }

    @Override
    public void onTimerBeat(int beatsLeft) {
    }

    @Override
    public void onPowerupHoverChanged(BuildingBlock hoveredBlock) {
    }
}
