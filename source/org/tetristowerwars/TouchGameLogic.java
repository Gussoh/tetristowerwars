/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import java.awt.Dimension;
import java.awt.DisplayMode;
import org.jbox2d.common.Vec2;
import org.tetristowerwars.control.Controller;
import org.tetristowerwars.control.InputManager;
import org.tetristowerwars.control.MouseInputManager;
import org.tetristowerwars.control.TouchInputManager;
import org.tetristowerwars.gui.Renderer;
import org.tetristowerwars.model.BuildingBlockFactory;
import org.tetristowerwars.model.GameModel;
import org.tetristowerwars.model.Player;
import org.tetristowerwars.model.CannonFactory;
import org.tetristowerwars.model.material.BrickMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import org.tetristowerwars.sound.SoundPlayer;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class TouchGameLogic {

    public TouchGameLogic(MainFrame frame) {

        frame.disableMouseEmulation();

        DisplayMode displayMode = frame.getJFrame().getGraphicsConfiguration().getDevice().getDisplayMode();
        Dimension screenDimensions = new Dimension(displayMode.getWidth(), displayMode.getHeight());
        final Settings settings = frame.getSettings();

        final GameModel gameModel = new GameModel(settings.getWorldWidth(), settings.getWorldHeight(), settings.getGroundHeight(), settings.getBlockSize());
        final Renderer glRenderer = new org.tetristowerwars.gui.GLRenderer(gameModel, frame);

        final SoundPlayer soundPlayer = new SoundPlayer(gameModel);
        
        final InputManager mouseInputManager = new MouseInputManager(glRenderer.getMouseInputComponent());
        final InputManager touchInputManager = new TouchInputManager(frame.getTuioClient(), screenDimensions, glRenderer.getMouseInputComponent());

        final Controller mouseController = new Controller(gameModel, mouseInputManager, glRenderer);
        final Controller touchController = new Controller(gameModel, touchInputManager, glRenderer);

        final float playerAreaWidth = settings.getWorldWidth() * (settings.getPlayerArea() * 0.005f);


        Player player1 = gameModel.createPlayer(settings.getLeftTeamName(), 0, playerAreaWidth);
        Player player2 = gameModel.createPlayer(settings.getRightTeamName(), settings.getWorldWidth() - playerAreaWidth, settings.getWorldWidth());

        CannonFactory cannonFactory = gameModel.getCannonFactory();
        cannonFactory.createBasicCannon(player1, new Vec2(playerAreaWidth, settings.getGroundHeight()), false);
        cannonFactory.createBasicCannon(player2, new Vec2(settings.getWorldWidth() - playerAreaWidth, settings.getGroundHeight()), true);

        //WinningCondition win1 = new TimedWinningCondition(gameModel, 3 * 60 * 1000);
        //win1.setWinningCondition();
        //WinningCondition win2 = new LimitedBlocksWinningCondition(gameModel, 20);
        //win2.setWinningCondition();
        //WinningCondition win3 = new HeightWinningCondition(gameModel, 100);
        //win3.setWinningCondition();
        //ArrayList conditions = new ArrayList();
        //conditions.add(win1);
        //conditions.add(win3);
        //WinningCondition cwin = new CompoundWinningCondition(gameModel, conditions, CompoundWinningCondition.LogicType.AND);
        //cwin.setWinningCondition();

        new Thread() {

            @Override
            public void run() {
                final float constantStepTimeS = 1f / 60f;
                long lastStepTimeNano = System.nanoTime();
                
                for (;;) {
                    Thread.yield();

                    long currentTimeNano = System.nanoTime();
                    long stepTimeNano = currentTimeNano - lastStepTimeNano;
                    int numTimesStepped = 0;

                    if (stepTimeNano < 0) { // in case of nanoTime wrapping around
                        stepTimeNano = (long) (constantStepTimeS * 1000000000.0f);
                    }

                    while (stepTimeNano > (long) (constantStepTimeS * 1000000000.0f) && numTimesStepped < 2) {
                        mouseController.pumpEvents();
                        touchController.pumpEvents();
                        gameModel.update();
                        numTimesStepped++;
                        stepTimeNano -= (long) (constantStepTimeS * 1000000000.0f);
                    }

                    lastStepTimeNano = currentTimeNano - stepTimeNano; // Save remaining step time

                    if (numTimesStepped > 0) {
                        glRenderer.renderFrame();
                    }

                    if (gameModel.getBuildingBlockPool().size() <= 1) {
                        for (int i = 0; i < 7; i++) {
                            createRandomBuildingBlock(gameModel, playerAreaWidth, settings.getWorldWidth() - playerAreaWidth);
                        }
                    }
                }
            }
        }.start();

    }

    public static void createRandomBuildingBlock(GameModel gameModel, float left, float right) {

        BuildingBlockFactory bbf = gameModel.getBuildingBlockFactory();
        double randomValue = Math.random() * 3;

        Vec2 pos = new Vec2(MathUtil.random(left, right), gameModel.getWorldBoundries().upperBound.y - gameModel.getBlockSize() * 5);
        Material material;


        if (randomValue < 1.0) {
            material = new WoodMaterial();
        } else if (randomValue < 2.0) {
            material = new BrickMaterial();
        } else {
            material = new SteelMaterial();
        }

        bbf.createRandomRectangularBuildingBlock(pos, material);
    }
}