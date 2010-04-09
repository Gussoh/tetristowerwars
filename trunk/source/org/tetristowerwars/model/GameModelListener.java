/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import java.util.ArrayList;

/**
 *
 * @author Andreas
 */
public interface GameModelListener {
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed);

    public void onJointCreation(BuildingBlockJoint blockJoint);

    public void onBlockDestruction(Block block);

    public void onJointDestruction(BuildingBlockJoint blockJoint);

    public void onBlockCreation(Block block);

    public void onBuildingBlockOwnerChanged(BuildingBlock block);

    public void onWinningConditionFulfilled(WinningCondition condition);

    public abstract void onLeaderChanged(ArrayList scoreList);
}
