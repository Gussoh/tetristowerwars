/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.gui.SwingRenderer;
import org.tetristowerwars.model.GameModel;

/**
 *
 * @author Andreas
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello tetris tower wars!");
        GameModel gameModel = new GameModel(640, 480, 30);
        Renderer renderer = new SwingRenderer(gameModel, null);
        int kalle = 0;
        for (;;) {
            kalle++;
            Thread.sleep(10);
            renderer.renderFrame();

            if (kalle % 50 == 0) {

                gameModel.addBody();
            }

            gameModel.update();
        }
    }
}
