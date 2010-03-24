/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    private final MassData massData;
    private final Rectangle2D[] rectangles;

    protected BuildingBlock(Body body, Material material, float area) {
        super(body);
        this.material = material;
        this.massData = new MassData();
        this.rectangles = null;

        // TODO: Inertias are calculated as if all building blocks are circles/cylinders.
        // If game experience is bad, fix the real inertia calculations.

        float maxSquaredDistance = 0;

        
        
            for (PolygonShape shape = (PolygonShape) body.getShapeList(); shape != null; shape = (PolygonShape) shape.getNext()) {
                Vec2[] vertices = shape.getVertices();
                for (Vec2 vec2 : vertices) {
                    maxSquaredDistance = Math.max(maxSquaredDistance, vec2.x * vec2.x + vec2.y * vec2.y);
                }
            }

            massData.mass = area * material.getDensity();
            massData.I = massData.mass * maxSquaredDistance / 2.0f;
            massData.center.set(0, 0);
            // TODO: Should iterate over bodies
            body.setMass(massData);
        
    }


    protected float calcMomentOfInertia() {

        float totalInertia = 0;

        for (Rectangle2D rectangle : rectangles) {
            float width = (float) rectangle.getWidth();
            float height = (float) rectangle.getHeight();

            float mass = (float) (width * height * material.getDensity());
            float inertiaCenter = mass * (width * width + height * height) / 12.0f;


            float distanceSq = (float) Point2D.distanceSq(0, 0, rectangle.getCenterX(), rectangle.getCenterY());

            totalInertia += inertiaCenter + mass * distanceSq;
        }

        return totalInertia;
    }

    public Material getMaterial() {
        return material;
    }

    public MassData getOriginalMassData() {
        return massData;
    }
}