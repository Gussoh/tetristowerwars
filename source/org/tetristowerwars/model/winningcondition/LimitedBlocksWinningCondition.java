/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model.winningcondition;

import java.util.List;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.BuildingBlock;
import org.tetristowerwars.model.BuildingBlockJoint;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.GameModelListener;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.WinningCondition;

/**
 *
 * @author Reeen
 */
public class LimitedBlocksWinningCondition extends WinningCondition implements GameModelListener {
    private final int maxBlocks;
    private int numBlocks;

    public LimitedBlocksWinningCondition(GameModel model, int maxBlocks) {
        super(model);
        this.maxBlocks = maxBlocks;
        model.addGameModelListener(this);
    }

    @Override
    public boolean gameIsOver() {
        //TODO: which pieces to count? Global? Player?
       return numBlocks >= maxBlocks;
    }

    @Override
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed) {
    }

    @Override
    public void onJointCreation(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockDestruction(Block block) {
        if (block instanceof BuildingBlock) {
            numBlocks--;
        }
    }

    @Override
    public void onJointDestruction(BuildingBlockJoint blockJoint) {
    }

    @Override
    public void onBlockCreation(Block block) {
        if (block instanceof BuildingBlock) {
            numBlocks++;
        }
    }

}
