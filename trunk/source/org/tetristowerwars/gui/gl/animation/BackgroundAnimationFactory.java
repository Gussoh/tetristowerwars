/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.animation;

import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.BackgroundAnimationRenderer;
import static org.tetristowerwars.gui.gl.BackgroundAnimationRenderer.*;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class BackgroundAnimationFactory {

    private final BackgroundAnimationRenderer animationRenderer;
    private final float groundLevel;
    private final float horizontLevel;
    private long totalElapsedTimeMs = 0;
    private long createTanksAtThisTime = 3000;
    private long createZeppelinAtThisTime = 5000;
    private long createSputnikAtThisTime = 30000;
    private final float worldWidth;
    private final static int TANK_INTERVAL_MS = 20000;
    private final static int ZEPPELIN_INTERVAL_MS = 32000;
    private final static int SPUTNIK_INTERVAL_MS = 60000;

    public BackgroundAnimationFactory(BackgroundAnimationRenderer animationRenderer, float groundLevel, float horizontLevel, float worldWidth) {
        this.animationRenderer = animationRenderer;
        this.groundLevel = groundLevel;
        this.horizontLevel = horizontLevel;
        this.worldWidth = worldWidth;
    }

    public void run(long elapsedTimeMs) {
        totalElapsedTimeMs += elapsedTimeMs;

        if (totalElapsedTimeMs > createTanksAtThisTime) {
            createGroundFormation();
            createTanksAtThisTime += TANK_INTERVAL_MS;
        } else if (totalElapsedTimeMs > createZeppelinAtThisTime) {
            createAirVehicle();
            createZeppelinAtThisTime += ZEPPELIN_INTERVAL_MS;
        } else if (totalElapsedTimeMs > createSputnikAtThisTime) {
            createSputnik();
            createSputnikAtThisTime += SPUTNIK_INTERVAL_MS;
        }
    }

    private void createGroundFormation() {

        float bottom = groundLevel + 10;
        float top = horizontLevel - 3;
        float y = MathUtil.random(bottom, top);



        boolean leftToRight = Math.random() < 0.5;
        int tankImage;
        double randomValue = Math.random();

        if (randomValue < 0.33) {
            tankImage = TANK1;
        } else if (randomValue < 0.66) {
            tankImage = TANK2;
        } else {
            tankImage = SCUD;
        }


        int numTanks = (int) (Math.random() * 5) + 3;

        boolean zigzag = true;//Math.random() < 0.5 ? true : false;

        for (int i = 0; i < numTanks; i++) {

            float yPos = zigzag ? y + 2 * (i % 2) : y;
            float width = Math.abs(MathUtil.lerp(yPos, bottom, top, -30, -5));

            Vec2 start = new Vec2(-width * (i + 1), yPos);
            Vec2 end = new Vec2(worldWidth + width * (numTanks - i), yPos);

            float timeToFinish = MathUtil.lerp(y, bottom, top, 20000, 180000);
            Path path;
            if (leftToRight) {
                path = new Path(start, end, timeToFinish);
            } else {
                path = new Path(end, start, timeToFinish);
            }

            animationRenderer.addAnimation(path, width, tankImage);
        }
    }

    private void createAirVehicle() {
        float bottom = horizontLevel + 20;
        float top = horizontLevel + 100;

        float y = MathUtil.random(bottom, top);
        float width = MathUtil.random(10, 25);

        float travelTime = Math.abs(MathUtil.lerp(width, 10, 25, -400000, -100000));

        boolean leftToRight = Math.random() < 0.5 ? true : false;

        Vec2 start = new Vec2(-width, y);
        Vec2 end = new Vec2(worldWidth + width, y);

        Path path;

        if (leftToRight) {
            path = new Path(start, end, travelTime);
        } else {
            path = new Path(end, start, travelTime);
        }

        int image = Math.random() < 0.5 ? ZEPPELIN1 : ZEPPELIN2;

        animationRenderer.addAnimation(path, width, image);
    }

    public void createSputnik() {

        float sputnikWidth = 10;

        float left = -sputnikWidth;
        float right = worldWidth + sputnikWidth;

        float top = horizontLevel + 100;

        Vec2 start, end;
        if (Math.random() < 0.5) {
            start = new Vec2(left, horizontLevel);
            end = new Vec2(right, top);
        } else {
            start = new Vec2(right, horizontLevel);
            end = new Vec2(left, top);
        }

        Path p = new Path(start, end, 15000);

        animationRenderer.addAnimation(p, sputnikWidth, SPUTNIK);
    }
}
