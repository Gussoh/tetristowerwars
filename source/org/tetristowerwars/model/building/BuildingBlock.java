/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model.building;

import org.jbox2d.collision.MassData;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.Block;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author magnus
 */
public class BuildingBlock extends Block {

    private final Material material;
    private final MassData massData;

    protected BuildingBlock(Body[] bodies, Material material, float area) {
        super(bodies);
        this.material = material;

        if (bodies.length != 1) {
            throw new IllegalArgumentException("Building blocks with more than 1 body not supported");
        }

        // TODO: Inertias are calculated as if all building blocks are circles/cylinders.
        // If game experience is bad, fix the real inertia calculations.

        float maxSquaredDistance = 0;

        for (PolygonShape shape = (PolygonShape) bodies[0].getShapeList(); shape != null; shape = (PolygonShape) shape.getNext()) {
            Vec2[] vertices = shape.getVertices();
            for (Vec2 vec2 : vertices) {
                maxSquaredDistance = Math.max(maxSquaredDistance, vec2.x * vec2.x + vec2.y * vec2.y);
            }
        }

        massData = new MassData();
        massData.mass = area * material.getDensity();
        massData.I = massData.mass * maxSquaredDistance / 2.0f;
        massData.center.set(0, 0);

        bodies[0].setMass(massData);
    }

    public Material getMaterial() {
        return material;
    }

    public MassData getOriginalMassData() {
        return massData;
    }
}
