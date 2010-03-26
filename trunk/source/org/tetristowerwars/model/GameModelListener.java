/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

/**
 *
 * @author Andreas
 */
public interface GameModelListener {
    public abstract void onBlockCollision(Block block1, Block block2, float collisionSpeed, float tangentSpeed);

    public void onJointCreation(BuildingBlockJoint blockJoint);

    public void onBlockDestruction(Block block);

    public void onJointDestruction(BuildingBlockJoint blockJoint);
}