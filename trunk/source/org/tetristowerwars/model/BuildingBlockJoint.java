/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.tetristowerwars.util.MathUtil;
import org.jbox2d.collision.MassData;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

/**
 *
 * @author Andreas
 */
public class BuildingBlockJoint {

    private final BuildingBlock buildingBlock;
    private final MouseJoint joint;
    private final World world;
    private Vec2 pointerPosition;
    private Vec2 targetPosition;
    private float jointOffset = 0;
    private float maxJointOffset = 20; //TODO: good value?

    protected BuildingBlockJoint(World world, BuildingBlock buildingBlock, Vec2 position) {
        this.buildingBlock = buildingBlock;
        this.world = world;
        this.pointerPosition = position;
        targetPosition = position.clone();

        Body blockBody = buildingBlock.getBody();

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

        MassData newMassData = new MassData(buildingBlock.getOriginalMassData());
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
        return pointerPosition;
    }

    protected void updatePointerPosition(Vec2 position) {
//        boolean goingDown = false;
//        if (position.y < pointerPosition.y) {
//            goingDown = true;
//        }
//        pointerPosition = position;
//
//        if (goingDown) {
//            targetPosition = new Vec2(position.x, position.y - Math.min(jointOffset, maxJointOffset));
//        } else {
//            jointOffset = MathUtil.vecLength(pointerPosition, targetPosition);
//            if (jointOffset < maxJointOffset) {
//                targetPosition = new Vec2(position.x, position.y - jointOffset);
//            } else if (jointOffset >= maxJointOffset) {
//                targetPosition = new Vec2(position.x, position.y - maxJointOffset);
//            }
//        }
//        joint.setTarget(targetPosition);

        //previous code
        pointerPosition = new Vec2(position.x, position.y);
        joint.setTarget(pointerPosition);
    }

    public void destroy(boolean isLastJoint) {
        world.destroyJoint(joint);
        if (isLastJoint) {
            buildingBlock.getBody().setMass(buildingBlock.getOriginalMassData());
            float angVel = buildingBlock.getBody().getAngularVelocity();
            Vec2 vel = buildingBlock.getBody().getLinearVelocity();

            buildingBlock.getBody().setAngularVelocity(angVel * 0.1f);
            buildingBlock.getBody().setLinearVelocity(new Vec2(vel.x * 0.1f, vel.y * 0.1f));
        }
    }

    protected void dampAngularVelocity() {
        Body b = buildingBlock.getBody();

        b.setAngularVelocity(b.getAngularVelocity() * 0.999f);
    }
  }
