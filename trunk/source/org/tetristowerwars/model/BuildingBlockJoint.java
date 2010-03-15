/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.tetristowerwars.model.building.BuildingBlock;

/**
 *
 * @author Andreas
 */
public class BuildingBlockJoint {
    private final BuildingBlock buildingBlock;
    private final MouseJoint joint;
    private final World world;
    private Vec2 position;

    protected BuildingBlockJoint(World world, BuildingBlock buildingBlock, Vec2 position) {
        this.buildingBlock = buildingBlock;
        this.world = world;
        this.position = position;

        Body blockBody = buildingBlock.getBodies()[0];  // TODO: which one?

        Vec2 localCoordinates = new Vec2(position.x, position.y);
        localCoordinates.x -= blockBody.getPosition().x;
        localCoordinates.y -= blockBody.getPosition().y;

        MouseJointDef mouseJointDef = new MouseJointDef();
        mouseJointDef.body1 = blockBody;
        mouseJointDef.body2 = blockBody;
        mouseJointDef.maxForce = 1000000;
        mouseJointDef.dampingRatio = 1f;
        
        mouseJointDef.target.set(position.x, position.y);

        joint = (MouseJoint) world.createJoint(mouseJointDef);

    }

    public BuildingBlock getBuildingBlock() {
        return buildingBlock;
    }

    public Vec2 getBodyPosition() {
        return joint.getAnchor2();
    }

    public Vec2 getPointerPosition() {
        return position;
    }

    protected void updatePointerPosition(Vec2 position) {
        this.position = position;
        joint.setTarget(position);
    }

    public void destroy() {
        world.destroyJoint(joint);
    }
}
