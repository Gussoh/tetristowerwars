/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import org.jbox2d.collision.MassData;
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
    private final MassData originalMassData;

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
        mouseJointDef.maxForce = 5000; // TODO: Is this a good value?
        mouseJointDef.dampingRatio = 1f;
        
        mouseJointDef.target.set(position.x, position.y);

        joint = (MouseJoint) world.createJoint(mouseJointDef);

        originalMassData = new MassData();
        originalMassData.mass = blockBody.getMass();
        originalMassData.I = blockBody.getInertia();
        originalMassData.center = blockBody.getLocalCenter();

        MassData newMassData = new MassData(originalMassData);
        newMassData.mass /= 200;
        newMassData.I /= 200;
        blockBody.setMass(newMassData);
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
        buildingBlock.getBodies()[0].setMass(originalMassData);
    }

    protected void dampAngularVelocity() {
        Body b = buildingBlock.getBodies()[0];

        b.setAngularVelocity(b.getAngularVelocity() * 0.999f);
    }
}
