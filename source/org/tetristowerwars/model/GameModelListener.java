/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.common.Vec2;

/**
 *
 * @author Andreas
 */
public interface GameModelListener {
    public void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed, Vec2 contactPoint);

    public void onJointCreation(BuildingBlockJoint blockJoint);

    public void onBlockDestruction(Block block);

    public void onJointDestruction(BuildingBlockJoint blockJoint);

    public void onBlockCreation(Block block);

    public void onBuildingBlockOwnerChanged(BuildingBlock block);

    public void onWinningConditionFulfilled(WinningCondition condition);

    public void onLeaderChanged(Player leader);

    public void onGameReset();

    public void onTimerBeat(int beatsLeft);
}
