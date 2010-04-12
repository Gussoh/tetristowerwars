/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.animation;

import org.jbox2d.common.Vec2;

/**
 *
 * @author Andreas
 */
public class Path {

    private final Vec2 start, end;
    private final float travelTime;
    private float currentTime;

    public Path(Vec2 start, Vec2 end, float travelTime) {
        this.start = start;
        this.end = end;
        this.travelTime = travelTime;
        currentTime = 0;
    }

    public void setElapsedTime(float time) {
        currentTime = Math.min(time, travelTime);
    }

    public void setRatio(float ratio) {
        if (ratio < 0) {
            currentTime = 0;
        } else if (ratio > 1) {
            currentTime = travelTime;
        } else {
            currentTime = travelTime * ratio;
        }
    }

    public void addTime(float time) {
        currentTime += time;
        currentTime = Math.min(currentTime, travelTime);
    }

    /**
     * Returns the current position. Note that the all calculation is done in this method.
     * Re-use the returned value instead of calling this method repeatedly.
     *
     * @return
     */
    public Vec2 getCurrentPosition() {
        float ratio = getRatio();
        return new Vec2(start.x * (1 - ratio) + end.x * ratio, start.y * (1 - ratio) + end.y * ratio);
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public float getRatio() {
        return currentTime / travelTime;
    }

    public boolean isDone() {
        return travelTime - currentTime < 0.0001f;
    }

    public boolean isLeftToright() {
        return end.x - start.x > 0;
    }
}
