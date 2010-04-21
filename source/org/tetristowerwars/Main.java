/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tetristowerwars;

import TUIO.TuioClient;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
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
import org.tetristowerwars.model.WinningCondition;
import org.tetristowerwars.model.material.BrickMaterial;
import org.tetristowerwars.model.material.Material;
import org.tetristowerwars.model.material.SteelMaterial;
import org.tetristowerwars.model.material.WoodMaterial;
import org.tetristowerwars.model.winningcondition.CompoundWinningCondition;
import org.tetristowerwars.model.winningcondition.HeightWinningCondition;
import org.tetristowerwars.model.winningcondition.LimitedBlocksWinningCondition;
import org.tetristowerwars.model.winningcondition.TimedWinningCondition;
import org.tetristowerwars.sound.SoundPlayer;
import org.tetristowerwars.util.MathUtil;

/**
 *
 * @author Andreas
 */
public class Main {

    public static final float LEFT_BORDER = 80;
    public static final float RIGHT_BORDER = 160;
    public static final float WORLD_WIDTH = 240;
    public static final float GROUND_HEIGHT = 30;

    public static void main(String[] args) throws InterruptedException {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = ge.getDefaultScreenDevice().getDisplayMode();
        Dimension screenDimensions = new Dimension(displayMode.getWidth(), displayMode.getHeight());

        float blockSize = 5;
        boolean useLightingEffects = true;
        final GameModel gameModel = new GameModel(WORLD_WIDTH, 480, GROUND_HEIGHT, blockSize);
        final Renderer glRenderer = new org.tetristowerwars.gui.GLRenderer(gameModel, useLightingEffects, null);
        //final Renderer renderer = new SwingRenderer(gameModel);
        final SoundPlayer soundPlayer = new SoundPlayer(gameModel);
        final TuioClient tuioClient = new TuioClient();


        final InputManager mouseInputManager = new MouseInputManager(glRenderer.getMouseInputComponent());
        final InputManager touchInputManager = new TouchInputManager(tuioClient, screenDimensions, glRenderer.getMouseInputComponent());

        final Controller mouseController = new Controller(gameModel, mouseInputManager, glRenderer);
        final Controller touchController = new Controller(gameModel, touchInputManager, glRenderer);

        tuioClient.connect();

        Player player1 = gameModel.createPlayer("Player 1", 0, LEFT_BORDER);
        Player player2 = gameModel.createPlayer("Player 2", RIGHT_BORDER, WORLD_WIDTH);

        CannonFactory cannonFactory = gameModel.getCannonFactory();
        cannonFactory.createBasicCannon(player1, new Vec2(LEFT_BORDER, GROUND_HEIGHT), false);
        cannonFactory.createBasicCannon(player2, new Vec2(RIGHT_BORDER, GROUND_HEIGHT), true);

        //WinningCondition win1 = new TimedWinningCondition(gameModel, 3 * 60 * 1000);
        //win1.setWinningCondition();
        //WinningCondition win2 = new LimitedBlocksWinningCondition(gameModel, 20);
        //win2.setWinningCondition();
        WinningCondition win3 = new HeightWinningCondition(gameModel, 100);
        win3.setWinningCondition();
        //ArrayList conditions = new ArrayList();
        //conditions.add(win1);
        //conditions.add(win3);
        //WinningCondition cwin = new CompoundWinningCondition(gameModel, conditions, CompoundWinningCondition.LogicType.AND);
        //cwin.setWinningCondition();

        for (;;) {
            Thread.yield();

            if (gameModel.checkWinningConditions()) {
                mouseInputManager.clearEvents();
                touchInputManager.clearEvents();
            } else {
                mouseInputManager.pumpEvents();
                touchInputManager.pumpEvents();
            }

            if (gameModel.update() > 0) {
                glRenderer.renderFrame();
            }

            if (gameModel.getBuildingBlockPool().size() <= 1) {
                for (int i = 0; i < 7; i++) {
                    createRandomBuildingBlock(gameModel, LEFT_BORDER, RIGHT_BORDER);
                }
            }
        }
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
