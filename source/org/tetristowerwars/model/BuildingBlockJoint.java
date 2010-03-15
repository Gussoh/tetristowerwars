/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tetristowerwars.model;

import java.awt.Point;
import org.tetristowerwars.model.building.BuildingBlock;

/**
 *
 * @author Andreas
 */
public class BuildingBlockJoint {
    private final BuildingBlock buildingBlock;
    private Point startPosition;
    private Point endPosition;

    public BuildingBlockJoint(BuildingBlock buildingBlock, Point startPosition, Point endPosition) {
        this.buildingBlock = buildingBlock;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public BuildingBlock getBuildingBlock() {
        return buildingBlock;
    }

    public Point getEndPosition() {
        return endPosition;
    }

    public Point getStartPosition() {
        return startPosition;
    }

    public void setEndPosition(Point endPosition) {
        this.endPosition = endPosition;
    }

    public void setStartPosition(Point startPosition) {
        this.startPosition = startPosition;
    }
}
