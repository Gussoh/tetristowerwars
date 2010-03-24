/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import org.jbox2d.collision.MassData;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author magnus
 */
public class BuildingBlock extends Block {

    private final Material material;
    private final MassData[] massData;

    protected BuildingBlock(Body[] bodies, Material material, float area) {
        super(bodies);
        this.material = material;
        this.massData = new MassData[bodies.length];

        // TODO: Inertias are calculated as if all building blocks are circles/cylinders.
        // If game experience is bad, fix the real inertia calculations.

        float maxSquaredDistance = 0;

        for (int i = 0; i < bodies.length; i++) {
            Body body = bodies[i];
            for (PolygonShape shape = (PolygonShape) body.getShapeList(); shape != null; shape = (PolygonShape) shape.getNext()) {
                Vec2[] vertices = shape.getVertices();
                for (Vec2 vec2 : vertices) {
                    maxSquaredDistance = Math.max(maxSquaredDistance, vec2.x * vec2.x + vec2.y * vec2.y);
                }
            }

            massData[i] = new MassData();
            massData[i].mass = area * material.getDensity();
            massData[i].I = massData[i].mass * maxSquaredDistance / 2.0f;
            massData[i].center.set(0, 0);
            // TODO: Should iterate over bodies
            bodies[i].setMass(massData[i]);
        }
    }

    public Material getMaterial() {
        return material;
    }

    public MassData getOriginalMassData(int bodyIndex) {
        return massData[bodyIndex];
    }
}
