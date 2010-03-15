/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import java.awt.Point;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.tetristowerwars.model.building.BuildingBlock;

/**
 *
 * @author Andreas
 */
public class BuildingBlockJoint {
    private final BuildingBlock buildingBlock;
    private final Body mouseBody;
    private final DistanceJoint joint;
    private final World world;

    protected BuildingBlockJoint(World world, BuildingBlock buildingBlock, Vec2 position) {
        this.buildingBlock = buildingBlock;
        this.world = world;
        BodyDef mouseBodyDef = new BodyDef();
        Body blockBody = buildingBlock.getBodies()[0];  // TODO: which one?
        PolygonDef mouseShapeDef = new PolygonDef();
        
        mouseBodyDef.position.set(position);
        mouseShapeDef.isSensor = true;
        mouseShapeDef.setAsBox(1, 1);

        mouseBody = world.createBody(mouseBodyDef);
        mouseBody.createShape(mouseShapeDef);

        Vec2 localCoordinates = new Vec2(position.x, position.y);
        localCoordinates.x -= blockBody.getPosition().x;
        localCoordinates.y -= blockBody.getPosition().y;

        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.body1 = blockBody;
        distanceJointDef.body2 = mouseBody;
        distanceJointDef.localAnchor1.set(localCoordinates);
        distanceJointDef.localAnchor2.set(0, 0);

        joint = (DistanceJoint) world.createJoint(distanceJointDef);
    }

    public BuildingBlock getBuildingBlock() {
        return buildingBlock;
    }

    public Vec2 getBodyPosition() {
        return joint.getAnchor1();
    }

    public Vec2 getPointerPosition() {
        return joint.getAnchor2();
    }

    protected void updatePointerPosition(Vec2 position) {
        mouseBody.setXForm(position, 0);
    }

    public void destroy() {
        world.destroyJoint(joint);
        world.destroyBody(mouseBody);
    }
}
