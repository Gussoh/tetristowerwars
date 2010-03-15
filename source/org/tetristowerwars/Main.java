/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.MouseEventController;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.gui.SwingRenderer;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.material.AluminiumMaterial;
import org.tetristowerwars.model.material.ConcreteMaterial;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;

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
            ++kalle;
            Thread.sleep(20);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    renderer.renderFrame();

                    if (kalle % 50 == 0) {

                        BuildingBlock b = gameModel.getBlockFactory().createPyramidBlock(new Vec2(240, 400),
                                new ConcreteMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    if (kalle % 50 == 0) {

                        BuildingBlock b = gameModel.getBlockFactory().createSBlock(new Vec2(280, 400),
                                new AluminiumMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    if (kalle % 50 == 0) {

                        BuildingBlock b = gameModel.getBlockFactory().createSquareBlock(new Vec2(320, 400),
                                new SteelMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    if (kalle % 50 == 0) {

                        BuildingBlock b = gameModel.getBlockFactory().createLineBlock(new Vec2(360, 400),
                                new WoodMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    if (kalle % 50 == 0) {

                        BuildingBlock b = gameModel.getBlockFactory().createLBlock(new Vec2(400, 400),
                                new ConcreteMaterial());
                        gameModel.addToBlockPool(b);
                    }

                    if (kalle % 400 == 0) {
                        System.out.println("Number of Objects: " + gameModel.getWorld().getBodyCount());
                    }

                    gameModel.update();
                }
                
            });
           

        }
    }
}
