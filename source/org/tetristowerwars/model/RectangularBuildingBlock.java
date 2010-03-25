/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Reeen
 */
public class RectangularBuildingBlock extends BuildingBlock {
    private final Rectangle2D[] rectangles;

    public RectangularBuildingBlock(Body body, Material material, Rectangle2D[] rectangles) {
        super(body, material);
        this.rectangles = rectangles;

        calcMassData();
        body.setMass(massData);
    }

    @Override
    protected void calcMassData() {
        massData.I = 0;
        massData.mass = 0;
        massData.center.setZero();

        for (Rectangle2D rectangle : rectangles) {
            float width = (float) rectangle.getWidth();
            float height = (float) rectangle.getHeight();

            float mass = (float) (width * height * material.getDensity());
            float inertiaCenter = mass * (width * width + height * height) / 12.0f;

            float distanceSq = (float) Point2D.distanceSq(0, 0, rectangle.getCenterX(), rectangle.getCenterY());

            massData.I += inertiaCenter + mass * distanceSq;
            massData.mass += mass;
        }
    }

    public Rectangle2D[] getRectangles() {
        return rectangles;
    }
}
