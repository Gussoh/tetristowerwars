/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import TUIO.TuioClient;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.control.MouseInputManager;
import org.tetristowerwars.control.TouchInputManager;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.CannonFactory;
import org.tetristowerwars.model.WinningCondition;
import org.tetristowerwars.model.material.ConcreteMaterial;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import org.tetristowerwars.model.winningcondition.HeightWinningCondition;
import org.tetristowerwars.model.winningcondition.LimitedBlocksWinningCondition;
import org.tetristowerwars.model.winningcondition.TimedWinningCondition;
import org.tetristowerwars.sound.SoundPlayer;

/**
 *
 * @author Andreas
 */
public class Main {

    static int kalle = 0;

    public static void main(String[] args) throws InterruptedException {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = ge.getDefaultScreenDevice().getDisplayMode();
        Dimension screenDimensions = new Dimension(displayMode.getWidth(), displayMode.getHeight());

        float blockSize = 5;
        boolean useLightingEffects = true;
        final GameModel gameModel = new GameModel(260, 480, 10, blockSize);
        final Renderer glRenderer = new org.tetristowerwars.gui.GLRenderer(gameModel, useLightingEffects, null);
        //final Renderer renderer = new SwingRenderer(gameModel);
        final SoundPlayer soundPlayer = new SoundPlayer(gameModel);
        final TuioClient tuioClient = new TuioClient();


        final InputManager mouseInputManager = new MouseInputManager(glRenderer.getMouseInputComponent());
        final InputManager touchInputManager = new TouchInputManager(tuioClient, screenDimensions, glRenderer.getMouseInputComponent());

        final Controller mouseController = new Controller(gameModel, mouseInputManager, glRenderer);
        final Controller touchController = new Controller(gameModel, touchInputManager, glRenderer);

        tuioClient.connect();

        Player player1 = gameModel.createPlayer("Player 1", 0, 80);
        Player player2 = gameModel.createPlayer("Player 2", 180, 260);

        CannonFactory cannonFactory = gameModel.getCannonFactory();
        cannonFactory.createBasicCannon(player1, new Vec2(80, 10), false);
        cannonFactory.createBasicCannon(player2, new Vec2(180, 10), true);

        //WinningCondition win1 = new TimedWinningCondition(gameModel, 20000);
        //WinningCondition win2 = new LimitedBlocksWinningCondition(gameModel, 40);
        WinningCondition win3 = new HeightWinningCondition(gameModel, 100);

        for (;;) {
            ++kalle;
            Thread.yield();

            mouseInputManager.pumpEvents();
            touchInputManager.pumpEvents();

            if (!gameModel.checkWinningConditions() && gameModel.update() > 0) {
                //renderer.renderFrame();
                glRenderer.renderFrame();
            }

            if (gameModel.getBuildingBlockPool().size() < 10) {

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createPyramidBlock(new Vec2(120, 400), new ConcreteMaterial());
                }

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createRightSBlock(new Vec2(120, 400), new SteelMaterial());
                }

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createLeftSBlock(new Vec2(130, 400), new SteelMaterial());
                }

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createLineBlock(new Vec2(140, 400), new WoodMaterial());
                }

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createRightLBlock(new Vec2(100, 400), new ConcreteMaterial());
                }

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createLeftLBlock(new Vec2(110, 400), new ConcreteMaterial());
                }

                if (kalle % 50 == 0) {
                    gameModel.getBuildingBlockFactory().createSquareBlock(new Vec2(140, 400), new ConcreteMaterial());
                }

            }
        }
    }
}
