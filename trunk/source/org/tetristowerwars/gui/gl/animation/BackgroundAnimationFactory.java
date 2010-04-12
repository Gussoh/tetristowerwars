/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars.gui.gl.animation;

import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.gl.BackgroundAnimationRenderer;
import org.tetristowerwars.gui.gl.GLUtil;
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
    private final float worldWidth;

    public BackgroundAnimationFactory(BackgroundAnimationRenderer animationRenderer, float groundLevel, float horizontLevel, float worldWidth) {
        this.animationRenderer = animationRenderer;
        this.groundLevel = groundLevel;
        this.horizontLevel = horizontLevel;
        this.worldWidth = worldWidth;
    }

    public void run(long elapsedTimeMs) {
        totalElapsedTimeMs += elapsedTimeMs;

        if (totalElapsedTimeMs > createTanksAtThisTime) {
            createTankFormation();
            createTanksAtThisTime += 20000;
        } else if (totalElapsedTimeMs > createZeppelinAtThisTime) {
            createZeppelin();
            createZeppelinAtThisTime += 32000;
        }
    }

    private void createTankFormation() {

        float bottom = groundLevel + 10;
        float top = horizontLevel - 3;
        float y = MathUtil.random(bottom, top);

        

        boolean leftToRight = Math.random() < 0.5;
        int tankImage = Math.random() < 0.5 ? BackgroundAnimationRenderer.TANK1 : BackgroundAnimationRenderer.TANK2;

        int numTanks = (int) (Math.random() * 5) + 3;

        boolean zigzag = Math.random() < 0.5 ? true : false;

        for (int i = 0; i < numTanks; i++) {

            float yPos = zigzag ? y + 2 * (i % 2) : y;
            float width = Math.abs(MathUtil.lerp(yPos, bottom, top, -30, -5));

            Vec2 start = new Vec2(-width * i, yPos);
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

    private void createZeppelin() {
        float bottom = horizontLevel + 20;
        float top = horizontLevel + 60;

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

        animationRenderer.addAnimation(path, width, BackgroundAnimationRenderer.ZEPPELIN);
    }
}
