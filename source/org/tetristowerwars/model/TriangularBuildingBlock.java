/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.model;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import org.jbox2d.dynamics.Body;
import org.tetristowerwars.model.material.Material;

/**
 *
 * @author Reeen
 */
public class TriangularBuildingBlock extends BuildingBlock {

    protected final Path2D[] triangles;

    public TriangularBuildingBlock(Body body, Material material, Path2D[] triangles) {
        super(body, material);
        this.triangles = triangles;

        updateMassData();
        body.setMass(massData);
    }

    //TODO: right now triangles must be symmetrical around the y-axis - fix so any orientation works!
    @Override
    protected void updateMassData() {
        massData.I = 0;
        massData.mass = 0;
        massData.center.setZero();

        for (Path2D triangle : triangles) {

            PathIterator iter = triangle.getPathIterator(null);

            // Will hold coordinate pairs (x,y) for the vertices of the triangle
            // Vertice i will be at coordinates[i](x) and coordinates[i+1](y)
            float[] coordinates = new float[8];
            boolean iterIsDone = false;
//            int i = 0;
//            while (!iter.isDone()) {
//                i++;
//                iter.next();
//            }

            for (int i = 0; !iter.isDone(); i += 2) {
                iterIsDone = iter.isDone();
                float[] coord = new float[6];
                iter.currentSegment(coord);
                coordinates[i] = coord[0];
                coordinates[i + 1] = coord[1];
                iter.next();
            }

            float xCenter = coordinates[4]; // x coordinate for top vertice
            float yCenter = Math.abs((coordinates[5] - coordinates[1]) / 2);

            float width = coordinates[2] - coordinates[0];
            float height = Math.abs((coordinates[5] - coordinates[1]) / 2);
            float a = width / 2; //all triangles must be symmetrical!

            float mass = ((width * height) / 2) * material.getDensity();

            // Moment of inertia calculation taken from http://www.efunda.com/math/areas/triangle.cfm
            // Calculation used is 'Polar Moment of Inertia about the Zc axis'
            // Multiply with mass/6 as done for polygon in http://en.wikipedia.org/wiki/List_of_moments_of_inertia
            float inertiaCenter = (float) ((Math.pow(width, 3) * height
                    - Math.pow(width, 2) * height * a
                    + width * height * Math.pow(a, 2)
                    + width * Math.pow(height, 3))
                    / 36f) * (mass/6);

            float distanceSq = (float) Point2D.distanceSq(0, 0, xCenter, yCenter);

            massData.I += inertiaCenter + mass * distanceSq;
            massData.mass += mass;
        }
    }

    public Path2D[] getTriangles() {
        return triangles;
    }
}
