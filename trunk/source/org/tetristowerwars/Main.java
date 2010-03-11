/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.gui.SwingRenderer;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.material.ConcreteMaterial;

/**
 *
 * @author Andreas
 */
public class Main {

    static int kalle = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello tetris tower wars!");
        float blockSize = 5;
        final GameModel gameModel = new GameModel(640, 480, 30, blockSize);
        final Renderer renderer = new SwingRenderer(gameModel, null);

        for (;;) {
            kalle++;
            Thread.sleep(20);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    renderer.renderFrame();

                    if (kalle % 50 == 0) {

                        BuildingBlock b = gameModel.getBlockFactory().createPyramidBlock(new Vec2(320, 400),
                                new ConcreteMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    if (kalle % 200 == 25) {

                        BuildingBlock b = gameModel.getBlockFactory().createLineBlock(new Vec2(300, 400),
                                new ConcreteMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    gameModel.update();
                }
            });
           

        }
    }
}
