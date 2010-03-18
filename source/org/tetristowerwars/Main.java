/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import TUIO.TuioClient;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.control.MouseInputManager;
import org.tetristowerwars.control.TouchInputManager;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.gui.SwingRenderer;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.building.BuildingBlock;
import org.tetristowerwars.model.cannon.CannonBlock;
import org.tetristowerwars.model.cannon.CannonFactory;
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


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = ge.getDefaultScreenDevice().getDisplayMode();
        Dimension screenDimensions = new Dimension(displayMode.getWidth(), displayMode.getHeight());

        float blockSize = 5;
        final GameModel gameModel = new GameModel(300, 480, 30, blockSize, 100);
        final Renderer renderer = new SwingRenderer(gameModel);
        final TuioClient tuioClient = new TuioClient();


        final InputManager mouseInputManager = new MouseInputManager(renderer.getMouseInputComponent());
        final InputManager touchInputManager = new TouchInputManager(tuioClient, screenDimensions, renderer.getMouseInputComponent());

        final Controller mouseController = new Controller(gameModel, mouseInputManager, renderer);
        final Controller touchController = new Controller(gameModel, touchInputManager, renderer);

        tuioClient.connect();

        Player player1 = gameModel.createPlayer("Player 1");
        Player player2 = gameModel.createPlayer("Player 2");

        CannonFactory cannonFactory = gameModel.getCannonFactory();
        cannonFactory.createBasicCannon(player1, new Vec2(80, 30));
        cannonFactory.createBasicCannon(player2, new Vec2(180, 30));

        for (;;) {
            ++kalle;
            Thread.sleep(20);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    renderer.renderFrame();

                    if (gameModel.getWorld().getBodyCount() < 10) {

                        if (kalle % 50 == 0) {
                            gameModel.getBlockFactory().createPyramidBlock(new Vec2(40, 400), new ConcreteMaterial());
                        }

                        if (kalle % 50 == 0) {
                            gameModel.getBlockFactory().createSBlock(new Vec2(20, 400), new SteelMaterial());
                        }

                        if (kalle % 50 == 0) {
                            gameModel.getBlockFactory().createLineBlock(new Vec2(60, 400), new WoodMaterial());
                        }

                        if (kalle % 50 == 0) {
                            gameModel.getBlockFactory().createLBlock(new Vec2(04, 400), new ConcreteMaterial());
                        }

                        if (kalle % 50 == 0) {
                            gameModel.getBlockFactory().createSquareBlock(new Vec2(40, 400), new ConcreteMaterial());
                        }

                        if (kalle % 400 == 0) {
                            System.out.println("Number of Objects: " + gameModel.getWorld().getBodyCount());
                        }
                    }

                    gameModel.update();
                }
            });
        }
    }
}
